package edu.washington.cs.cse490h.donut.service.application;

import edu.washington.cs.cse490h.donut.business.Pair;
import edu.washington.edu.cs.cse490h.donut.service.EntryKey;

/**
 * Interface for application level services.
 * 
 * @author alevy
 */
public interface DonutHashTableService {

    Pair<byte[], Integer> get(EntryKey key);

    void put(EntryKey key, byte[] data, int replicas);

    void remove(EntryKey key);

}
