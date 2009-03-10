package edu.washington.cs.cse490h.donut;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.server.DonutModule;
import edu.washington.cs.cse490h.donut.server.DonutPeer;
import edu.washington.cs.cse490h.donut.service.thrift.TNode;

public class Donut {
    public static void main(String[] args) throws Exception {
        DonutModule donutModule = new DonutModule();
        donutModule.parseArgs(args);
        Injector injector = Guice.createInjector(donutModule);
        DonutPeer donutPeer = injector.getInstance(DonutPeer.class);
        
        if (donutModule.getKnownHostname() == null)
            // Not connecting to anyone, make a single chord node (complete ring)
            donutPeer.run(injector.getInstance(Node.class).getTNode());
        else 
            // Connect to the hostname:port given by module args
            donutPeer.run(new TNode(donutModule.getKnownHostname(), donutModule.getKnownPort(), null));
    }
}
