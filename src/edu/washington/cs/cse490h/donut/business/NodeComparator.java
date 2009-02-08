package edu.washington.cs.cse490h.donut.business;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    
    private final KeyIdComparator comparator;

    public NodeComparator(KeyIdComparator comparator) {
        this.comparator = comparator;
        
    }
    
    public int compare(Node node1, Node node2) {
        return comparator.compare(node1.getNodeId(), node2.getNodeId());
    }

}