package edu.washington.cs.cse490h.donut.util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.service.LocalLocatorClientFactory;
import edu.washington.cs.cse490h.donut.service.NodeLocator;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class DonutTestRunner {

    private final List<Node>                nodeList;
    private final LocalLocatorClientFactory clientFactory;
    private final List<DonutClient>         clientList;
    private final PriorityQueue<DonutEvent> eventList;

    public DonutTestRunner(long... ids) {
        nodeList = new ArrayList<Node>();
        eventList = new PriorityQueue<DonutEvent>();
        clientList = new ArrayList<DonutClient>();
        clientFactory = new LocalLocatorClientFactory();
        for (int i = 0; i < ids.length; ++i) {
            Node node = new Node("node" + i, 8080, new KeyId(ids[i]));
            nodeList.add(node);
            NodeLocator nodeLocator = new NodeLocator(node, getClientFactory());
            clientFactory.add(node.getTNode(), nodeLocator);
            clientFactory.printKeySet();
            clientList.add(new DonutClient(node, getClientFactory()));
        }
    }

    public DonutEvent addEvent(int milliseconds) {
        DonutEvent donutEvent = new DonutEvent(milliseconds, this);
        getEventList().add(donutEvent);
        return donutEvent;
    }

    public Node node(int index) {
        return nodeList.get(index);
    }

    public LocalLocatorClientFactory getClientFactory() {
        return clientFactory;
    }

    public DonutClient client(int index) {
        return clientList.get(index);
    }

    public PriorityQueue<DonutEvent> getEventList() {
        return eventList;
    }

    @SuppressWarnings("deprecation")
    public void run() {
        long start = System.currentTimeMillis();
        while (!eventList.isEmpty()) {
            if (eventList.peek().getSeconds() <= (System.currentTimeMillis() - start)) {
                eventList.poll().run();
            }
        }
        for (DonutClient client : clientList) {
            client.stop();
        }
    }

}
