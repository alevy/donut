package edu.washington.cs.cse490h.donut.service;

import java.net.Socket;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

import edu.washington.cs.cse490h.donut.AbstractRetriable;
import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;

/**
 * @author alevy
 */
public class RemoteLocatorClientFactory extends AbstractRetriable<KeyLocator.Iface, Node> implements
        LocatorClientFactory {

    @Override
    public KeyLocator.Iface tryOne(Node node) throws Exception {
        TBinaryProtocol protocol;
        protocol = new TBinaryProtocol(new TSocket(new Socket(node.getName(), node.getPort())));
        return new KeyLocator.Client(protocol);
    }

}
