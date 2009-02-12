package edu.washington.cs.cse490h.donut;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.washington.cs.cse490h.donut.server.DonutModule;
import edu.washington.cs.cse490h.donut.server.DonutPeer;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

/**
 * @author alevy
 */
public class TestDonut {

    public static void main(String[] args) throws Exception {
        DonutModule donutModule1 = new DonutModule();
        donutModule1.setPort(8080);
        donutModule1.setKey(0);
        Injector injector1 = Guice.createInjector(donutModule1);

        DonutModule donutModule2 = new DonutModule();
        donutModule2.setPort(8081);
        donutModule2.setKey(Long.MAX_VALUE / 2);
        donutModule2.setKnownHostname("localhost");
        donutModule2.setKnownPort(8080);
        Injector injector2 = Guice.createInjector(donutModule2);

        DonutModule donutModule3 = new DonutModule();
        donutModule3.setPort(8082);
        donutModule3.setKey(Long.MAX_VALUE);
        donutModule3.setKnownHostname("localhost");
        donutModule3.setKnownPort(8080);
        Injector injector3 = Guice.createInjector(donutModule3);

        DonutModule donutModule4 = new DonutModule();
        donutModule4.setPort(8083);
        donutModule4.setKey(Long.MAX_VALUE + Long.MAX_VALUE / 2);
        donutModule4.setKnownHostname("localhost");
        donutModule4.setKnownPort(8080);
        Injector injector4 = Guice.createInjector(donutModule4);

        injector1.getInstance(DonutPeer.class).run(injector1.getInstance(TNode.class));
        injector2.getInstance(DonutPeer.class).run(injector2.getInstance(TNode.class));
        injector3.getInstance(DonutPeer.class).run(injector3.getInstance(TNode.class));
        injector4.getInstance(DonutPeer.class).run(injector4.getInstance(TNode.class));
    }

}
