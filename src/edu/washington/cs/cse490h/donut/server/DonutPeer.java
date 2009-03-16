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

package edu.washington.cs.cse490h.donut.server;

import org.apache.thrift.TException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.washington.cs.cse490h.donut.business.TNode;

/**
 * @author alevy
 */
public class DonutPeer {

    public static final String DONUT_NODE           = "DonutNode";
    public static final String DONUT_REQUEST_SERVER = "DonutRequestServer";

    private final DonutServer  server;
    private final DonutClient  client;
    private final DonutServer  requestThread;

    @Inject
    public DonutPeer(@Named(value = DONUT_NODE) DonutServer serverThread, DonutClient client,
            @Named(value = DONUT_REQUEST_SERVER) DonutServer requestThread) {
        this.server = serverThread;
        this.client = client;
        this.requestThread = requestThread;
    }

    public void run(TNode knownNode) throws TException {
        server.start();
        client.join(knownNode);
        client.start();
        requestThread.start();
    }

    public void kill() {
        requestThread.kill();
        server.kill();
        client.kill();
    }
}
