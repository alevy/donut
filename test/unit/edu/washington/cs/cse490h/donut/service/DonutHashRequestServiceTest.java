/*
 * Copyright 2009 Amit Levy, Jeff Prouty, Rylan Hawkins
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
 */
public class DonutHashRequestServiceTest {

    private LocatorClientFactory clientFactoryMock;
    private Node                 node;
    private KeyLocator.Iface     curLocatorMock;
    private KeyLocator.Iface     nextLocatorMock;

    @Before
    public void setUp() throws Exception {
        clientFactoryMock = createMock(LocatorClientFactory.class);
        curLocatorMock = createMock(KeyLocator.Iface.class);
        node = new Node("localhost", 8080, null);
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
        DonutHashRequestService requestService = new DonutHashRequestService(node,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);
        byte[] value = "value".getBytes();

        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(clientFactoryMock.get(node.getTNode())).andReturn(curLocatorMock);
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        clientFactoryMock.release(node.getTNode());
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        expect(nextLocatorMock.get(new EntryKey(keyId, keyStr))).andReturn(value);
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);

        assertArrayEquals(value, requestService.get(keyStr));
    }

    @Test
    public void testPut() throws Exception {
        DonutHashRequestService requestService = new DonutHashRequestService(node,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);
        byte[] value = "value".getBytes();

        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(clientFactoryMock.get(node.getTNode())).andReturn(curLocatorMock);
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        clientFactoryMock.release(node.getTNode());
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        nextLocatorMock.put(new EntryKey(keyId, keyStr), value);
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);

        requestService.put(keyStr, value);
    }

    @Test
    public void testRemove() throws Exception {
        DonutHashRequestService requestService = new DonutHashRequestService(node,
                clientFactoryMock);
        String keyStr = "hello world";
        KeyId keyId = KeyIdUtil.generateKeyId(keyStr);

        TNode successor = new TNode("successor", 8080, new KeyId(1));
        expect(clientFactoryMock.get(node.getTNode())).andReturn(curLocatorMock);
        expect(curLocatorMock.findSuccessor(keyId)).andReturn(successor);
        clientFactoryMock.release(node.getTNode());
        expect(clientFactoryMock.get(successor)).andReturn(nextLocatorMock);
        nextLocatorMock.remove(new EntryKey(keyId, keyStr));
        clientFactoryMock.release(successor);
        replay(clientFactoryMock, nextLocatorMock, curLocatorMock);

        requestService.remove(keyStr);
    }

}
