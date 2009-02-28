package edu.washington.cs.cse490h.donut.server;

import java.util.List;
import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.service.RetryFailedException;
import edu.washington.cs.cse490h.donut.service.LocatorClientFactory;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.NodeNotFoundException;
import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class DonutClient extends Thread {
    private static final Logger        LOGGER;

    private final Node                 node;
    private final LocatorClientFactory clientFactory;

    private int                        nextFingerToUpdate;

    static {
        LOGGER = Logger.getLogger(DonutClient.class.getName());
    }

    @Inject
    public DonutClient(Node node, LocatorClientFactory clientFactory) {
        this.node = node;
        this.clientFactory = clientFactory;
        nextFingerToUpdate = 0;
    }

    public void join(TNode n) throws TException {
        LOGGER.info(printNode(this.node.getTNode()) + " - Joining Donut by node: " + printNode(n));
        try {
            if (!n.equals(node.getTNode())) {
                TNode found = clientFactory.get(n).findSuccessor(node.getNodeId());
                node.setSuccessor(found);
                clientFactory.release(n);
            }
        } catch (RetryFailedException e) {
            throw new TException(e);
        }
        LOGGER.info(printNode(this.node.getTNode()) + " - Joined Donut!");
    }

    public boolean ping(TNode n) {
        try {
            clientFactory.get(n).ping();
            clientFactory.release(n);
            return true;
        } catch (RetryFailedException e) {
            return false;
        } catch (TException e) {
            // Thrift error. Take a look at the trace
            clientFactory.release(n);
            LOGGER.warning(e.toString());
            return false;
        }
    }

    /**
     * Called periodically. Checks whether the predecessor has failed.
     */
    public void checkPredecessor() {
        if (this.node.getPredecessor() != null && !ping(this.node.getPredecessor())) {
            // A predecessor is defined but could not be reached. Nullify the current predecessor
            LOGGER.warning("Node " + node.getPredecessor() + " is down!");
            this.node.setPredecessor(null);
        }
    }

    /**
     * Called periodically. Refreshes the finger table entries. nextFingerToUpdate stores the index
     * of the next finger to fix.
     */
    public void fixFingers() {
        fixFinger(nextFingerToUpdate);

        nextFingerToUpdate = (nextFingerToUpdate + 1) % node.getFingers().size();
    }

    public void fixFinger(int finger) {
        // Even if the successor is self, we still might need to fix other fingers.
        // Do NOT, therefore include this code (left in until we're sure of this statement)
        // if (node.getSuccessor().equals(node.getTNode())) {
        // return;
        // }
        if (node.getPort() == 8081 && finger > 1) {
            @SuppressWarnings("unused")
            int j = 0;
        }

        Iface iface;
        try {
            iface = clientFactory.get(node.getTNode());
        } catch (RetryFailedException e1) {
            LOGGER.warning("Something funny in the sockets is going down");
            e1.printStackTrace();
            return;
        }

        // Keep as seperate variable: Be careful of some weirdass java background shit with ints
        long base = node.getNodeId().getId();
        long pow = 0x0000000000000001L << finger;
        long id = base + pow;

        KeyId keyId = new KeyId(id);
        try {
            TNode updatedFinger = iface.findSuccessor(keyId);
            this.node.setFinger(finger, updatedFinger);
        } catch (TException e1) {
            LOGGER.warning("Thrift exception on findSuccessor in fixFingers");
        }

        clientFactory.release(node.getTNode());
    }

    /**
     * Called periodically. Verify's immediate successor, and tell's successor about us.
     */
    public void stabilize() {

        TNode x = null;
        TNode successor = node.getSuccessor();
        Iface successorClient;

        try {
            successorClient = clientFactory.get(successor);

        } catch (RetryFailedException e) {

            LOGGER
                    .warning("Something funny in the sockets is going down for successor :"
                            + successor);
            e.printStackTrace();
            clientFactory.release(successor);
            LOGGER.info("Lost successor: Node-" + printNode(node.getTNode()) + "Successor" + printNode(node.getSuccessor()));
            node.removeSuccessor();
            return;

        }

        try {

            x = successorClient.getPredecessor();

        } catch (NodeNotFoundException e) {
            // Successor's predecessor is null

        } catch (TException e) {
            LOGGER.warning("Successor went down");
            e.printStackTrace();
            // node.setSuccessor(node.getTNode());
            node.removeSuccessor();
            clientFactory.release(successor);
            return;

        }

        if (x != null
                && KeyIdUtil.isAfterXButBeforeEqualY(x.getNodeId(), node.getNodeId(), successor
                        .getNodeId())) {
            clientFactory.release(successor);
            successor = x;

            try {
                successorClient = clientFactory.get(successor);

            } catch (RetryFailedException e) {

                LOGGER.warning("Something funny in the sockets is going down");
                e.printStackTrace();
                clientFactory.release(successor);
                return;
            }
        }

        node.setSuccessor(successor);

        try {
            LOGGER.info(printNode(node.getTNode()) + " Notifying: " + printNode(node.getSuccessor()));
            List<TNode> successorList = successorClient.notify(node.getTNode());
            updateSuccessorList(successorList);

        } catch (TException e) {

            e.printStackTrace();
            // node.setSuccessor(node.getTNode());
            LOGGER.info("Lost successor: Node-" + printNode(node.getTNode()) + "Successor" + printNode(node.getSuccessor()));
            node.removeSuccessor();
            return;

        } finally {

            clientFactory.release(successor);

        }

    }

    public void updateSuccessorList(List<TNode> list) {
        for (int i = 0; (i < this.node.SUCCESSORLISTSIZE - 1) && (i < list.size()); i++) {
            try {
                this.node.setSuccessor(i + 1, list.get(i));
            } catch (IndexOutOfBoundsException e) {
                this.node.addSuccessor(list.get(i));
            }
        }
    }

    public String printNode(TNode n) {
        if (n == null)
            return "NULL";
        else
            return "" + n.getName();
    }

    public String printNodeList(List<TNode> l) {
        StringBuilder result = new StringBuilder("[");

        for (TNode n : l) {
            result.append(printNode(n) + ", ");
        }

        result.append("]");

        return result.toString();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                LOGGER.info(printNode(node.getTNode()) + ": Pred - "
                        + printNode(node.getPredecessor()) + "\t Succ - "
                        + printNode(node.getSuccessor()) + "\t FingerList - "
                        + printNodeList(node.getFingers()) + "\t SuccessorList - "
                        + printNodeList(node.getSuccessorList()));
                stabilize();
                fixFingers();
                checkPredecessor();
                sleep(10);
            } catch (InterruptedException e) {
                //
            }
        }
    }
}