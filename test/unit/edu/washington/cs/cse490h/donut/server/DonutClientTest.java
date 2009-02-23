package edu.washington.cs.cse490h.donut.server;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import org.apache.thrift.TException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.service.LocatorClientFactory;
import edu.washington.cs.cse490h.donut.service.RetryFailedException;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.NodeNotFoundException;
import edu.washington.edu.cs.cse490h.donut.service.TNode;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class DonutClientTest {

    private LocatorClientFactory clientLocatorMock;
    private Iface                keyLocator;

    @Before
    public void setUp() {
        clientLocatorMock = EasyMock.createMock(LocatorClientFactory.class);
        keyLocator = EasyMock.createMock(KeyLocator.Iface.class);
    }        
    
    @After
    public void tearDown() {
        verify(clientLocatorMock, keyLocator);
    }

    @Test
    public void testJoinTNode() throws Exception {
        Node testNode = new Node("testNode0", 8080, new KeyId(100));
        TNode knownNode = new TNode("testNode1", 8080, new KeyId(200));
        TNode successorNode = new TNode("successor", 8080, new KeyId(300));

        DonutClient donutClient = new DonutClient(testNode, clientLocatorMock);

        expect(clientLocatorMock.get(knownNode)).andReturn(keyLocator);
        clientLocatorMock.release(knownNode);
        expect(keyLocator.findSuccessor(testNode.getNodeId())).andReturn(successorNode);
        replay(clientLocatorMock, keyLocator);

        donutClient.join(knownNode);

        assertSame(testNode.getSuccessor(), successorNode);
    }

    @Test
    public void testPing_True() throws Exception {
        DonutClient donutClient = new DonutClient(null, clientLocatorMock);

        TNode node = new TNode();

        expect(clientLocatorMock.get(node)).andReturn(keyLocator);
        clientLocatorMock.release(node);
        keyLocator.ping();
        replay(clientLocatorMock);
        replay(keyLocator);

        assertTrue(donutClient.ping(node));
    }

    @Test
    public void testPing_False() throws Exception {
        DonutClient donutClient = new DonutClient(null, clientLocatorMock);

        TNode node = new TNode();

        expect(clientLocatorMock.get(node)).andReturn(keyLocator);
        clientLocatorMock.release(node);
        keyLocator.ping();
        EasyMock.expectLastCall().andThrow(new TException());
        replay(clientLocatorMock, keyLocator);

        assertFalse(donutClient.ping(node));
    }

    @Test
    public void testCheckPredecessor_StillUp() throws Exception {
        Node node = new Node(null, 0, null);
        TNode predecessor = new TNode("pred", 8080, null);
        node.setPredecessor(predecessor);
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(predecessor)).andReturn(keyLocator);
        clientLocatorMock.release(predecessor);
        keyLocator.ping();
        replay(clientLocatorMock, keyLocator);

        donutClient.checkPredecessor();
        assertEquals(predecessor, node.getPredecessor());
    }

    @Test
    public void testCheckPredecessor_Down() throws Exception {
        Node node = new Node(null, 0, null);
        TNode predecessor = new TNode("pred", 8080, null);
        node.setPredecessor(predecessor);
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(predecessor)).andReturn(keyLocator);
        clientLocatorMock.release(predecessor);
        keyLocator.ping();
        expectLastCall().andThrow(new TException());
        replay(clientLocatorMock, keyLocator);

        donutClient.checkPredecessor();
        assertNull(node.getPredecessor());
    }

    @Test
    public void testStabilize_Alone() throws Exception {
        Node node = new Node(null, 0, null);
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);
        
        expect(clientLocatorMock.get(node.getTNode())).andReturn(keyLocator);
        clientLocatorMock.release(node.getTNode());
        keyLocator.getPredecessor();
        expectLastCall().andThrow(new NodeNotFoundException());
        keyLocator.notify(node.getTNode());

        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(node.getTNode(), node.getSuccessor());
    }
    
    @Test
    public void testStabilize_HasPredecessor() throws Exception {
        Node node = new Node("testNode0", 8080, null);
        TNode predecessor = new TNode("testNode1", 0, null);
        node.setPredecessor(predecessor);
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);
        
        expect(clientLocatorMock.get(node.getTNode())).andReturn(keyLocator);
        clientLocatorMock.release(node.getTNode());
        expect(clientLocatorMock.get(predecessor)).andReturn(keyLocator);
        clientLocatorMock.release(predecessor);
        expect(keyLocator.getPredecessor()).andReturn(predecessor);
        keyLocator.notify(node.getTNode());
        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(node.getPredecessor(), node.getSuccessor());
    }
    
    @Test
    public void testStabilize_JustNotifyBecauseNoPredecessor() throws Exception {
        Node node = new Node("self", 0, null);
        TNode successor = new TNode("other", 0, null);
        node.setSuccessor(successor);
        
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(successor)).andReturn(keyLocator);
        clientLocatorMock.release(successor);
        expect(keyLocator.getPredecessor()).andThrow(new NodeNotFoundException());
        keyLocator.notify(node.getTNode());
        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(successor, node.getSuccessor());
    }
    
    @Test
    public void testStabilize_JustNotifyBecauseNotBetween() throws Exception {
        Node node = new Node("self", 0, new KeyId(0));
        TNode between = new TNode("between", 0, new KeyId(300));
        TNode successor = new TNode("other", 0, new KeyId(200));
        node.setSuccessor(successor);
        
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(successor)).andReturn(keyLocator);
        clientLocatorMock.release(successor);
        expect(keyLocator.getPredecessor()).andReturn(between);
        keyLocator.notify(node.getTNode());
        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(successor, node.getSuccessor());
    }
    
    @Test
    public void testStabilize_NotifyAndSetSuccessor() throws Exception {
        Node node = new Node("self", 0, new KeyId(0));
        TNode between = new TNode("between", 0, new KeyId(100));
        TNode successor = new TNode("other", 0, new KeyId(200));
        node.setSuccessor(successor);
        
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(successor)).andReturn(keyLocator);
        clientLocatorMock.release(successor);
        expect(clientLocatorMock.get(between)).andReturn(keyLocator);
        clientLocatorMock.release(between);
        expect(keyLocator.getPredecessor()).andReturn(between);
        keyLocator.notify(node.getTNode());
        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(between, node.getSuccessor());
    }
    
    @Test
    public void testStabilize_NodeThrowsRetryFailedException() throws Exception {
        Node node = new Node("self", 0, new KeyId(0));
        TNode successor = new TNode("other", 0, new KeyId(200));
        node.setSuccessor(successor);
        
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);
        
        expect(clientLocatorMock.get(successor)).andThrow(new RetryFailedException());
        clientLocatorMock.release(successor);
        replay(clientLocatorMock, keyLocator);
        
        donutClient.stabilize();
        assertSame(node.getTNode(), node.getSuccessor());
    }
    
    @Test
    public void testStabilize_NodeThrowsTException() throws Exception {
        Node node = new Node("self", 0, new KeyId(0));
        TNode successor = new TNode("other", 0, new KeyId(200));
        node.setSuccessor(successor);
        
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);

        expect(clientLocatorMock.get(successor)).andReturn(keyLocator);
        clientLocatorMock.release(successor);
        expect(keyLocator.getPredecessor()).andThrow(new TException());
        replay(clientLocatorMock, keyLocator);

        donutClient.stabilize();
        assertSame(node.getTNode(), node.getSuccessor());
    }
    

    @Test
    public void testFixFingers() throws Exception {
        Node node = new Node(null, 0, new KeyId(0));
        TNode finger1 = new TNode();
        node.setSuccessor(new TNode("hello", 0, null));
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);
        
        expect(clientLocatorMock.get(node.getTNode())).andReturn(keyLocator);
        clientLocatorMock.release(node.getTNode());
        expect(keyLocator.findSuccessor(new KeyId(1))).andReturn(finger1);
        replay(clientLocatorMock, keyLocator);
        
        donutClient.fixFinger(0);
        
        assertSame(finger1, node.getFinger(0));
    }
    
    @Test
    public void testFixFingers_Far() throws Exception {
        Node node = new Node(null, 0, new KeyId(0));
        TNode finger10 = new TNode();
        node.setSuccessor(new TNode("hello", 0, null));
        DonutClient donutClient = new DonutClient(node, clientLocatorMock);
        
        expect(clientLocatorMock.get(node.getTNode())).andReturn(keyLocator);
        clientLocatorMock.release(node.getTNode());
        expect(keyLocator.findSuccessor(new KeyId(1024))).andReturn(finger10);
        replay(clientLocatorMock, keyLocator);
        
        donutClient.fixFinger(10);
        
        assertSame(finger10, node.getFinger(10));
    }

}
