package edu.washington.cs.cse490h.donut.business;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    
    private NodeComparatorData data = new NodeComparatorData();

    public NodeComparator(KeyIdComparator comp) {
        this.data.comp = comp;
        
    }
    
    public int compare(Node node1, Node node2) {
        return data.comp.compare(node1.getNodeId(), node2.getNodeId());
    }

}