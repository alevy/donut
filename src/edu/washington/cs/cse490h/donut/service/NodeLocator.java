package edu.washington.cs.cse490h.donut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;
import edu.washington.edu.cs.cse490h.donut.service.DonutData;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.NodeNotFoundException;
import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class NodeLocator implements Iface {

    private static Logger              LOGGER;
    private final Node                 node;
    private final LocatorClientFactory clientFactory;
    private final Map<KeyId, byte[]>   dataMap;

    static {
        LOGGER = Logger.getLogger(NodeLocator.class.getName());
        LOGGER.setLevel(Level.WARNING);
    }

    @Inject
    public NodeLocator(Node node, LocatorClientFactory clientFactory) {
        this.node = node;
        this.clientFactory = clientFactory;
        this.dataMap = new HashMap<KeyId, byte[]>();
    }

    public TNode findSuccessor(KeyId entryId) throws TException {
        LOGGER.info("Request for entity with id \"" + entryId.toString() + "\"");
        TNode next = node.closestPrecedingNode(entryId);
        if (next.equals(node.getTNode())) {
            LOGGER.info("I am the predecessor for \"" + entryId.toString() + "\"");
            return node.getSuccessor();
        }
        try {
            TNode successor = clientFactory.get(next).findSuccessor(entryId);
            clientFactory.release(next);
            return successor;
        } catch (RetryFailedException e) {
            throw new TException(e);
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

    public Map<KeyId, byte[]> getDataMap() {
        return dataMap;
    }

    /*
     * Should do nothing if connection completes. If the connection fails, then a TException is
     * thrown.
     */
    public void ping() throws TException {

    }

    public TNode getPredecessor() throws TException, NodeNotFoundException {
        TNode predecessor = node.getPredecessor();
        if (predecessor == null) {
            throw new NodeNotFoundException();
        }
        return predecessor;
    }

    public void notify(TNode n) throws TException {
        if (!n.equals(node.getTNode())
                && (node.getPredecessor() == null || KeyIdUtil.isAfterXButBeforeEqualY(n
                        .getNodeId(), node.getPredecessor().getNodeId(), node.getNodeId()))) {
            node.setPredecessor(n);
        }

    }

    public List<TNode> getFingers() throws TException {
        return node.getFingers();
    }

}
