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
public class ServerModule extends AbstractModule {

    private final DonutModule donutModule;

    public ServerModule(DonutModule donutModule) {
        this.donutModule = donutModule;
    }

    @Override
    protected void configure() {
        binder().install(donutModule);
        try {
            binder().bind(TServerTransport.class).toInstance(
                    new TServerSocket(donutModule.getPort()));
        } catch (TTransportException e) {
            System.err.println("Unable to listen on port " + donutModule.getPort() + ".");
            System.exit(1);
        }
        binder().bind(TProcessor.class).toProvider(Providers.TKeyLocatorProcessorProvider.class);
        binder().bind(TServer.class).toProvider(Providers.TServerProvider.class);
    }
}
