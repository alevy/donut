package edu.washington.cs.cse490h.donut.service.application;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * Interface for application level services.
 * 
 * @author alevy
 */
public interface DonutHashTableService {

    byte[] get(KeyId entryId);

    void put(KeyId entryId, byte[] data);

    void remove(KeyId entryId);

}
