package edu.washington.cs.cse490h.donut.service;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.cse490h.donut.business.KeyId;
import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class LocalLocatorClientFactory implements LocatorClientFactory {

    private Map<KeyId, NodeLocator> locatorMap = new HashMap<KeyId, NodeLocator>();

    public synchronized void add(TNode node, NodeLocator nodeLocator) {
        locatorMap.put(node.getNodeId(), nodeLocator);
    }

    public void remove(TNode node) {
        locatorMap.remove(node.getNodeId());
    }

    public synchronized Iface get(TNode node) throws RetryFailedException {
        if (locatorMap.containsKey(node.getNodeId())) {
            return locatorMap.get(node.getNodeId());
        }

        // The node wasn't found.
        throw new RetryFailedException();
    }

    public void release(TNode node) {
        // Do nothing
    }

}
