package edu.washington.cs.cse490h.donut.business;

import java.util.Comparator;

import edu.washington.edu.cs.cse490h.donut.service.KeyId;

/**
 * @author alevy
 */
public class KeyIdComparator implements Comparator<KeyId> {

    private final KeyId baseId;

    /**
     * @param baseId
     *            the {@link KeyId} on which to center the circle.
     */
    public KeyIdComparator(KeyId baseId) {
        this.baseId = baseId;
    }
    
    public NodeComparator getNodeComparator() {
        return new NodeComparator(this);
    }

    /*
     * Normalizes an id to baseId by subtracting baseId and moding by the key-space.
     */
    private KeyId normalize(KeyId id) {
        long diff = id.getId() - baseId.getId();
        if (diff < 0) {
            return new KeyId(Long.MAX_VALUE + diff);
        }
        return new KeyId(diff);
    }

    public int compare(KeyId key1, KeyId key2) {
        long diff = distance(key1, key2);
        if (diff == 0) {
            return 0;
        } else if (diff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Computes the distance from {@code key1} to {@code key2} in a circle based around the {@code
     * baseId} passed in to the constructor.
     * 
     * @param key1
     * @param key2
     * @return The absolute distance clockwise, along a circle's perimeter normalized such that
     *         {@code baseId} is 0, from {@code key1} to {@code key2}. The circle's perimeter size
     *         is equal to the key-space.
     */
    public long distance(KeyId key1, KeyId key2) {
        return normalize(key1).getId() - normalize(key2).getId();
    }

}
