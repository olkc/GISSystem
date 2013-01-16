/**
 *
 */
package cs3114.gis.core.util;

import java.util.Vector;

/**
 * The name and offsets pair for the name index.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 */
public class NameEntry {
    private String toHash;

    private Vector<Long> offsets;

    /**
     * Constructor for NameEntry
     *
     * @param name
     * @param state
     */
    public NameEntry(String name, String state) {
        this(name, state, 0);
    }

    /**
     * Constructor for NameEntry
     *
     * @param name
     * @param state
     * @param offset
     */
    public NameEntry(String name, String state, long offset) {
        this.toHash = name + ":" + state;
        offsets = new Vector<Long>();
        this.offsets.add(offset);
    }

    /**
     * Add a new offset to the entry.
     *
     * @param offset
     */
    public void addOffset(long offset) {
        offsets.add(offset);
    }

    @Override
    public boolean equals(Object other) {
        if (!other.getClass().equals(NameEntry.class)) {
            return false;
        }
        return this.toHash.equals(((NameEntry) other).toHash);
    }

    /**
     * Get the offsets of in the pair.
     *
     * @return the collection of offsets.
     */
    public Vector<Long> getOffsets() {
        return offsets;
    }

    @Override
    // use the elfHash function to calculate the hash value.
    public int hashCode() {

        int hashValue = 0;
        for (int Pos = 0; Pos < toHash.length(); Pos++) { // use all elements
            hashValue = (hashValue << 4) + toHash.charAt(Pos); // shift/mix
            int hiBits = hashValue & 0xF0000000; // get high nybble
            if (hiBits != 0) {
                hashValue ^= hiBits >> 24; // xor high nybble with second nybble
            }
            hashValue &= ~hiBits; // clear high nybble
        }
        return hashValue;
    }

    @Override
    public String toString() {
        return String.format("%-40s ", toHash) + offsets;
    }
}
