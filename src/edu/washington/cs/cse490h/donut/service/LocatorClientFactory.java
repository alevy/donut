package edu.washington.cs.cse490h.donut.service;

import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 *
 */
public interface LocatorClientFactory {

    Iface get(String node) throws ConnectionFailedException;
    
}
