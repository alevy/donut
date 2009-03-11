package edu.washington.cs.cse490h.donut.util;

import edu.washington.cs.cse490h.donut.service.thrift.TNode;

/**
 * Holds information about an event to be run, including
 * 
 * @author alevy
 */
public class DonutEvent implements Comparable<DonutEvent> {
    private final int             milliseconds;
    private final DonutTestRunner testRunner;
    private DonutClosure          closure;

    public DonutEvent(int milliseconds, DonutTestRunner testRunner) {
        this.milliseconds = milliseconds;
        this.testRunner = testRunner;
    }

    /**
     * @param test the {@link DonutTestCase} to run.
     */
    public void test(DonutTestCase test) {
        setClosure(test);
    }

    /**
     * Join {@link Node} with index {@code nodeNum} to the Donut ring referencing the {@Node}
     * with index {@code knownNode} as the known {@link Node} in the ring.
     * 
     * @param nodeNum
     */
    public void join(int nodeNum, int knownNode) {
        TNode node = testRunner.node(knownNode).getTNode();
        setClosure(new DonutJoinClosure(testRunner.client(nodeNum), node));
    }

    public void joinNewNode(int knownNode, String name, long id) {
        TNode node = testRunner.node(knownNode).getTNode();
        setClosure(new DonutJoinNewClosure(testRunner, node, name, id));
    }

    /**
     * Leave {@link Node} with index {@code nodeNum} from the Donut ring.
     * 
     * @param nodeNum
     */
    public void leave(int nodeNum) {
        setClosure(new DonutLeaveClosure(testRunner.client(nodeNum), testRunner.getClientFactory(),
                testRunner.node(nodeNum).getTNode()));
    }

    public void run() throws Exception {
        getClosure().run();
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public int compareTo(DonutEvent o) {
        return this.milliseconds - o.milliseconds;
    }

    public void setClosure(DonutClosure closure) {
        this.closure = closure;
    }

    public DonutClosure getClosure() {
        return closure;
    }
}
