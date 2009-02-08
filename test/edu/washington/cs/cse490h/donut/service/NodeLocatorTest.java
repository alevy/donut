package edu.washington.cs.cse490h.donut.service;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;


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
        Node node1 = new Node("node1", new KeyId(100));
        Node node2 = new Node("node2", new KeyId(900));
        
        node1.setFingers(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, null);
        
        assertEquals(node2.getName(), nodeLocator.findSuccessor(new KeyId(456)));
    }
    
    @Test
    public void testFindSuccessor_NotImmediateSuccessor() throws Exception {
        KeyId entryId = new KeyId(1024);

        Node node1 = new Node("node1", new KeyId(100));
        Node node2 = new Node("node2", new KeyId(900));
        
        String resultNodeName = "resultNode";
        
        node1.setFingers(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, clientFactoryMock);
        
        // ClientFactory Expectations:
        expect(clientFactoryMock.get(node2.getName())).andReturn(nextLocatorMock);
        replay(clientFactoryMock);
        
        // NextLocatorMock Expectations:
        expect(nextLocatorMock.findSuccessor(entryId)).andReturn(resultNodeName);
        replay(nextLocatorMock);
        
        assertEquals(resultNodeName, nodeLocator.findSuccessor(entryId));
    }
}
