package edu.washington.cs.cse490h.donut.service.application;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.cse490h.donut.business.Pair;
import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 *
 */
public class DonutInMemoryHashTableService implements DonutHashTableService {
    
    private final Map<KeyId, Pair<byte[], Integer>> map;
    
    public DonutInMemoryHashTableService() {
        map = new HashMap<KeyId, Pair<byte[],Integer>>();
    }

    public Pair<byte[], Integer> get(KeyId entryId) {
        return map.get(entryId);
    }

    public void put(KeyId entryId, byte[] data, int replicas) {
        map.put(entryId, new Pair<byte[], Integer>(data, replicas));
    }

    public void remove(KeyId entryId) {
        map.remove(entryId);
    }


}
