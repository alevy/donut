package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.cse490h.donut.util.KeyIdUtil;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy, jprouty
 */
public class Node {

    public static final int       KEYSPACESIZE = 64;
    private final TNode           tNode;

    private List<TNode>            fingers;
    private TNode                  predecessor;

    /**
     * Create a new Chord ring
     * 
     * @param tNode
     *            The Thrift Node object that describes the physical topology of the node.
     */
    public Node(TNode tNode) {
        this.tNode = tNode;
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
        this.fingers = new ArrayList<TNode>(KEYSPACESIZE - 1);
        for (int i = 0; i < KEYSPACESIZE - 1; i++)
            this.fingers.add(tNode);
    }

    /**
     * Scans this Node's finger table for the closest preceding node to the given key.
     * 
     * @param entryId
     * @return the {@link Node} from the finger table that is the closest and preceding the entryId
     */
    public TNode closestPrecedingNode(KeyId entryId) throws IllegalArgumentException {
        long id = entryId.getId();

        long prevId = getNodeId().getId();
        for (int i = fingers.size() - 1; i >= 0; --i) {
            long currentId = fingers.get(i).getNodeId().getId();
            if (KeyIdUtil.isAfterXButBeforeOrEqualY(id, currentId, prevId)) {
                return fingers.get(i);
            }
            prevId = currentId;
        }
        return getTNode();
    }

    public KeyId getNodeId() {
        return tNode.getNodeId();
    }

    public String getName() {
        return tNode.getName();
    }

    public void setPredecessor(TNode predecessor) {
        this.predecessor = predecessor;
    }

    public TNode getPredecessor() {
        return predecessor;
    }

    public TNode getSuccessor() {
        return fingers.get(0);
    }

    /**
     * Get a finger entry
     * 
     * @param i
     *            The index to retrieve
     * @return fingers[i]
     */
    public TNode getFinger(int i) {
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
    public void setFinger(int i, TNode n) {
        if (i < 0 || i >= fingers.size())
            // Invalid range
            throw new IndexOutOfBoundsException();

        fingers.set(i, n);
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
        return tNode.toString();
    }

    public TNode getTNode() {
        return tNode;
    }

    public int getPort() {
        return tNode.getPort();
    }

    public void setSuccessor(TNode node) {
        this.fingers.set(0, node);
        
    }

    public int getFingersSize() {
        return fingers.size();
    }
}
