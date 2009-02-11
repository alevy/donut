package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy, jprouty
 */
public class Node {

    public static final int       KEYSPACESIZE = 64;
    private final TNode           tNode;

    private final KeyIdComparator keyIdComparator;
    private List<Node>            fingers;
    private Node                  predecessor;

    /**
     * Create a new Chord ring
     * 
     * @param tNode
     *            The Thrift Node object that describes the physical topology of the node.
     */
    public Node(TNode tNode) {
        this.tNode = tNode;
        this.keyIdComparator = new KeyIdComparator(getNodeId());

        this.predecessor = null;
        initFingers();
    }

    /**
     * Create a new Chord ring
     * 
     * @param name
     *            The [host]name of the node (or IP)
     * @param port
     *            The port on which the node will reside
     * @param id
     *            The KeyId where this know will live in the Chord ring
     */
    public Node(String name, int port, KeyId id) {
        this(new TNode(name, port, id));
    }

    /**
     * Initializes the finger table. The successor and all fingers will become this, creating a
     * complete chord ring.
     */
    private void initFingers() {
        this.fingers = new ArrayList<Node>(KEYSPACESIZE);
        for (int i = 0; i < KEYSPACESIZE; i++)
            this.fingers.add(this);
    }

    /**
     * Join a Chord ring containing node n
     * 
     * @param toJoin
     *            An existing Chord node that is already on the ring
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
     * This method is unneeded because a node can never be positive of its predecessor at any given
     * time. FindSuccesors will always find who's responsible for the given KeyId
     * 
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

    /**
     * Get a finger entry
     * 
     * @param i
     *            The index to retrieve
     * @return fingers[i]
     */
    public Node getFinger(int i) {
        if (i < 0 && i >= fingers.size())
            // Invalid range
            throw new IndexOutOfBoundsException();

        return fingers.get(i);
    }

    /**
     * finger[i] = n
     * 
     * @param i
     *            The index to set
     * @param n
     *            The node to set
     */
    public void setFinger(int i, Node n) {
        if (i < 0 || i >= fingers.size())
            // Invalid range
            throw new IndexOutOfBoundsException();

        fingers.set(i, n);
    }

    /**
     * This method should only be used in testing!
     * 
     * @return The current list of fingers
     */
    public List<Node> getFingers() {
        return fingers;
    }

    /**
     * This method should only be used in testing!
     * 
     * @param nodes
     *            The nodes to make the new finger list
     * @return The current list of fingers
     */
    public List<Node> setFingers(Node... nodes) {
        fingers = Arrays.asList(nodes);
        return fingers;
    }

    @Override
    public boolean equals(Object obj) {
        if (Node.class.isInstance(obj)) {
            Node other = (Node) obj;
            return other.getTNode().equals(this.getTNode());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return getTNode().toString();
    }

    public TNode getTNode() {
        return tNode;
    }

    public int getPort() {
        return tNode.getPort();
    }
}
