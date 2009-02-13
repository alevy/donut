package edu.washington.cs.cse490h.donut.server;

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
        LOGGER.info("Joining Donut...");
        try {
            if (!n.equals(node.getTNode())) {
                TNode found = clientFactory.get(n).findSuccessor(node.getNodeId());
                node.setSuccessor(found);
                clientFactory.release(n);
            }
        } catch (RetryFailedException e) {
            throw new TException(e);
        }
        LOGGER.info("Joined Donut!");
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

        nextFingerToUpdate = (nextFingerToUpdate + 1) % node.getFingersSize();
    }

    public void fixFinger(int finger) {
        if (node.getSuccessor().equals(node.getTNode())) {
            return;
        }
        TNode updatedFinger;
        try {
            Iface iface;
            try {
                iface = clientFactory.get(node.getTNode());
            } catch (RetryFailedException e) {
                e.printStackTrace();
                return;
            }
            long id = node.getNodeId().getId() + 1 << finger;
            KeyId keyId = new KeyId(id);
            updatedFinger = iface.findSuccessor(keyId);
            this.node.setFinger(finger, updatedFinger);
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            clientFactory.release(node.getTNode());
        }
    }

    /**
     * Called periodically. Verify's immediate successor, and tell's successor about us.
     */
    public void stabilize() {

        TNode x = null;
        TNode successor = node.getSuccessor();
        if (!node.getTNode().equals(successor)) {
            try {
                Iface successorClient = clientFactory.get(successor);
                try {
                    x = successorClient.getPredecessor();
                    if (KeyIdUtil.isAfterXButBeforeEqualY(x.getNodeId(), node.getNodeId(), successor.getNodeId())) {
                        node.setSuccessor(x);
                    }
                } catch (NodeNotFoundException e) {
                    // Successor's predecessor is null
                    x = null;
                }
                successorClient.notify(node.getTNode());
            } catch (TException e) {
                e.printStackTrace();
                node.setSuccessor(node.getTNode());
                return;
            } catch (RetryFailedException e) {
                e.printStackTrace();
                node.setSuccessor(node.getTNode());
                return;
            } finally {
                clientFactory.release(successor);
            }
        } else {
            x = node.getPredecessor();
            if (x != null) {
                node.setSuccessor(x);
            }
        }

    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                stabilize();
                fixFingers();
                checkPredecessor();
                sleep(100);
            } catch (InterruptedException e) {
                //
            }
        }
    }
}