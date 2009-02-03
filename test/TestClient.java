import java.net.Socket;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.LocatorCallback;
import edu.washington.edu.cs.cse490h.donut.service.MetaData;

public class TestClient {
    
    private static class LookupCallbackImpl implements LocatorCallback.Iface {

        public void lookup(KeyId id, MetaData data) throws TException {
            System.out.println("Result: " + data.toString());
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        
        Thread server = new Thread() {
            @Override
            public void run() {
                super.run();
                TProcessor proc = new LocatorCallback.Processor(new LookupCallbackImpl());
                TSimpleServer server;
                try {
                    server = new TSimpleServer(proc, new TServerSocket(8081));
                    server.serve();
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }
        };
        server.start();
        
        try {
            KeyLocator.Client client = new KeyLocator.Client(new TBinaryProtocol(new TSocket(
                    new Socket("localhost", 8080))));
            client.lookup(new KeyId(123), "localhost");
            System.out.println("Success");
        } catch (TTransportException e) {
            System.err.println(e.getMessage());
        }
    }
}
