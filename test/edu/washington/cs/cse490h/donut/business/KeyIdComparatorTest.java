package edu.washington.cs.cse490h.donut.business;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class KeyIdComparatorTest {

    @Test
    public void testDistance() {
        KeyId baseId = new KeyId(50);
        KeyIdComparator keyIdComparator = new KeyIdComparator(baseId);

        assertEquals(0, keyIdComparator.distance(new KeyId(100), new KeyId(100)));
        assertEquals(-10, keyIdComparator.distance(new KeyId(100), new KeyId(110)));
        assertEquals(10, keyIdComparator.distance(new KeyId(100), new KeyId(90)));
        assertEquals(-(Long.MAX_VALUE - 20),
                keyIdComparator.distance(new KeyId(60), new KeyId(40)));
    }

}
