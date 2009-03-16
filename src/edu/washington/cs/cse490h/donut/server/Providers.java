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
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerTransport;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.washington.cs.cse490h.donut.service.HashService;
import edu.washington.cs.cse490h.donut.service.KeyLocator;

/**
 * @author alevy
 */
public class Providers {

    protected static class TServerProvider implements Provider<TServer> {
        private final TProcessor       proc;
        private final TServerTransport transport;

        @Inject
        private TServerProvider(TServerTransport transport, TProcessor proc) {
            this.proc = proc;
            this.transport = transport;
        }

        public TServer get() {
            return new TThreadPoolServer(proc, transport);
        }
    }

    protected static class TKeyLocatorProcessorProvider implements Provider<TProcessor> {
        private final KeyLocator.Iface iface;

        @Inject
        private TKeyLocatorProcessorProvider(KeyLocator.Iface iface) {
            this.iface = iface;
        }

        public TProcessor get() {
            return new KeyLocator.Processor(iface);
        }
    }

    protected static class TRequestServerProcessorProvider implements Provider<TProcessor> {
        private final HashService.Iface iface;

        @Inject
        private TRequestServerProcessorProvider(HashService.Iface iface) {
            this.iface = iface;
        }

        public TProcessor get() {
            return new HashService.Processor(iface);
        }
    }

}
