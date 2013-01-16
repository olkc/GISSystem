/**
 *
 */
package cs3114.gis.container.hash;

import java.io.PrintWriter;

/**
 * This is a implementation of a hash table.
 *
 * @author Tianyu Geng
 * @version Nov 17, 2012
 * @param <T>
 *            The type to be stored in the hash table
 */
public class HashTable<T> {
    private static int[] sizes = { 1019, 2027, 4079, 8123, 16267, 32503,
            65011, 130027, 260111, 520279, 1040387, 2080763, 4161539,
            8323151, 16646323 };
    private static final double LOAD_FACTOR = 0.7;
    // the sizeIndex is used to keep track of the size of this hash table. For
    // example sizeIndex = 0 means the size should be sizes[0], which is 1019.
    private int sizeIndex;
    // the table main table for this hashtable
    private T[] table;
    private int size;
    private int longestProbing;

    /**
     * Constructor for HashTable
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        size = 0;
        sizeIndex = 0;
        longestProbing = 0;
        table = (T[]) new Object[sizes[sizeIndex]];

    }

    /**
     * Getting the number of elements stored in the hash map.
     *
     * @return the size of the hash map
     */
    public int getSize() {
        return size;
    }

    /**
     * Insert an element to the hash table. If the same element is already
     * inserted, it will return the inserted element. Otherwise, it returns
     * null.
     *
     * @param elem
     *            the element to insert
     * @return null if the insertion is successful, otherwise return the
     *         duplicate
     */
    public T insert(T elem) {
        // check the load factor and decide whether or not to reallocate the
        // hash table.
        if (((double) size / table.length) > LOAD_FACTOR) {
            reallocate();
        }
        T result;
        // check to see whether the size should be increased.
        if ((result = insertHelper(table, elem)) == null) {
            size++;
            return null;
        }
        return result;
    }

    private T insertHelper(T[] tb, T elem) {
        // the home slot for the current element
        int home = Math.abs(elem.hashCode() % tb.length);
        // declare the i outside the for loop because it will be used to count
        // the longest probing.
        int i;
        // try to probe for a place to put the new element
        for (i = 0;; i++) {
            int pos = (home + (i * i + i) / 2) % tb.length;
            // the slot is empty, the place to put it is found
            if (tb[pos] == null) {
                tb[pos] = elem;
                break;
            }
            // if the position is holding a duplicate, return this duplicate.
            if (tb[pos].equals(elem)) {
                return tb[pos];
            }
        }
        longestProbing = Math.max(i, longestProbing);
        return null;

    }

    // reallocate the hash table to make the table bigger
    private void reallocate() {
        sizeIndex++;
        @SuppressWarnings("unchecked")
        // create the new table with bigger size
        T[] newTable = (T[]) new Object[sizes[sizeIndex]];
        // traverse the old table and copy every element to the new table
        for (T elem : table) {
            if (elem != null) {
                insertHelper(newTable, elem);
            }
        }

        table = newTable;
    }

    /**
     * Find an element in the hash table.
     *
     * @param elem
     *            the element to look for
     * @return null if the element doesn't have duplicate in the hash table,
     *         otherwise return the duplicate.
     */
    public T find(T elem) {
        // calculate the home slot
        int home = Math.abs(elem.hashCode() % table.length);
        // probe the table to find a match
        for (int i = 0;; i++) {
            // calculate the probing position
            int pos = home + ((i * i + i) / 2) % table.length;
            // if the position is empty, this means there is no match
            if (table[pos] == null) {
                return null;
            }
            // if a match is found, return it
            if (table[pos].equals(elem)) {
                return table[pos];
            }
        }
    }

    /**
     * Get the longest probing conducted by this hash table.
     *
     * @return the number of the longest probing.
     */
    public int getLongestProbing() {
        return longestProbing;
    }

    /**
     * Log the content in the hash table to the PrintWriter in the parameter.
     *
     * @param log
     *            the PrintWriter to log the content
     */
    public void logString(PrintWriter log) {

        log.println("Table size: " + table.length);
        log.println("Number of elements: " + size);
        log.println();
        log.println("    Slot: Content");
        // traverse the table and write the content to the log
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                log.println(String.format("%8d: ", i) + table[i]);
            }
        }

    }

}
