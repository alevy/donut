package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Node>                     fingers = new ArrayList<Node>();
    private Node                                 predecessor;

    @Inject
    public Node(@Named("NodeName") String name, @Named("NodeId") KeyId nodeId) {
        this.name = name;
        this.nodeId = nodeId;
        this.keyIdComparator = new KeyIdComparator(nodeId);
    }

    /**
     * @param entryId
     * @return the next {@link Node} in the search tree.
     */
    public Node closestPreceedingNode(KeyId entryId) throws IllegalArgumentException {
        for (int i = 0; i < getFingers().size() - 1; ++i) {
            if (getKeyIdComparator().compare(entryId, getFingers().get(i).getNodeId()) <= 0) {
                return getFingers().get(i);
            }
        }
        return getFingers().get(getFingers().size() - 1);
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

    public List<Node> getFingers() {
        return fingers;
    }

    public List<Node> setFingers(Node... nodes) {
        fingers.clear();
        for (Node node : nodes) {
            fingers.add(node);
        }
        return fingers;
    }
}
