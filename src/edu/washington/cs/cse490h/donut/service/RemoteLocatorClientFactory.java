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

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

import edu.washington.cs.cse490h.donut.service.KeyLocator;
import edu.washington.cs.cse490h.donut.business.TNode;

/**
 * @author alevy
 */
public class RemoteLocatorClientFactory extends AbstractRetriable<KeyLocator.Iface, TNode>
        implements LocatorClientFactory {

    private Map<TNode, Socket> socketMap = new HashMap<TNode, Socket>();

    @Override
    public synchronized KeyLocator.Iface tryOne(TNode node) throws Exception {
        TProtocol protocol;
        while (socketMap.containsKey(node)) {
            wait();
        }

        Socket socket = new Socket(node.getName(), node.getPort());
        socketMap.put(node, socket);
        protocol = new TBinaryProtocol(new TSocket(socket));
        return new KeyLocator.Client(protocol);
    }

    public synchronized void release(TNode node) {
        Socket socket = socketMap.remove(node);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        notify();
    }
}
