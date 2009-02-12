package edu.washington.cs.cse490h.donut.util;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class KeyIdUtil {

    public static boolean isAfterXButBeforeY(KeyId id, KeyId x, KeyId y) {
        return isAfterXButBeforeY(id.getId(), x.getId(), y.getId());
    }
    
    public static boolean isAfterXButBeforeY(long id, long x, long y) {
        if (y < id) {
            return x > y && x < id;
        } else {
            return x < y && x < id || x > y && x > id;
        }
    }

}
