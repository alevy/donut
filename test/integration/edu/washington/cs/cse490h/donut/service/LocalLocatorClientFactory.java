package edu.washington.cs.cse490h.donut.service;

import java.util.HashMap;
import java.util.Map;

import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class LocalLocatorClientFactory implements LocatorClientFactory {

    private Map<TNode, NodeLocator> locatorMap = new HashMap<TNode, NodeLocator>();

    public void add(TNode node, NodeLocator nodeLocator) {
        locatorMap.put(node, nodeLocator);
    }

    public void remove(TNode node) {
        locatorMap.remove(node);
    }

    public Iface get(TNode node) throws RetryFailedException {
        if (locatorMap.containsKey(node)) {
            return locatorMap.get(node);
        }
        throw new RetryFailedException();
    }

    public void release(TNode node) {
        // Do nothing
    }

}
