package edu.washington.cs.cse490h.donut.service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.service.application.DonutHashTableService;
import edu.washington.cs.cse490h.donut.service.thrift.Constants;
import edu.washington.cs.cse490h.donut.service.thrift.DataNotFoundException;
import edu.washington.cs.cse490h.donut.service.thrift.DataPair;
import edu.washington.cs.cse490h.donut.service.thrift.EntryKey;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.service.thrift.NodeNotFoundException;
import edu.washington.cs.cse490h.donut.service.thrift.NotResponsibleForId;
import edu.washington.cs.cse490h.donut.service.thrift.TNode;
import edu.washington.cs.cse490h.donut.service.thrift.KeyLocator.Iface;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
 */
public class NodeLocator implements Iface {

    private static Logger               LOGGER;
    private final Node                  node;
    private final LocatorClientFactory  clientFactory;
    private final DonutHashTableService service;

    static {
        LOGGER = Logger.getLogger(NodeLocator.class.getName());
        LOGGER.setLevel(Level.WARNING);
    }

    @Inject
    public NodeLocator(Node node, DonutHashTableService service, LocatorClientFactory clientFactory) {
        this.node = node;
        this.service = service;
        this.clientFactory = clientFactory;
    }

    public TNode findSuccessor(KeyId entryId) throws TException {
        LOGGER.info("Request for entity [" + printNode(this.node.getTNode()) + "]: Id - \""
                + entryId.toString() + "\"");
        TNode next = node.closestPrecedingNode(entryId);
        if (next.equals(node.getTNode())) {
            LOGGER.info("I am predecessor [" + printNode(this.node.getTNode()) + "]: Id \""
                    + entryId.toString() + "\"");
            return node.getSuccessor();
        }
        try {
            LOGGER.info("I am NOT the predecessor [" + printNode(this.node.getTNode()) + "]: Id \""
                    + entryId.toString() + "\" \n" + "Connecting to " + next.getPort());
            TNode successor = clientFactory.get(next).findSuccessor(entryId);
            clientFactory.release(next);
            return successor;
        } catch (RetryFailedException e) {
            throw new TException(e);
        }
    }

    public byte[] get(EntryKey key) throws TException, DataNotFoundException {
        LOGGER.info("Get entity with id \"" + key.toString() + "\".");
        DataPair data = service.get(key);
        if (data == null) {
            throw new DataNotFoundException();
        }
        return data.getData();
    }

    public void put(EntryKey key, byte[] data) throws TException, NotResponsibleForId {
        if (!KeyIdUtil.isAfterXButBeforeEqualY(key.getId(), node.getPredecessor().getNodeId(), node
                .getNodeId())) {
            LOGGER.info("Not responsible for entity with id \"" + key.toString() + "\".");
            throw new NotResponsibleForId(key.getId());
        }
        LOGGER.info("Put \"" + data + "\" into entity with id \"" + key.toString() + "\".");
        service.put(key, data, Constants.SUCCESSOR_LIST_SIZE);
        TNode successor = node.getSuccessor();
        if (!successor.equals(node.getTNode())) {
            try {
                clientFactory.get(successor).replicatePut(key, data,
                        Constants.SUCCESSOR_LIST_SIZE - 1);
                clientFactory.release(successor);
            } catch (RetryFailedException e) {
                throw new TException(e);
            }
        }
    }

    public void remove(EntryKey key) throws TException, NotResponsibleForId {
        if (!KeyIdUtil.isAfterXButBeforeEqualY(key.getId(), node.getPredecessor().getNodeId(), node
                .getNodeId())) {
            LOGGER.info("Not responsible for entity with id \"" + key.toString() + "\".");
            throw new NotResponsibleForId(key.getId());
        }
        LOGGER.info("Remove entity with id \"" + key.toString() + "\".");
        service.remove(key);
        TNode successor = node.getSuccessor();
        if (!node.getSuccessor().equals(node.getTNode())) {
            try {
                clientFactory.get(successor)
                        .replicateRemove(key, Constants.SUCCESSOR_LIST_SIZE - 1);
                clientFactory.release(successor);
            } catch (RetryFailedException e) {
                throw new TException(e);
            }
        }
    }

    public void replicatePut(EntryKey key, byte[] data, int numReplicas) throws TException {
        LOGGER.info("Put \"" + data + "\" into entity with id \"" + key.toString() + "\".");
        service.put(key, data, numReplicas);
        if (numReplicas > 0) {
            TNode successor = node.getSuccessor();
            try {
                clientFactory.get(successor).replicatePut(key, data, numReplicas - 1);
                clientFactory.release(successor);
            } catch (RetryFailedException e) {
                throw new TException(e);
            }
        }
    }

    public void replicateRemove(EntryKey key, int numReplicas) throws TException {
        LOGGER.info("Remove entity with id \"" + key.toString() + "\".");
        service.remove(key);
        if (numReplicas > 0) {
            TNode successor = node.getSuccessor();
            try {
                clientFactory.get(successor).replicateRemove(key, numReplicas - 1);
                clientFactory.release(successor);
            } catch (RetryFailedException e) {
                throw new TException(e);
            }
        }
    }

    public DonutHashTableService getService() {
        return service;
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

    public List<TNode> notify(TNode n) throws TException {
        if (node.getPredecessor() == null
                || KeyIdUtil.isAfterXButBeforeEqualY(n.getNodeId(), node.getPredecessor()
                        .getNodeId(), node.getNodeId())) {
            if (node.getPredecessor() == null) {
                TNode successor = node.getSuccessor();

                // Copy data that belongs to me from my successor
                try {
                    Iface successorClient = clientFactory.get(successor);
                    copyData(successorClient, successorClient.getDataRange(n.getNodeId(), node
                            .getNodeId()));
                    clientFactory.release(successor);
                } catch (RetryFailedException e) {
                    throw new TException(e);
                }
            }

            // Copy data that I should replicate from new predecessor
            try {
                Iface predecessorClient = clientFactory.get(n);
                copyData(predecessorClient, predecessorClient.getDataRange(node.getNodeId(), n
                        .getNodeId()));
                clientFactory.release(n);
            } catch (RetryFailedException e) {
                throw new TException(e);
            }
            node.setPredecessor(n);
        }
        return node.getSuccessorList();
    }

    private void copyData(Iface client, Set<EntryKey> keySet) throws TException {
        try {
            for (EntryKey key : keySet) {
                byte[] data;
                data = client.get(key);
                service.put(key, data, Constants.SUCCESSOR_LIST_SIZE);
            }
        } catch (DataNotFoundException e) {
            // We were lied to! Die gracefully
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    public List<TNode> getFingers() throws TException {
        return node.getFingers();
    }

    public String printNode(TNode n) {
        if (n == null)
            return "NULL";
        else
            return "" + n.getName();
    }

    public Set<EntryKey> getDataRange(KeyId start, KeyId end) throws TException {
        return service.getRange(start, end);
    }

}
