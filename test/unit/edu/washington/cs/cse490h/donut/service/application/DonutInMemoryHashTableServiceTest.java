package edu.washington.cs.cse490h.donut.service.application;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.washington.cs.cse490h.donut.service.thrift.DataPair;
import edu.washington.cs.cse490h.donut.service.thrift.EntryKey;
import edu.washington.cs.cse490h.donut.service.thrift.KeyId;

/**
 * @author alevy
 */
public class DonutInMemoryHashTableServiceTest {

    @Test
    public void testPut() {
        Map<EntryKey, DataPair> map = new HashMap<EntryKey, DataPair>();
        DonutHashTableService hashTableService = new DonutInMemoryHashTableService(map);

        hashTableService.put(new EntryKey(new KeyId(1), "key1"), "hello".getBytes(), 5);

        assertEquals(new DataPair("hello".getBytes(), 5), map
                .get(new EntryKey(new KeyId(1), "key1")));
    }

    @Test
    public void testGetRange() {
        Map<EntryKey, DataPair> map = new HashMap<EntryKey, DataPair>();
        map.put(new EntryKey(new KeyId(1), "key1"), new DataPair("val1".getBytes(), 1));
        map.put(new EntryKey(new KeyId(7), "key7"), new DataPair("val7".getBytes(), 1));
        map.put(new EntryKey(new KeyId(15), "key15"), new DataPair("val15".getBytes(), 1));
        DonutHashTableService hashTableService = new DonutInMemoryHashTableService(map);

        Set<EntryKey> result = hashTableService.getRange(new KeyId(0), new KeyId(10));
        assertEquals(2, result.size());
        assertTrue(result.contains(new EntryKey(new KeyId(1), "key1")));
        assertTrue(result.contains(new EntryKey(new KeyId(7), "key7")));
    }

}
