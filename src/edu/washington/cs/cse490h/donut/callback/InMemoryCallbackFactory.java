package edu.washington.cs.cse490h.donut.callback;

import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransport;

import com.google.inject.Inject;

import edu.washington.edu.cs.cse490h.donut.service.LocatorCallback;
import edu.washington.edu.cs.cse490h.donut.service.LocatorCallback.Iface;

/**
 * @author alevy
 *
 */
public class InMemoryCallbackFactory implements LocatorCallbackFactory {

    private final Map<String, TTransport> bufMap;

    @Inject
    public InMemoryCallbackFactory(Map<String, TTransport> bufMap) {
        this.bufMap = bufMap;
    }
    
    public Iface get(String caller) throws ConnectionFailedException {
        TBinaryProtocol prot = new TBinaryProtocol(bufMap.get(caller));
        return new LocatorCallback.Client(prot);
    }

}
