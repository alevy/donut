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

import org.apache.thrift.TException;

import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.business.TNode;

public class DonutJoinNewClosure extends DonutClosure {

    private final DonutTestRunner testRunner;
    private final TNode           knownNode;
    private final String          name;
    private final long            id;

    public DonutJoinNewClosure(DonutTestRunner testRunner, TNode knownNode, String name, long id) {
        this.testRunner = testRunner;
        this.knownNode = knownNode;
        this.name = name;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            int node = testRunner.createAndAddNode(name, id);
            DonutClient donutClient = testRunner.client(node);
            donutClient.join(knownNode);
            donutClient.start();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}