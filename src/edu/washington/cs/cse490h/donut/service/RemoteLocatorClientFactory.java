package edu.washington.cs.cse490h.donut.service;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy
 */
public class RemoteLocatorClientFactory extends AbstractRetriable<KeyLocator.Iface, TNode>
        implements LocatorClientFactory {

    private Map<TNode, Socket> socketMap = new HashMap<TNode, Socket>();

    @Override
    public synchronized KeyLocator.Iface tryOne(TNode node) throws Exception {
        TProtocol protocol;
        while (socketMap.containsKey(node)) {
            wait();
        }
        
        Socket socket = new Socket(node.getName(), node.getPort());
        socketMap.put(node, socket);
        protocol = new TBinaryProtocol(new TSocket(socket));
        return new KeyLocator.Client(protocol);
    }

    public synchronized void release(TNode node) {
        Socket socket = socketMap.remove(node);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notify();
    }
}
