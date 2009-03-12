package edu.washington.cs.cse490h.donut.service;

import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public interface LocatorClientFactory {

    Iface get(TNode node) throws RetryFailedException;

    void release(TNode node);

}
