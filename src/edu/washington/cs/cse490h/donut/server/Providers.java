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
