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

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.cse490h.donut.business.KeyId;
import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class LocalLocatorClientFactory implements LocatorClientFactory {

    private Map<KeyId, NodeLocator> locatorMap = new HashMap<KeyId, NodeLocator>();

    public synchronized void add(TNode node, NodeLocator nodeLocator) {
        locatorMap.put(node.getNodeId(), nodeLocator);
    }

    public void remove(TNode node) {
        locatorMap.remove(node.getNodeId());
    }

    public synchronized Iface get(TNode node) throws RetryFailedException {
        if (locatorMap.containsKey(node.getNodeId())) {
            return locatorMap.get(node.getNodeId());
        }

        // The node wasn't found.
        throw new RetryFailedException();
    }

    public void release(TNode node) {
        // Do nothing
    }

}
