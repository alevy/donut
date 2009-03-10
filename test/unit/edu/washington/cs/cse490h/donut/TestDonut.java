package edu.washington.cs.cse490h.donut;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.server.DonutModule;
import edu.washington.cs.cse490h.donut.server.DonutPeer;

/**
 * @author alevy
 */
public class TestDonut {

    public static void main(String[] args) throws Exception {
        DonutModule donutModule1 = new DonutModule();
        donutModule1.setPort(8080);
        donutModule1.setKey(0x0000000000000000L);
        Injector injector1 = Guice.createInjector(donutModule1);

        DonutModule donutModule2 = new DonutModule();
        donutModule2.setPort(8081);
        donutModule2.setKey(0x4000000000000000L);
//        donutModule2.setKnownHostname("localhost");
//        donutModule2.setKnownPort(8080);
        Injector injector2 = Guice.createInjector(donutModule2);

        DonutModule donutModule3 = new DonutModule();
        donutModule3.setPort(8082);
        donutModule3.setKey(0x8000000000000000L);
//        donutModule3.setKnownHostname("localhost");
//        donutModule3.setKnownPort(8080);
        Injector injector3 = Guice.createInjector(donutModule3);

        DonutModule donutModule4 = new DonutModule();
        donutModule4.setPort(8083);
        donutModule4.setKey(0xC000000000000000L);
//        donutModule4.setKnownHostname("localhost");
//        donutModule4.setKnownPort(8080);
        Injector injector4 = Guice.createInjector(donutModule4);

        injector2.getInstance(DonutPeer.class).run(injector2.getInstance(Node.class).getTNode());
        injector1.getInstance(DonutPeer.class).run(injector2.getInstance(Node.class).getTNode());
        injector4.getInstance(DonutPeer.class).run(injector1.getInstance(Node.class).getTNode());
        injector3.getInstance(DonutPeer.class).run(injector4.getInstance(Node.class).getTNode());
    }

}
