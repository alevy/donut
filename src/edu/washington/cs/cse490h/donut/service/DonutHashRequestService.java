package edu.washington.cs.cse490h.donut.service;

import org.apache.thrift.TException;

import com.google.inject.Inject;

import edu.washington.cs.cse490h.donut.business.EntryKey;
import edu.washington.cs.cse490h.donut.business.TNode;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
 */
public class DonutHashRequestService implements HashService.Iface {

    private final KeyLocator.Iface keyLocator;
    private final LocatorClientFactory clientFactory;

    @Inject
    public DonutHashRequestService(KeyLocator.Iface keyLocator, LocatorClientFactory clientFactory) {
        this.keyLocator = keyLocator;
        this.clientFactory = clientFactory;
    }

    public byte[] get(String key) throws DataNotFoundException, TException {
        EntryKey entryKey = new EntryKey(KeyIdUtil.generateKeyId(key), key);
        TNode successor = keyLocator.findSuccessor(entryKey.getId());
        
        try {
            KeyLocator.Iface hashClient = clientFactory.get(successor);
            return hashClient.get(entryKey);
        } catch (RetryFailedException e) {
            throw new TException(e);
        } finally {
            clientFactory.release(successor);
        }
    }

    public void put(String key, byte[] value) throws TException {
        EntryKey entryKey = new EntryKey(KeyIdUtil.generateKeyId(key), key);
        TNode successor = keyLocator.findSuccessor(entryKey.getId());
        
        try {
            KeyLocator.Iface hashClient = clientFactory.get(successor);
            hashClient.put(entryKey, value);
        } catch (RetryFailedException e) {
            throw new TException(e);
        } catch (NotResponsibleForId e) {
            throw new TException(e);
        } finally {
            clientFactory.release(successor);
        }
    }

    public void remove(String key) throws TException {
        EntryKey entryKey = new EntryKey(KeyIdUtil.generateKeyId(key), key);
        TNode successor = keyLocator.findSuccessor(entryKey.getId());
        
        try {
            KeyLocator.Iface hashClient = clientFactory.get(successor);
            hashClient.remove(entryKey);
        } catch (RetryFailedException e) {
            throw new TException(e);
        } catch (NotResponsibleForId e) {
            throw new TException(e);
        } finally {
            clientFactory.release(successor);
        }
    }

}
