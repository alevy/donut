package edu.washington.cs.cse490h.donut.service;

import edu.washington.cs.cse490h.donut.service.thrift.TNode;
import edu.washington.cs.cse490h.donut.service.thrift.KeyLocator.Iface;

/**
 * @author alevy
 *
 */
public interface LocatorClientFactory {

    Iface get(TNode node) throws RetryFailedException;
    
    void release(TNode node);
    
}
