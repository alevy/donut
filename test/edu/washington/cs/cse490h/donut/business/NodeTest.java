package edu.washington.cs.cse490h.donut.business;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy
 */
public class NodeTest {

    @Test
    public void testClosestPrecedingNode_Simple() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(100));
        Node finger1 = new Node("testNode1", 8080, new KeyId(3000));
        
        initialNode.setSuccessor(finger1.getTNode());
        
        KeyId entryId = new KeyId(99);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1.getName(), nextHop.getName());
        
        entryId = new KeyId(2000);
        nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(initialNode, nextHop);
    }
    
    @Test
    public void testClosestPrecedingNode_InList() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(54));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));
        
        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(0, finger2.getTNode());
        initialNode.setFinger(0, finger3.getTNode());
        
        KeyId entryId = new KeyId(125);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1.getName(), nextHop.getName());
    }
    
    @Test
    public void testClosestPrecedingNode_EqualToNode() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(54));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));
        
        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(0, finger2.getTNode());
        initialNode.setFinger(0, finger3.getTNode());
        
        KeyId entryId = new KeyId(200);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger2, nextHop);
    }
    
    @Test
    public void testClosestPrecedingNode_Circle() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(50));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));
        
        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(0, finger2.getTNode());
        initialNode.setFinger(0, finger3.getTNode());
        
        KeyId entryId = new KeyId(125);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1, nextHop);
    }
    
    @Test
    public void testClosestPrecedingNode_ItsYou() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(75));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));
        
        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(0, finger2.getTNode());
        initialNode.setFinger(0, finger3.getTNode());
        
        KeyId entryId = new KeyId(80);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(initialNode.getName(), nextHop.getName());
    }

    @Test
    public void testJoin_SuccessorIsSet() throws Exception {
        Node testNode0 = new Node("testNode0", 8080, new KeyId(100));
        Node testNode1 = new Node("testNode1", 8080, new KeyId(1000));
        testNode0.join(testNode1.getTNode());
        assertSame(testNode0.getSuccessor(), testNode1);
    }
}
