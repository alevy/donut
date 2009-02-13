package edu.washington.cs.cse490h.donut.util;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class KeyIdUtil {

    public static boolean isAfterXButBeforeEqualY(KeyId id, KeyId x, KeyId y) {
        return isAfterXButBeforeOrEqualY(id.getId(), x.getId(), y.getId());
    }
    
    public static boolean isAfterXButBeforeOrEqualY(long id, long x, long y) {
        if (y < id) {
            return x > y && x < id;
        } else {
            return x < y && x < id || x > y && x > id;
        }
    }

}
