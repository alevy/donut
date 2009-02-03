package edu.washington.cs.cse490h.donut;

import java.util.logging.Logger;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.Node;
import edu.washington.cs.cse490h.donut.callback.ConnectionFailedException;
import edu.washington.cs.cse490h.donut.callback.LocatorCallbackFactory;
import edu.washington.cs.cse490h.donut.service.LocatorClientFactory;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;
import edu.washington.edu.cs.cse490h.donut.service.MetaData;
import edu.washington.edu.cs.cse490h.donut.service.KeyLocator.Iface;

/**
 * @author alevy
 */
public class NodeLocator implements Iface {

    private static Logger                LOGGER = Logger.getLogger(NodeLocator.class.getName());
    private final LocatorCallbackFactory callbackFactory;
    private final Node                   node;
    private final LocatorClientFactory   clientFactory;

    @Inject
    public NodeLocator(Node node, LocatorCallbackFactory callbackFactory,
            LocatorClientFactory clientFactory) {
        this.node = node;
        this.callbackFactory = callbackFactory;
        this.clientFactory = clientFactory;
    }

    @Override
    public void lookup(KeyId id, String caller) throws TException {
        LOGGER
                .info("Caller \"" + caller + "\" requested entity with id \"" + id.toString()
                        + "\".");
        if (node.isResponsibleFor(id)) {
            try {
                callbackFactory.get(caller).lookup(id, new MetaData());
            } catch (ConnectionFailedException e) {
                throw new TException(e);
            }
        } else {
            try {
                clientFactory.get(node.getNextHop(id).getName()).lookup(id, caller);
            } catch (ConnectionFailedException e) {
                throw new TException(e);
            }
        }
    }
}
