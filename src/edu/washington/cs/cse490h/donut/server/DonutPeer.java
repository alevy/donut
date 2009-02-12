package edu.washington.cs.cse490h.donut.server;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.edu.cs.cse490h.donut.service.TNode;


/**
 * @author alevy
 *
 */
public class DonutPeer {

    private final DonutServer server;
    private final DonutClient client;

    @Inject
    public DonutPeer(DonutServer serverThread, DonutClient client) {
        this.server = serverThread;
        this.client = client;
    }
    
    public void run(TNode knownNode) throws TException {
        server.start();
        client.join(knownNode);
        client.start();
    }
    
}
