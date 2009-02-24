package edu.washington.cs.cse490h.donut.util;

import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.service.LocalLocatorClientFactory;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * Donut closure for leaving nodes
 * @author alevy
 *
 */
public class DonutLeaveClosure extends DonutClosure {

    private final DonutClient               client;
    private final LocalLocatorClientFactory clientFactory;
    private final TNode                     node;

    /**
     * @param client
     * @param clientFactory
     * @param node the {@link TNode} to remove.
     */
    public DonutLeaveClosure(DonutClient client,
            LocalLocatorClientFactory clientFactory, TNode node) {
        this.client = client;
        this.clientFactory = clientFactory;
        this.node = node;
    }

    @Override
    public void run() {
        clientFactory.remove(node);
        client.kill();
    }
}
