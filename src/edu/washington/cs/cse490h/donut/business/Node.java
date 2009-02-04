package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class Node {

    private final KeyId                          nodeId;
    private final String                         name;
    
    private final KeyIdComparator                keyIdComparator;
    private final SortedSet<Node>                fingers;
    private Node                                 predecessor;

    @Inject
    public Node(@Named("NodeName") String name, @Named("NodeId") KeyId nodeId) {
        this.name = name;
        this.nodeId = nodeId;
        this.keyIdComparator = new KeyIdComparator(nodeId);
        this.fingers = new TreeSet<Node>(getKeyIdComparator().getNodeComparator());
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
        
        if (predecessor.getKeyIdComparator().compare(nodeId, id) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public KeyId getNodeId() {
        return nodeId;
    }

    public String getName() {
        return name;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Node getPredecessor() {
        return predecessor;
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
}
