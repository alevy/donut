import java.net.Socket;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator;
import edu.washington.edu.cs.cse490h.donut.service.TNode;

public class TestClient {
    public static void main(String[] args) throws Exception {
        
        try {
            KeyLocator.Client client = new KeyLocator.Client(new TBinaryProtocol(new TSocket(
                    new Socket("localhost", 8080))));
            TNode result = client.findSuccessor(new KeyId(2));
            System.out.println("Success! " + result.getName());
        } catch (TTransportException e) {
            System.err.println(e.getMessage());
        }
    }
}
