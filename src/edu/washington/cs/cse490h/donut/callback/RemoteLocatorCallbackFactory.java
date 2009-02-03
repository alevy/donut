package edu.washington.cs.cse490h.donut.callback;

import java.net.Socket;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

import edu.washington.cs.cse490h.donut.AbstractRetriable;
import edu.washington.edu.cs.cse490h.donut.service.LocatorCallback;

/**
 * @author alevy
 */
public class RemoteLocatorCallbackFactory extends AbstractRetriable<LocatorCallback.Iface>
        implements LocatorCallbackFactory {

    @Override
    public LocatorCallback.Iface tryOne(String caller) throws Exception {
        TBinaryProtocol protocol = new TBinaryProtocol(new TSocket(new Socket(caller, 8081)));
        return new LocatorCallback.Client(protocol);
    }

}
