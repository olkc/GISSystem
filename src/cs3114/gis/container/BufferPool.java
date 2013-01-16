/**
 *
 */
package cs3114.gis.container;

/**
 * The LRU buffer pool.
 *
 * @author Tianyu Geng
 * @version Nov 18, 2012
 * @param <K>
 *            the type of the key to locate a value
 * @param <V>
 *            the type of the value stored in the buffer pool
 */
public class BufferPool<K, V> {

    private static final class Node<K, V> {
        K key;
        V value;
        /**
         * the previous node
         */
        Node<K, V> previous;
        /**
         * the next node
         */
        Node<K, V> next;

        Node() {
            key = null;
            value = null;
            previous = null;
            next = null;
        }

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * The capacity of the buffer pool.
     */
    public static final int CAPACITY = 20;
    private int size;
    private Node<K, V> head;
    private Node<K, V> tail;

    /**
     * Constructor for BufferPool
     */
    public BufferPool() {
        size = 0;
        head = new Node<K, V>();
        tail = new Node<K, V>();
        head.next = tail;
        tail.previous = head;
    }

    /**
     * Get the value associated with the key.
     *
     * @param key
     * @return the value associated with the key. null if the key doesn't exist
     */
    public V get(K key) {
        Node<K, V> current = head.next;
        // traver the list and find a match. On found, move this match to the
        // top of this list.
        while (current != tail) {
            if (current.key.equals(key)) {
                current.previous.next = current.next;
                current.next.previous = current.previous;
                current.next = head.next;
                current.previous = head;
                head.next.previous = current;
                head.next = current;
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Insert a new key-value pair into the pool
     *
     * @param key
     * @param value
     */
    public void insert(K key, V value) {
        Node<K, V> newNode = new Node<K, V>(key, value);
        newNode.next = head.next;
        newNode.previous = head;
        head.next.previous = newNode;
        head.next = newNode;
        size++;
        // if the number of elements exceeds the limit, delete the node at the
        // end.
        if (size > CAPACITY) {
            Node<K, V> last = tail.previous;
            tail.previous = last.previous;
            tail.previous.next = tail;
            last.previous = null;
            last.next = null;
            size--;

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Most Recently Used]\n");
        Node<K, V> current = head.next;
        // traverse the buffer and convert the content to a string.
        while (current != tail) {
            sb.append(String.format("%10s:  ", current.key.toString()));
            sb.append(current.value + "\n");
            current = current.next;
        }
        sb.append("[Least Recently Used]");
        return sb.toString();
    }
}
