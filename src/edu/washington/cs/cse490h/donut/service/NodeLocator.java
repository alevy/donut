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

    private static Logger              LOGGER;
    private final Node                 node;
    private final LocatorClientFactory clientFactory;
    private final Map<KeyId, byte[]>   dataMap;
    private int                        nextFingerToUpdate;

    static {
        LOGGER = Logger.getLogger(NodeLocator.class.getName());
    }

    @Inject
    public NodeLocator(Node node, LocatorClientFactory clientFactory) {
        this.node = node;
        this.clientFactory = clientFactory;
        this.dataMap = new HashMap<KeyId, byte[]>();
        this.nextFingerToUpdate = 0;
    }

    public TNode findSuccessor(KeyId entryId) throws TException {
        LOGGER.info(this.node.getTNode() + ": Request for entity with id \"" + entryId.toString() + "\".");
        Node next = node.closestPrecedingNode(entryId);
        if (next.equals(node)) {
            return node.getSuccessor().getTNode();
        }
        try {
            return clientFactory.get(next.getTNode()).findSuccessor(entryId);
        } catch (ConnectionFailedException e) {
            throw new TException(e);
        }
    }

    public void join(Node n) throws TException {
        try {
            TNode found = clientFactory.get(n.getTNode()).findSuccessor(this.node.getNodeId());
            this.node.join(new Node(found));
        } catch (ConnectionFailedException e) {
            throw new TException();
        }
    }

    public DonutData get(KeyId entryId) throws TException {
        LOGGER.info(this.node + ": Get entity with id \"" + entryId.toString() + "\".");
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

    // Should do nothing if connection completes.
    // If the connection fails, then a TException is thrown.
    public void ping() throws TException {

    }

    public boolean ping(Node n) {
        try {
            clientFactory.get(n.getTNode()).ping();
            return true;
        } catch (ConnectionFailedException e) {
            return false;
        } catch (TException e) {
            // Thrift error. Take a look at the trace
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Called periodically. Checks whether the predecessor has failed.
     */
    public void checkPredecessor() {
        if (this.node.getPredecessor() != null && !ping(this.node.getPredecessor())) {
            // A predecessor is defined but could not be reached. Nullify the current predecessor
            this.node.setPredecessor(null);
        }
    }

    /**
     * Called periodically. Refreshes the finger table entries. nextFingerToUpdate stores the index
     * of the next finger to fix.
     * 
     * @throws TException
     */
    public void fixFingers() throws TException {
        if (nextFingerToUpdate >= Node.KEYSPACESIZE)
            nextFingerToUpdate = 0;

        Node updatedFinger = new Node(findSuccessor(new KeyId(
                this.node.getNodeId().getId() + 2 << nextFingerToUpdate - 1)));

        this.node.setFinger(nextFingerToUpdate, updatedFinger);

        nextFingerToUpdate = nextFingerToUpdate + 1;
    }
}
