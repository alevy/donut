package edu.washington.cs.cse490h.donut;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.washington.cs.cse490h.donut.server.DonutModule;
import edu.washington.cs.cse490h.donut.server.DonutPeer;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class Donut {

    public static void main(String[] args) throws Exception {
        DonutModule donutModule = new DonutModule();
        donutModule.parseArgs(args);
        Injector injector = Guice.createInjector(donutModule);
        DonutPeer donutPeer = injector.getInstance(DonutPeer.class);
        donutPeer.run(injector.getInstance(TNode.class));
    }
}
