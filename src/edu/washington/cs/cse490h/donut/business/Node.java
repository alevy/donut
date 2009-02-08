package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy
 */
public class Node {

    private final TNode                          tNode;
    
    private final KeyIdComparator                keyIdComparator;
    private final SortedSet<Node>                fingers;
    private Node                                 predecessor;
    private Node                                 successor;

    /**
     * Create a new Chord ring
     * 
     * @param tNode
     */
    public Node(TNode tNode) {
        this.tNode = tNode;
        this.keyIdComparator = new KeyIdComparator(getNodeId());
        this.fingers = new TreeSet<Node>(getKeyIdComparator().getNodeComparator());
        this.predecessor = null;
        this.successor = this;
    }
    
    public Node(String name, KeyId id) {
        this(new TNode(name, id));
    }
    
    /**
     * Join a Chord ring containing node n
     * 
     * @param toJoin
     */
    public void join(Node n) {
        this.predecessor = null;
        this.successor = n;
    }

    /**
     * Scans this Node's finger table for the closest preceding node to the given key.
     * 
     * @param entryId
     * 
     * @return the {@link Node} from the finger table that is the closest and preceding the entryId
     */
    public Node closestPrecedingNode(KeyId entryId) throws IllegalArgumentException {
        List<Node> nodes = new ArrayList<Node>(getFingers());
        
        for (int i = getFingers().size() - 1; i >= 0; --i) {
            if (getKeyIdComparator().compare(entryId, nodes.get(i).getNodeId()) >= 0) {
                return nodes.get(i);
            }
        }
        return this;
    }

    /**
     * @param id
     * @return true if this node stores the value associated with {@code id}, false otherwise.
     */
    public boolean isResponsibleFor(KeyId id) {
        if (predecessor == null) {
            throw new IllegalStateException("All valid nodes must have predecessors");
        }
        
        if (predecessor.getKeyIdComparator().compare(getNodeId(), id) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public KeyId getNodeId() {
        return tNode.getNodeId();
    }

    public String getName() {
        return tNode.getName();
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Node getPredecessor() {
        return predecessor;
    }
    
    public Node getSuccessor() {
        return successor;
    }

    public KeyIdComparator getKeyIdComparator() {
        return keyIdComparator;
    }

    public SortedSet<Node> getFingers() {
        return fingers;
    }

    public SortedSet<Node> setFingers(Node... nodes) {
        fingers.clear();
        for (Node node : nodes) {
            fingers.add(node);
        }
        return fingers;
    }
    
    public TNode getTNode() {
        return tNode;
    }
}
