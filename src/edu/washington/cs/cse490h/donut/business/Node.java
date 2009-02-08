package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy
 */
public class Node {

    private final TNode                          tNode;
    
    private final KeyIdComparator                keyIdComparator;
    private List<Node>                     fingers;
    private Node                                 predecessor;

    /**
     * Create a new Chord ring
     * 
     * @param tNode
     */
    public Node(TNode tNode) {
        this.tNode = tNode;
        this.keyIdComparator = new KeyIdComparator(getNodeId());
        // Todo (jprouty): Add a constant somewhere that specifies the current size of our keyspace.
        this.fingers = new ArrayList<Node>(64);
        this.predecessor = null;
        // Set the current successor to this
        this.fingers.add(0, this);
    }

    public Node(String name, int port, KeyId id) {
        this(new TNode(name, port, id));
    }

    /**
     * Join a Chord ring containing node n
     * 
     * @param toJoin
     */
    public void join(Node n) {
        this.predecessor = null;
        this.fingers.set(0, n);
    }

    /**
     * Scans this Node's finger table for the closest preceding node to the given key.
     * 
     * @param entryId
     * @return the {@link Node} from the finger table that is the closest and preceding the entryId
     */
    public Node closestPrecedingNode(KeyId entryId) throws IllegalArgumentException {
        for (int i = getFingers().size() - 1; i >= 0; --i) {
            if (getKeyIdComparator().compare(entryId, fingers.get(i).getNodeId()) >= 0) {
                return fingers.get(i);
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
        return fingers.get(0);
    }

    public KeyIdComparator getKeyIdComparator() {
        return keyIdComparator;
    }

    public List<Node> getFingers() {
        return fingers;
    }

    public List<Node> setFingers(Node... nodes) {
        fingers = Arrays.asList(nodes);
        return fingers;
    }

    @Override
    public boolean equals(Object obj) {
        if (Node.class.isInstance(obj)) {
            Node other = (Node) obj;
            return other.getTNode() == this.getTNode();
        }
        return false;
    }

    public TNode getTNode() {
        return tNode;
    }

    public int getPort() {
        return tNode.getPort();
    }
}
