package edu.washington.cs.cse490h.donut.service.application;

import java.util.Set;

import edu.washington.cs.cse490h.donut.service.thrift.DataPair;
import edu.washington.cs.cse490h.donut.service.thrift.EntryKey;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;

/**
 * Interface for application level services.
 * 
 * @author alevy
 */
public interface DonutHashTableService {

    DataPair get(EntryKey key);

    void put(EntryKey key, byte[] data, int numReplicas);

    void remove(EntryKey key);

    Set<EntryKey> getRange(KeyId start, KeyId end);

}
