package edu.washington.cs.cse490h.donut.business;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class NodeTest {

    @Test
    public void testClosestPrecedingNode_InList() throws Exception {
        Node initialNode = new Node("testNode0", new KeyId(54));
        Node finger1 = new Node("testNode1", new KeyId(100));
        Node finger2 = new Node("testNode2", new KeyId(150));
        Node finger3 = new Node("testNode3", new KeyId(200));
        
        initialNode.setFingers(finger1, finger2, finger3);
        
        KeyId entryId = new KeyId(125);
        Node nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1.getName(), nextHop.getName());
    }
    
    @Test
    public void testClosestPrecedingNode_EqualToNode() throws Exception {
        Node initialNode = new Node("testNode0", new KeyId(54));
        Node finger1 = new Node("testNode1", new KeyId(100));
        Node finger2 = new Node("testNode2", new KeyId(150));
        Node finger3 = new Node("testNode3", new KeyId(200));
        
        initialNode.setFingers(finger1, finger2, finger3);
        
        KeyId entryId = new KeyId(200);
        Node nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger3.getName(), nextHop.getName());
    }
    
    @Test
    public void testClosestPrecedingNode_Circle() throws Exception {
        Node initialNode = new Node("testNode0", new KeyId(175));
        Node finger1 = new Node("testNode1", new KeyId(100));
        Node finger2 = new Node("testNode2", new KeyId(150));
        Node finger3 = new Node("testNode3", new KeyId(200));
        
        initialNode.setFingers(finger1, finger2, finger3);
        
        KeyId entryId = new KeyId(125);
        Node nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1, nextHop);
    }
    
    @Test
    public void testClosestPrecedingNode_ItsYou() throws Exception {
        Node initialNode = new Node("testNode0", new KeyId(75));
        Node finger1 = new Node("testNode1", new KeyId(100));
        Node finger2 = new Node("testNode2", new KeyId(150));
        Node finger3 = new Node("testNode3", new KeyId(200));
        
        initialNode.setFingers(finger1, finger2, finger3);
        
        KeyId entryId = new KeyId(80);
        Node nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(initialNode.getName(), nextHop.getName());
    }

    @Test(expected=IllegalStateException.class)
    public void testResponsibleFor_NoPredecessor() {
        new Node("testNode", new KeyId(14)).isResponsibleFor(new KeyId());
    }
    
    @Test
    public void testResponsibleFor_True() {
        Node node = new Node("testNode4", new KeyId(14));
        node.setPredecessor(new Node("testNode0", new KeyId(0)));
        assertEquals(true, node.isResponsibleFor(new KeyId(7)));
    }
    
    @Test
    public void testResponsibleFor_False() {
        Node node = new Node("testNode4", new KeyId(14));
        node.setPredecessor(new Node("testNode0", new KeyId(0)));
        assertEquals(false, node.isResponsibleFor(new KeyId(16)));
    }
    
    @Test
    public void testJoin_SuccessorIsSet() throws Exception {
        Node testNode0 = new Node("testNode0", new KeyId(100));
        Node testNode1 = new Node("testNode1", new KeyId(1000));
        testNode0.join(testNode1);
        assertSame(testNode0.getSuccessor(), testNode1);
    }
}
