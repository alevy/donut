import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.service.NodeLocator;
import edu.washington.cs.cse490h.donut.service.RemoteLocatorClientFactory;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;

public class TestServer {
    public static void main(String[] args) throws Exception {
        Node node1 = new Node("localhost", 8080, new KeyId(1000));
        Node node2 = new Node("localhost", 8081, new KeyId(3000));
        node1.setFingers(node2);
        node1.setPredecessor(node2);
        node2.setFingers(node1);
        node2.setPredecessor(node1);
        
        TProcessor proc1 = new KeyLocator.Processor(new NodeLocator(node1,
                new RemoteLocatorClientFactory()));
        TProcessor proc2 = new KeyLocator.Processor(new NodeLocator(node1,
                new RemoteLocatorClientFactory()));
        final TServer server1 = new TThreadPoolServer(proc1, new TServerSocket(8080));
        final TServer server2 = new TThreadPoolServer(proc2, new TServerSocket(8081));
        new Thread() {
            @Override
            public void run() {
                super.run();
                server1.serve();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                server2.serve();
            }
        }.start();
    }
}
