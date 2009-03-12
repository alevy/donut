package edu.washington.cs.cse490h.donut.server;

import org.apache.thrift.TException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.washington.cs.cse490h.donut.business.TNode;

/**
 * @author alevy
 */
public class DonutPeer {

    public static final String DONUT_NODE = "DonutNode";
    public static final String DONUT_REQUEST_SERVER = "DonutRequestServer";
    
    private final DonutServer server;
    private final DonutClient client;
    private final DonutServer requestThread;

    @Inject
    public DonutPeer(@Named(value = DONUT_NODE) DonutServer serverThread, DonutClient client,
            @Named(value = DONUT_REQUEST_SERVER) DonutServer requestThread) {
        this.server = serverThread;
        this.client = client;
        this.requestThread = requestThread;
    }

    public void run(TNode knownNode) throws TException {
        server.start();
        client.join(knownNode);
        client.start();
        requestThread.start();
    }

    public void kill() {
        requestThread.kill();
        server.kill();
        client.kill();
    }
}
