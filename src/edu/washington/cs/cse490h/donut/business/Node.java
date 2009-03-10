package edu.washington.cs.cse490h.donut.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.washington.cs.cse490h.donut.service.thrift.Constants;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.service.thrift.TNode;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy, jprouty
 */
public class Node {
    private final TNode tNode;

    private List<TNode> fingers;
    private List<TNode> successorList;
    private TNode       predecessor;

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
        initSuccessorList();
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
     * Initializes the successor list.
     */
    private void initSuccessorList() {
        this.successorList = new ArrayList<TNode>(Constants.SUCCESSOR_LIST_SIZE);

        // Adds the initial successor
        for (int i = 0; i < Constants.SUCCESSOR_LIST_SIZE; i++)
            this.successorList.add(tNode);
    }

    /**
     * Initializes the finger table. The successor and all fingers will become this, creating a
     * complete chord ring.
     */
    private void initFingers() {
        this.fingers = new ArrayList<TNode>(Constants.KEY_SPACE);
        for (int i = 0; i < Constants.KEY_SPACE; i++)
            this.fingers.add(tNode);
    }

    /**
     * Scans this Node's finger table for the closest preceding node to the given key.
     * 
     * @param entryId
     * @return the {@link Node} from the finger table that is the closest and preceding the entryId
     */
    public TNode closestPrecedingNode(KeyId entryId) throws IllegalArgumentException {
        for (int i = fingers.size() - 1; i >= 0; --i) {
            KeyId currentFinger = getFinger(i).getNodeId();
            // (id, finger, us)
            // (finger, us, id)
            if (!currentFinger.equals(getNodeId())
                    && KeyIdUtil.isAfterXButBeforeEqualY(entryId, currentFinger, getNodeId())) {
                return getFinger(i);
            }
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
        return successorList.get(0);
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
        if (i == 0)
            return getSuccessor();

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

        if (i == 0) {
            setSuccessor(n);
            return;
        }

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

    /**
     * Formats a TNode into a prettier String. We cannot change the toString of TNode because it's
     * implementation is generated each time with a thrift call. Therefore, this method subverts
     * their efforts.
     * 
     * @param n
     *            The node to print
     * @return Returns a String in the format of hostname:port. If n == null, then returns "NULL".
     */
    public static String TNodeToString(TNode n) {
        if (n == null)
            return "NULL";
        else
            return n.getName() + ":" + n.getPort();
    }

    /**
     * Formats a list of TNodes. Note: This implementation is based off Java 6 AbstractCollection
     * toString.
     * 
     * @param l
     *            List of TNodes
     * @return Produced String
     */
    public static String TNodeListToString(List<TNode> l) {
        Iterator<TNode> i = l.iterator();
        if (!i.hasNext())
            // Empty list
            return "[]";

        StringBuilder result = new StringBuilder();

        result.append("[");

        while (true) {
            TNode e = i.next();
            result.append(TNodeToString(e));
            if (!i.hasNext())
                return result.append(']').toString();

            result.append(", ");
        }
    }

    public void updateSuccessorList(List<TNode> list) {
        int i;
        for (i = 0; (i < Constants.SUCCESSOR_LIST_SIZE - 1) && (i < list.size()); i++) {
            try {
                setSuccessor(i + 1, list.get(i));
            } catch (IndexOutOfBoundsException e) {
                addSuccessor(list.get(i));
            }
        }

        // Remove any leftover successors
        i++;
        while (i < getSuccessorList().size()) {
            removeSuccessor(i);
        }
    }

    @Override
    public String toString() {
        return TNodeToString(this.tNode);
    }

    public TNode getTNode() {
        return tNode;
    }

    public int getPort() {
        return tNode.getPort();
    }

    public void setSuccessor(TNode node) {
        this.successorList.set(0, node);
    }

    public List<TNode> getFingers() {
        return new ArrayList<TNode>(fingers);
    }

    public List<TNode> getSuccessorList() {
        return new ArrayList<TNode>(successorList);
    }

    public void setSuccessor(int i, TNode node) {
        this.successorList.set(i, node);
    }

    public void addSuccessor(TNode node) {
        this.successorList.add(node);
    }

    public void removeSuccessor() {
        this.successorList.remove(0);
    }

    public void removeSuccessor(int i) {
        this.successorList.remove(i);
    }
}
