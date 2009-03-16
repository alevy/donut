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

package edu.washington.cs.cse490h.donut.util;

import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.service.LocalLocatorClientFactory;

/**
 * Donut closure for leaving nodes
 * 
 * @author alevy
 */
public class DonutLeaveClosure extends DonutClosure {

    private final DonutClient               client;
    private final LocalLocatorClientFactory clientFactory;
    private final TNode                     node;

    /**
     * @param client
     * @param clientFactory
     * @param node
     *            the {@link TNode} to remove.
     */
    public DonutLeaveClosure(DonutClient client, LocalLocatorClientFactory clientFactory, TNode node) {
        this.client = client;
        this.clientFactory = clientFactory;
        this.node = node;
    }

    @Override
    public void run() {
        clientFactory.remove(node);
        client.kill();
    }
}
