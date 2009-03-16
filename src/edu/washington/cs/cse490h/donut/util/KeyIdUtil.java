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

package edu.washington.cs.cse490h.donut.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.washington.cs.cse490h.donut.Constants;
import edu.washington.cs.cse490h.donut.business.KeyId;

/**
 * @author alevy
 */
public class KeyIdUtil {

    /**
     * Is id after x in the keyspace and also before or equal to y in the keyspace? Also returns
     * true if x and y are the same, because if the same we are searching the whole keyspace
     * 
     * @param id
     *            The KeyId that we are testing
     * @param x
     *            The exclusive lower bound
     * @param y
     *            The inclusive upper bound
     * @return See above
     */
    public static boolean isAfterXButBeforeEqualY(KeyId id, KeyId x, KeyId y) {
        return isAfterXButBeforeOrEqualY(id.getId(), x.getId(), y.getId());
    }

    public static boolean isAfterXButBeforeOrEqualY(long id, long x, long y) {
        if (x == y)
            return true;
        if (y < id) {
            return x > y && x < id;
        } else {
            return x < y && x < id || x > y && x > id;
        }
    }

    /**
     * Generates a {@link KeyId} from the given String by SHA1 hashing it and concatenating into 64
     * bits.
     * 
     * @param key
     *            a {@link String} to use for generating the {@link KeyId}
     * @return a {@link KeyId} which is a hash of the {@code key}
     */
    public static KeyId generateKeyId(String key) {
        try {
            KeyId keyId = new KeyId(0);

            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hash = md.digest(key.getBytes());
            int keySpaceBytes = Constants.KEY_SPACE / 4;
            for (int i = 0; i < Math.min(hash.length, keySpaceBytes); ++i) {
                keyId.setId(keyId.getId() | (((long) hash[i] & 0xff) << (i * 4)));
            }
            return keyId;
        } catch (NoSuchAlgorithmException e) {
            // Will never happen because SHA algorithm exists
            throw new RuntimeException(e);
        }
    }

}
