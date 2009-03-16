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
