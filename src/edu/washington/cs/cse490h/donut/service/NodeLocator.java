package edu.washington.cs.cse490h.donut.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.Data;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
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

    public String findSuccessor(KeyId entryId) throws TException {
        LOGGER.info("Request for entity with id \"" + entryId.toString() + "\".");
        Node next = node.closestPrecedingNode(entryId);
        if (next == node) {
            return node.getSuccessor().getName();
        }
        try {
            return clientFactory.get(next).findSuccessor(entryId);
        } catch (ConnectionFailedException e) {
            throw new TException();
        }
    }

    public Data get(KeyId entryId) throws TException {
        Data data = new Data();
        data.setData(dataMap.get(entryId));
        if (data.getData() == null) {
            data.setExists(false);
        } else {
            data.setExists(true);
        }
        return data;
    }

    public void put(KeyId entryId, Data data) throws TException {
        if (data.isExists()) {
            dataMap.put(entryId, data.getData());
        } else {
            dataMap.remove(entryId);
        }
    }
}
