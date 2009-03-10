package edu.washington.cs.cse490h.donut.util;

import org.apache.thrift.TException;

import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class DonutJoinNewClosure extends DonutClosure {

    private final DonutTestRunner testRunner;
    private final TNode           knownNode;
    private final String          name;
    private final long            id;

    public DonutJoinNewClosure(DonutTestRunner testRunner, TNode knownNode, String name, long id) {
        this.testRunner = testRunner;
        this.knownNode = knownNode;
        this.name = name;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            int node = testRunner.createAndAddNode(name, id);
            DonutClient donutClient = testRunner.client(node);
            donutClient.join(knownNode);
            donutClient.start();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}