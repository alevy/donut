package edu.washington.cs.cse490h.donut.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.cse490h.donut.business.EntryKey;
import edu.washington.cs.cse490h.donut.business.KeyId;
import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
 */
public class DonutHashRequestServiceTest {

    private LocatorClientFactory clientFactoryMock;
    private KeyLocator.Iface     curLocatorMock;
    private KeyLocator.Iface     nextLocatorMock;

    @Before
    public void setUp() throws Exception {
        clientFactoryMock = createMock(LocatorClientFactory.class);
        curLocatorMock = createMock(KeyLocator.Iface.class);
        nextLocatorMock = createMock(KeyLocator.Iface.class);
    }

    @After
    public void tearDown() throws Exception {
        verify(clientFactoryMock);
        verify(curLocatorMock);
        verify(nextLocatorMock);
    }

    @Test
    public void testGet() throws Exception {
        DonutHashRequestService requestService = new DonutHashRequestService(curLocatorMock,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);
        byte[] value = "value".getBytes();
        
        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        expect(nextLocatorMock.get(new EntryKey(keyId, keyStr))).andReturn(value);
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);
        
        assertArrayEquals(value, requestService.get(keyStr));
    }

    @Test
    public void testPut() throws Exception {
        DonutHashRequestService requestService = new DonutHashRequestService(curLocatorMock,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);
        byte[] value = "value".getBytes();
        
        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        nextLocatorMock.put(new EntryKey(keyId, keyStr), value);
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);
        
        requestService.put(keyStr, value);
    }

    @Test
    public void testRemove() throws Exception {
        DonutHashRequestService requestService = new DonutHashRequestService(curLocatorMock,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);
        
        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        nextLocatorMock.remove(new EntryKey(keyId, keyStr));
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);
        
        requestService.remove(keyStr);
    }

}
