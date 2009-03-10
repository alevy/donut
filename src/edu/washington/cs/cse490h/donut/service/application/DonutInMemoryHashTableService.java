package edu.washington.cs.cse490h.donut.service.application;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.cse490h.donut.business.Pair;
import edu.washington.edu.cs.cse490h.donut.service.EntryKey;

/**
 * @author alevy
 *
 */
public class DonutInMemoryHashTableService implements DonutHashTableService {
    
    private final Map<EntryKey, Pair<byte[], Integer>> map;
    
    public DonutInMemoryHashTableService() {
        map = new HashMap<EntryKey, Pair<byte[],Integer>>();
    }

    public Pair<byte[], Integer> get(EntryKey entryId) {
        return map.get(entryId);
    }

    public void put(EntryKey key, byte[] data, int replicas) {
        map.put(key, new Pair<byte[], Integer>(data, replicas));
    }

    public void remove(EntryKey entryId) {
        map.remove(entryId);
    }


}
