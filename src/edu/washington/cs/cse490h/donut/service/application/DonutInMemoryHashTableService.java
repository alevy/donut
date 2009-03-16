/*
 * Copyright 2009 Amit Levy, Jeff Prouty, Rylan Hawkins
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.washington.cs.cse490h.donut.service.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.washington.cs.cse490h.donut.business.DataPair;
import edu.washington.cs.cse490h.donut.business.EntryKey;
import edu.washington.cs.cse490h.donut.business.KeyId;
import edu.washington.cs.cse490h.donut.util.KeyIdUtil;

/**
 * @author alevy
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
