package edu.washington.cs.cse490h.donut.util;

import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class DonutEvent implements Comparable<DonutEvent> {
    private final int             seconds;
    private final DonutTestRunner testRunner;
    private DonutClosure          closure;

    public DonutEvent(int seconds, DonutTestRunner testRunner) {
        this.seconds = seconds;
        this.testRunner = testRunner;
    }

    public void test(DonutTestCase test) {
        closure = test;
    }

    public void join(int nodeNum, int knownNode) {
        TNode node = testRunner.node(knownNode).getTNode();
        closure = new DonutJoinClosure(testRunner.client(nodeNum), node);
    }

    public void leave(int nodeNum) {
        closure = new DonutLeaveClosure(testRunner.client(nodeNum), testRunner.getClientFactory(),
                testRunner.node(nodeNum).getTNode());
    }

    public void run() {
        closure.run();
    }

    public int getSeconds() {
        return seconds;
    }

    public int compareTo(DonutEvent o) {
        return this.seconds - o.seconds;
    }
}