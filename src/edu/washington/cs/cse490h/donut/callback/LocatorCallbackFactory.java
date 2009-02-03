package edu.washington.cs.cse490h.donut.callback;

import edu.washington.edu.cs.cse490h.donut.service.LocatorCallback;

/**
 * @author alevy
 *
 */
public interface LocatorCallbackFactory {
    LocatorCallback.Iface get(String caller) throws ConnectionFailedException;
}
