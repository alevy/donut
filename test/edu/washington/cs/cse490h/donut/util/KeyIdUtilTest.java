package edu.washington.cs.cse490h.donut.util;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author alevy
 *
 */
public class KeyIdUtilTest {

    @Test
    public void testIsAfterXButBeforeY_AllPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(7, 2, 11));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(12, 2, 11));
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(15, 12, 11));
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(2, 12, 11));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(12, 13, 11));
    }
    
    @Test
    public void testIsAfterXButBeforeY_AllNegative() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-7, -11, -2));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(-12, -11, -2));
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-15, -11, -12));
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-2, -11, -12));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(-12, -11, -13));
    }
    
    @Test
    public void testIsAfterXButBeforeY_XNegativeYAndIdPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(4, -11, 8));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(8, -11, 4));
    }
    
    @Test
    public void testIsAfterXButBeforeY_YNegativeXAndIdPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(11, 8, -4));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(8, 11, -4));
    }
    
    @Test
    public void testIsAfterXButBeforeY_IdNegativeYAndXPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-11, 8, 4));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(-11, 4, 8));
    }
    
    @Test
    public void testIsAfterXButBeforeY_XAndYNegativeIdPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(11, -4, -8));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(11, -8, -4));
    }
    
    @Test
    public void testIsAfterXButBeforeY_XAndIdNegativeYPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-4, -11, 8));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(-11, -4, 8));
    }
    
    @Test
    public void testIsAfterXButBeforeY_YAndIdNegativeXPositive() throws Exception {
        assertTrue(KeyIdUtil.isAfterXButBeforeOrEqualY(-11, 4, -8));
        assertFalse(KeyIdUtil.isAfterXButBeforeOrEqualY(-4, 11, -8));
    }
    
}
