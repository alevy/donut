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

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.google.inject.AbstractModule;

/**
 * @author alevy
 */
public class RequestModule extends AbstractModule {

    private final DonutModule donutModule;

    public RequestModule(DonutModule donutModule) {
        this.donutModule = donutModule;
    }

    @Override
    protected void configure() {
        binder().install(donutModule);
        try {
            binder().bind(TServerTransport.class).toInstance(
                    new TServerSocket(donutModule.getRequestPort()));
        } catch (TTransportException e) {
            System.err.println("Unable to listen on port " + donutModule.getRequestPort() + ".");
            System.exit(1);
        }
        binder().bind(TProcessor.class).toProvider(Providers.TRequestServerProcessorProvider.class);
        binder().bind(TServer.class).toProvider(Providers.TServerProvider.class);
    }

}
