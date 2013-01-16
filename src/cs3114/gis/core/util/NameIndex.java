package cs3114.gis.core.util;

import java.io.PrintWriter;

import cs3114.gis.container.hash.HashTable;
import java.util.Vector;

/**
 * The index of name and offsets pairs. This is an adapter for the underlying
 * hash table.
 *
 * @author TianyuGeng
 * @version Nov 17, 2012
 */
public class NameIndex {
    private HashTable<NameEntry> index;

    /**
     * Constructor for NameIndex
     */
    public NameIndex() {
        index = new HashTable<NameEntry>();
    }

    /**
     * Find the offsets associated with a name&state.
     *
     * @param name
     * @param state
     * @return the collection of offsets associated with the name and state
     */
    public Vector<Long> find(String name, String state) {
        NameEntry entry = index.find(new NameEntry(name, state));
        if (entry == null) {
            return null;
        }
        return entry.getOffsets();
    }

    /**
     * Get the largest number of probings.
     *
     * @return the largest number of probing
     */
    public int getLongestProbing() {
        return index.getLongestProbing();
    }

    /**
     * Insert a new name&state and offset pair.
     *
     * @param name
     * @param state
     * @param offset
     */
    public void insert(String name, String state, long offset) {
        NameEntry duplicate;
        // if there is a duplicate, put the new offset into this duplicated
        // object; otherwise put the new created object in there.
        if ((duplicate = index.insert(new NameEntry(name, state, offset))) != null) {
            duplicate.addOffset(offset);
        }
    }

    /**
     * Log the content as texts to a PrintWriter.
     *
     * @param log
     *            the PrintWriter to log the content
     */
    public void logString(PrintWriter log) {
        index.logString(log);
    }
}
