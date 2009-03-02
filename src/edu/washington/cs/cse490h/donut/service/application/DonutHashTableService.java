package edu.washington.cs.cse490h.donut.service.application;

import edu.washington.cs.cse490h.donut.business.Pair;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * Interface for application level services.
 * 
 * @author alevy
 */
public interface DonutHashTableService {

    Pair<byte[], Integer> get(KeyId entryId);

    void put(KeyId entryId, byte[] data, int replicas);

    void remove(KeyId entryId);

}
