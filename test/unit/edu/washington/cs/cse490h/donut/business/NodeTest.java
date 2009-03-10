package edu.washington.cs.cse490h.donut.business;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.service.thrift.TNode;

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
        assertEquals(initialNode.getTNode(), nextHop);
    }

    @Test
    public void testClosestPrecedingNode_InList() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(54));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));

        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(1, finger2.getTNode());
        initialNode.setFinger(2, finger3.getTNode());

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
        initialNode.setFinger(1, finger2.getTNode());
        initialNode.setFinger(2, finger3.getTNode());

        KeyId entryId = new KeyId(200);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger2.getTNode(), nextHop);
    }

    @Test
    public void testClosestPrecedingNode_Circle() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(50));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));

        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(1, finger2.getTNode());
        initialNode.setFinger(2, finger3.getTNode());

        KeyId entryId = new KeyId(125);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(finger1.getTNode(), nextHop);
    }

    @Test
    public void testClosestPrecedingNode_ItsYou() throws Exception {
        Node initialNode = new Node("testNode0", 8080, new KeyId(75));
        Node finger1 = new Node("testNode1", 8080, new KeyId(100));
        Node finger2 = new Node("testNode2", 8080, new KeyId(150));
        Node finger3 = new Node("testNode3", 8080, new KeyId(200));

        initialNode.setFinger(0, finger1.getTNode());
        initialNode.setFinger(1, finger2.getTNode());
        initialNode.setFinger(2, finger3.getTNode());

        KeyId entryId = new KeyId(80);
        TNode nextHop = initialNode.closestPrecedingNode(entryId);
        assertEquals(initialNode.getName(), nextHop.getName());
    }

    @Test
    public void testSetGetFingers() throws Exception {
        Node node1 = new Node("testNode1", 8080, new KeyId(50));
        Node node2 = new Node("testNode2", 8080, new KeyId(60));

        node1.setFinger(0, node2.getTNode());

        assertEquals(node1.getSuccessor(), node2.getTNode());
        assertEquals(node1.getSuccessor(), node1.getFinger(0));

    }

    @Test
    public void testTNodeToString() throws Exception {
        TNode node1 = new TNode("test.yo.com", 880, new KeyId(0));
        TNode node2 = new TNode("localhost", 8081, new KeyId(10000));
        TNode node3 = new TNode("helloworld.com", 80800, new KeyId(-4543450));
        TNode node4 = new TNode("attu.cs.washington.edu", 810, new KeyId(679451));
        TNode node5 = new TNode("bigpimin", 80, new KeyId(-12356478));

        assertEquals("test.yo.com:880", Node.TNodeToString(node1));
        assertEquals("localhost:8081", Node.TNodeToString(node2));
        assertEquals("helloworld.com:80800", Node.TNodeToString(node3));
        assertEquals("attu.cs.washington.edu:810", Node.TNodeToString(node4));
        assertEquals("bigpimin:80", Node.TNodeToString(node5));
        assertEquals("NULL", Node.TNodeToString(null));
    }

    @Test
    public void testTNodeListToString() throws Exception {
        List<TNode> list = new ArrayList<TNode>();
        list.add(new TNode("test.yo.com", 880, new KeyId(0)));
        list.add(new TNode("localhost", 8081, new KeyId(10000)));
        list.add(new TNode("helloworld.com", 80800, new KeyId(-4543450)));
        list.add(new TNode("attu.cs.washington.edu", 810, new KeyId(679451)));
        list.add(new TNode("bigpimin", 80, new KeyId(-12356478)));
        list.add(null);

        String expected = "[test.yo.com:880, localhost:8081, helloworld.com:80800, attu.cs.washington.edu:810, bigpimin:80, NULL]";

        assertEquals(expected, Node.TNodeListToString(list));
    }
}
