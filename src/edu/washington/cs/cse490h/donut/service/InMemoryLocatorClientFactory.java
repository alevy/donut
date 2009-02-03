package edu.washington.cs.cse490h.donut.service;

import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransport;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.callback.ConnectionFailedException;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 *
 */
public class InMemoryLocatorClientFactory implements LocatorClientFactory {

    private final Map<String, TTransport> bufMap;

    @Inject
    public InMemoryLocatorClientFactory(Map<String, TTransport> bufMap) {
        this.bufMap = bufMap;
    }
    
    @Override
    public Iface get(String caller) throws ConnectionFailedException {
        TBinaryProtocol prot = new TBinaryProtocol(bufMap.get(caller));
        return new KeyLocator.Client(prot);
    }

}
