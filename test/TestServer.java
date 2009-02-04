import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

import edu.washington.cs.cse490h.donut.NodeLocator;
import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.service.RemoteLocatorClientFactory;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;

public class TestServer {
    public static void main(String[] args) throws Exception {
        Node node = new Node("localhost", new KeyId(1));
        TProcessor proc = new KeyLocator.Processor(new NodeLocator(node,
                new RemoteLocatorClientFactory()));
        TSimpleServer server = new TSimpleServer(proc, new TServerSocket(8080));
        server.serve();
    }
}
