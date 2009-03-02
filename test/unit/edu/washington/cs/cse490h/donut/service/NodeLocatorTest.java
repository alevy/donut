package edu.washington.cs.cse490h.donut.service;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.business.Pair;
import edu.washington.cs.cse490h.donut.service.application.DonutHashTableService;
import edu.washington.edu.cs.cse490h.donut.service.DataNotFoundException;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class NodeLocatorTest {

    LocatorClientFactory  clientFactoryMock;
    KeyLocator.Iface      nextLocatorMock;
    DonutHashTableService service;

    @Before
    public void setUp() throws Exception {
        clientFactoryMock = createMock(LocatorClientFactory.class);
        nextLocatorMock = createMock(KeyLocator.Iface.class);
        service = createMock(DonutHashTableService.class);
    }

    @After
    public void tearDown() throws Exception {
        verify(clientFactoryMock, nextLocatorMock, service);
    }

    @Test
    public void testFindSuccessor_ImmediateSuccessor() throws Exception {
        Node node1 = new Node("node1", 8080, new KeyId(100));
        TNode node2 = new TNode("node2", 8080, new KeyId(900));

        node1.setSuccessor(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, null, null);

        replay(clientFactoryMock, nextLocatorMock, service);

        assertEquals(node2, nodeLocator.findSuccessor(new KeyId(456)));

    }

    @Test
    public void testFindSuccessor_NotImmediateSuccessor() throws Exception {
        KeyId entryId = new KeyId(1024);

        Node node1 = new Node("node1", 8080, new KeyId(100));
        TNode node2 = new TNode("node2", 8080, new KeyId(900));

        TNode resultNode = new TNode("resultNode", 8080, null);

        node1.setSuccessor(node2);
        NodeLocator nodeLocator = new NodeLocator(node1, null, clientFactoryMock);

        // ClientFactory Expectations:
        expect(clientFactoryMock.get(node2)).andReturn(nextLocatorMock);
        clientFactoryMock.release(node2);

        // NextLocatorMock Expectations:
        expect(nextLocatorMock.findSuccessor(entryId)).andReturn(resultNode);
        replay(clientFactoryMock, nextLocatorMock, service);

        assertEquals(resultNode, nodeLocator.findSuccessor(entryId));
    }

    @Test(expected = DataNotFoundException.class)
    public void testGet_Dne() throws Exception {
        NodeLocator nodeLocator = new NodeLocator(null, service, null);

        expect(service.get(new KeyId(1))).andReturn(null);
        replay(clientFactoryMock, nextLocatorMock, service);

        nodeLocator.get(new KeyId(1));
    }

    @Test
    public void testGet_Exists() throws Exception {
        NodeLocator nodeLocator = new NodeLocator(null, service, null);
        String value = "Hello World";

        expect(service.get(new KeyId(1))).andReturn(new Pair<byte[], Integer>(value.getBytes(), 0));
        replay(clientFactoryMock, nextLocatorMock, service);

        assertArrayEquals(value.getBytes(), nodeLocator.get(new KeyId(1)));
    }

    @Test
    public void testPut_NoReplica() throws Exception {
        NodeLocator nodeLocator = new NodeLocator(null, service, null);
        String value = "Hello World";

        service.put(eq(new KeyId(1)), aryEq(value.getBytes()), eq(0));
        replay(clientFactoryMock, nextLocatorMock, service);

        nodeLocator.put(new KeyId(1), value.getBytes(), 0);
    }

    @Test
    public void testPut_WithReplica() throws Exception {
        Node node = new Node(null);
        node.setSuccessor(new TNode("successor", 1234, new KeyId(123)));
        NodeLocator nodeLocator = new NodeLocator(node, service, clientFactoryMock);
        String value = "Hello World";

        expect(clientFactoryMock.get(node.getSuccessor())).andReturn(nextLocatorMock);
        clientFactoryMock.release(node.getSuccessor());
        nextLocatorMock.put(eq(new KeyId(1)), aryEq(value.getBytes()), eq(1));
        service.put(eq(new KeyId(1)), aryEq(value.getBytes()), eq(2));
        replay(clientFactoryMock, nextLocatorMock, service);

        nodeLocator.put(new KeyId(1), value.getBytes(), 2);
    }

    @Test
    public void testRemove_NoReplica() throws Exception {
        NodeLocator nodeLocator = new NodeLocator(null, service, null);

        service.remove(new KeyId(1));
        replay(clientFactoryMock, nextLocatorMock, service);

        nodeLocator.remove(new KeyId(1), 0);
    }

    @Test
    public void testRemove_WithReplica() throws Exception {
        Node node = new Node(null);
        node.setSuccessor(new TNode("successor", 1234, new KeyId(123)));
        NodeLocator nodeLocator = new NodeLocator(node, service, clientFactoryMock);

        expect(clientFactoryMock.get(node.getSuccessor())).andReturn(nextLocatorMock);
        clientFactoryMock.release(node.getSuccessor());
        nextLocatorMock.remove(new KeyId(1), 1);
        service.remove(new KeyId(1));
        replay(clientFactoryMock, nextLocatorMock, service);

        nodeLocator.remove(new KeyId(1), 2);
    }
}
