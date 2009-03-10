package edu.washington.cs.cse490h.donut.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.DonutData;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.TNode;


public class NodeLocatorTest {
    
    LocatorClientFactory clientFactoryMock;
    KeyLocator.Iface nextLocatorMock;
    Node node;

    @Before
    public void setUp() throws Exception {
        clientFactoryMock = createMock(LocatorClientFactory.class);
        nextLocatorMock = createMock(KeyLocator.Iface.class);
    }
    
    @Test
    public void testFindSuccessor_ImmediateSuccessor() throws Exception {
        Node node1 = new Node("node1", 8080, new KeyId(100));
        TNode node2 = new TNode("node2", 8080, new KeyId(900));
        
        node1.setSuccessor(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, null);
        
        assertEquals(node2, nodeLocator.findSuccessor(new KeyId(456)));
    }
    
    @Test
    public void testFindSuccessor_NotImmediateSuccessor() throws Exception {
        KeyId entryId = new KeyId(1024);

        Node node1 = new Node("node1", 8080, new KeyId(100));
        TNode node2 = new TNode("node2", 8080, new KeyId(900));
        
        TNode resultNode = new TNode("resultNode", 8080, null);
        
        node1.setSuccessor(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, clientFactoryMock);
        
        // ClientFactory Expectations:
        expect(clientFactoryMock.get(node2)).andReturn(nextLocatorMock);
        clientFactoryMock.release(node2);
        replay(clientFactoryMock);
        
        // NextLocatorMock Expectations:
        expect(nextLocatorMock.findSuccessor(entryId)).andReturn(resultNode);
        replay(nextLocatorMock);
        
        assertEquals(resultNode, nodeLocator.findSuccessor(entryId));
    }
    
    @Test
    public void testGet_Dne() throws Exception {
        Node node = new Node(new TNode());
        NodeLocator nodeLocator = new NodeLocator(node, null);
        nodeLocator.getDataMap().clear();
        assertEquals(new DonutData(false, null), nodeLocator.get(new KeyId(1)));
    }
    
    @Test
    public void testGet_Exists() throws Exception {
        Node node = new Node(new TNode());
        NodeLocator nodeLocator = new NodeLocator(node, null);
        String value = "Hello World";
        nodeLocator.getDataMap().put(new KeyId(1), value.getBytes());
        assertEquals(new DonutData(true, value.getBytes()), nodeLocator.get(new KeyId(1)));
    }
    
    @Test
    public void testPut() throws Exception {
        Node node = new Node(new TNode());
        NodeLocator nodeLocator = new NodeLocator(node, null);
        String value = "Hello World";
        nodeLocator.put(new KeyId(1), new DonutData(true, value.getBytes()));
        assertArrayEquals(value.getBytes(), nodeLocator.getDataMap().get(new KeyId(1)));
    }
    
    @Test
    public void testPut_Null() throws Exception {
        Node node = new Node(new TNode());
        NodeLocator nodeLocator = new NodeLocator(node, null);
        nodeLocator.put(new KeyId(1), new DonutData(false, null));
        assertNull(nodeLocator.getDataMap().get(new KeyId(1)));
    }
}
