package edu.washington.cs.cse490h.donut.util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.service.LocalLocatorClientFactory;
import edu.washington.cs.cse490h.donut.service.NodeLocator;
import edu.washington.cs.cse490h.donut.service.RetryFailedException;
import edu.washington.cs.cse490h.donut.service.application.DonutInMemoryHashTableService;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.service.thrift.KeyLocator.Iface;

/**
 * <p>
 * Provides a framework for setting up, running, and testing an in memory Donut ring.
 * </p>
 * <p>
 * Example usage: Joins three nodes and drops one.
 * 
 * <pre>
 * DonutTestRunner donutTestRunner = new DonutTestRunner(0x0L, 0xCF00L, -0x4444L);
 * donutTestRunner.addEvent(0).join(0, 0);
 * donutTestRunner.addEvent(1000).join(1, 0);
 * donutTestRunner.addEvent(2000).join(2, 0);
 * donutTestRunner.addEvent(3000).leave(2);
 * donutTestRunner.addEvent(4000).test(new DonutTestCase() {
 *     public void test() {
 *       assertEquals(...)
 *       ...
 *     }
 *   });
 * donutTestRunner.run();
 * </pre>
 * 
 * </p>
 * 
 * @author alevy
 */
public class DonutTestRunner {

    private final List<Node>                          nodeList;
    private final List<DonutInMemoryHashTableService> serviceList;
    private final LocalLocatorClientFactory           clientFactory;
    private final List<DonutClient>                   clientList;
    private final PriorityQueue<DonutEvent>           eventList;

    /**
     * @param ids
     *            the ids of {@link Node}s that will be available to the Donut
     */
    public DonutTestRunner(long... ids) {
        nodeList = new ArrayList<Node>();
        serviceList = new ArrayList<DonutInMemoryHashTableService>();
        eventList = new PriorityQueue<DonutEvent>();
        clientList = new ArrayList<DonutClient>();
        clientFactory = new LocalLocatorClientFactory();
        for (int i = 0; i < ids.length; ++i) {
            createAndAddNode("node" + i, ids[i]);
        }
    }

    public int createAndAddNode(String name, long id) {
        Node node = new Node(name, 8080, new KeyId(id));
        nodeList.add(node);
        DonutInMemoryHashTableService service = new DonutInMemoryHashTableService();
        serviceList.add(service);
        NodeLocator nodeLocator = new NodeLocator(node, service, getClientFactory());
        clientFactory.add(node.getTNode(), nodeLocator);
        clientList.add(new DonutClient(node, getClientFactory()));
        return clientList.size() - 1;
    }

    /**
     * Adds an event to the test
     * 
     * @param milliseconds
     *            the elapsed time in milliseconds, from the time {@link #run} is called, after
     *            which this event will be triggered.
     * @return a {@link DonutEvent} which is used to add functionality to the event by calls to
     *         {@link DonutEvent#join}, {@link DonutEvent#leave}, {@link DonutEvent#test}, etc'.
     */
    public DonutEvent addEvent(int milliseconds) {
        DonutEvent donutEvent = new DonutEvent(milliseconds, this);
        getEventList().add(donutEvent);
        return donutEvent;
    }

    /**
     * Returns the {@link Node} referneced in this {@link DonutTestRunner} by the index specified.
     */
    public Node node(int index) {
        return nodeList.get(index);
    }

    /**
     * Returns the {@link DonutInMemoryHashTableService} referneced in this {@link DonutTestRunner}
     * by the index specified.
     */
    public DonutInMemoryHashTableService service(int index) {
        return serviceList.get(index);
    }

    protected LocalLocatorClientFactory getClientFactory() {
        return clientFactory;
    }
    
    public Iface iface(int index) throws RetryFailedException {
        return clientFactory.get(node(index).getTNode());
    }

    protected DonutClient client(int index) {
        return clientList.get(index);
    }

    protected PriorityQueue<DonutEvent> getEventList() {
        return eventList;
    }

    /**
     * Runs the test.
     */
    public void run() throws Exception {
        long start = System.currentTimeMillis();
        while (!eventList.isEmpty()) {
            if (eventList.peek().getMilliseconds() <= (System.currentTimeMillis() - start)) {
                eventList.poll().run();
            }
        }
        for (DonutClient client : clientList) {
            client.kill();
        }
    }
}
