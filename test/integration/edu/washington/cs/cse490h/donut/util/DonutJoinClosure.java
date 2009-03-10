package edu.washington.cs.cse490h.donut.util;

import org.apache.thrift.TException;

import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class DonutJoinClosure extends DonutClosure {

    private final DonutClient donutClient;
    private final TNode       knownNode;

    public DonutJoinClosure(DonutClient donutClient, TNode knownNode) {
        this.donutClient = donutClient;
        this.knownNode = knownNode;
    }

    @Override
    public void run() {
        try {
            donutClient.join(knownNode);
            donutClient.start();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}