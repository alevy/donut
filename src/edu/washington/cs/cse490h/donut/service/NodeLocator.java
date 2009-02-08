package edu.washington.cs.cse490h.donut.service;

import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class NodeLocator implements Iface {

    private static Logger                LOGGER = Logger.getLogger(NodeLocator.class.getName());
    private final Node                   node;
    private final LocatorClientFactory   clientFactory;

    @Inject
    public NodeLocator(Node node,
            LocatorClientFactory clientFactory) {
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
            return clientFactory.get(next.getName()).findSuccessor(entryId);
        } catch (ConnectionFailedException e) {
            throw new TException();
        }
    }
    
    public void join(Node n) throws TException {
        try {
            TNode found = clientFactory.get(n.getName()).findSuccessor(this.node.getNodeId());
            this.node.join(new Node(found));
        } catch (ConnectionFailedException e) {
            throw new TException();
        }
    }
}
