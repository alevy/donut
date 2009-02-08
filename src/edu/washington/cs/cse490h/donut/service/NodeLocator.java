package edu.washington.cs.cse490h.donut.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.DonutData;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class NodeLocator implements Iface {

    private static Logger              LOGGER  = Logger.getLogger(NodeLocator.class.getName());
    private final Node                 node;
    private final LocatorClientFactory clientFactory;
    private final Map<KeyId, byte[]>   dataMap = new HashMap<KeyId, byte[]>();

    @Inject
    public NodeLocator(Node node, LocatorClientFactory clientFactory) {
        this.node = node;
        this.clientFactory = clientFactory;
    }

    public TNode findSuccessor(KeyId entryId) throws TException {
        LOGGER.info("Request for entity with id \"" + entryId.toString() + "\".");
        Node next = node.closestPrecedingNode(entryId);
        if (next == node) {
            return node.getSuccessor().getTNode();
        }
        try {
            return clientFactory.get(next).findSuccessor(entryId);
        } catch (ConnectionFailedException e) {
            throw new TException(e);
        }
    }

    public void join(Node n) throws TException {
        try {
            TNode found = clientFactory.get(n).findSuccessor(this.node.getNodeId());
            this.node.join(new Node(found));
        } catch (ConnectionFailedException e) {
            throw new TException();
        }
    }

    public DonutData get(KeyId entryId) throws TException {
        LOGGER.info("Get entity with id \"" + entryId.toString() + "\".");
        DonutData data = new DonutData();
        data.setData(dataMap.get(entryId));
        if (data.getData() != null) {
            data.setExists(true);
        } else {
            data.setExists(false);
        }
        return data;
    }

    public void put(KeyId entryId, DonutData data) throws TException {
        LOGGER.info("Put \"" + data + "\" into entity with id \"" + entryId.toString() + "\".");
        if (data.isExists()) {
            dataMap.put(entryId, data.getData());
        } else {
            dataMap.remove(entryId);
        }
    }

}
