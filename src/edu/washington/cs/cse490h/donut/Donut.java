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

package edu.washington.cs.cse490h.donut;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.server.DonutClient;
import edu.washington.cs.cse490h.donut.server.DonutModule;
import edu.washington.cs.cse490h.donut.server.DonutPeer;
import edu.washington.cs.cse490h.donut.server.DonutServer;
import edu.washington.cs.cse490h.donut.server.RequestModule;
import edu.washington.cs.cse490h.donut.server.ServerModule;
import edu.washington.cs.cse490h.donut.business.TNode;

/**
 * @author alevy
 */
public class Donut {
    public static void main(String[] args) throws Exception {
        DonutModule donutModule = new DonutModule();
        donutModule.parseArgs(args);

        ServerModule donutServerModule = new ServerModule(donutModule);
        RequestModule requestModule = new RequestModule(donutModule);
        Injector serverModuleInjector = Guice.createInjector(donutServerModule);
        Injector requestModuleInjector = Guice.createInjector(requestModule);
        DonutPeer donutPeer = new DonutPeer(serverModuleInjector.getInstance(DonutServer.class),
                serverModuleInjector.getInstance(DonutClient.class), requestModuleInjector
                        .getInstance(DonutServer.class));

        if (donutModule.getKnownHostname() == null)
            // Not connecting to anyone, make a single chord node (complete ring)
            donutPeer.run(serverModuleInjector.getInstance(Node.class).getTNode());
        else
            // Connect to the hostname:port given by module args
            donutPeer.run(new TNode(donutModule.getKnownHostname(), donutModule.getKnownPort(),
                    null));
    }
}
