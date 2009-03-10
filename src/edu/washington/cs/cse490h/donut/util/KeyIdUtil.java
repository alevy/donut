package edu.washington.cs.cse490h.donut.util;

import edu.washington.cs.cse490h.donut.service.thrift.KeyId;

/**
 * @author alevy
 */
public class KeyIdUtil {

    /**
     * Is id after x in the keyspace and also before or equal to y in the keyspace?
     * Also returns true if x and y are the same, because if the same we are searching the whole keyspace
     * 
     * @param id The KeyId that we are testing 
     * @param x The exclusive lower bound   
     * @param y The inclusive upper bound
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

}
