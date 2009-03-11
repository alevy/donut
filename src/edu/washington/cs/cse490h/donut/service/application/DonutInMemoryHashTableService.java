package edu.washington.cs.cse490h.donut.service.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.washington.cs.cse490h.donut.service.thrift.DataPair;
import edu.washington.cs.cse490h.donut.service.thrift.EntryKey;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
 *
 */
public class DonutInMemoryHashTableService implements DonutHashTableService {
    
    private final Map<EntryKey, DataPair> map;
    
    public DonutInMemoryHashTableService() {
        map = new HashMap<EntryKey, DataPair>();
    }
    
    public DonutInMemoryHashTableService(Map<EntryKey, DataPair> map) {
        this.map = map;
    }

    public DataPair get(EntryKey entryId) {
        return map.get(entryId);
    }

    public void put(EntryKey key, byte[] data, int replicas) {
        map.put(key, new DataPair(data, replicas));
    }

    public void remove(EntryKey entryId) {
        map.remove(entryId);
    }

    public Set<EntryKey> getRange(KeyId start, KeyId end) {
        Set<EntryKey> result = new HashSet<EntryKey>();
        for (EntryKey key : map.keySet()) {
            if (KeyIdUtil.isAfterXButBeforeEqualY(key.getId(), start, end)) {
                result.add(key);
            }
        }
        
        return result;
    }


}
