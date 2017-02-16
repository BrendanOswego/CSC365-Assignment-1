package com.example.brendan.mainpackage;

/**
 * Using Algorithms and Functions based off of
 * https://mitpress.mit.edu/books/introduction-algorithms pseudo-code
 */
@SuppressWarnings("unchecked")
public class CustomHashTable<K, V> {
    private float loadFactor;
    private int slots;
    private int size;
    private HashEntry<K, V>[] hashTable;

    /**
     * Constructor to initialize instance of class, taking slots as a parameter,
     * declares the loadFactor to default value, and new HashEntry[] table
     * with the size given by slots.
     *
     * @param slots Size of the hash table.
     */
    public CustomHashTable(int slots) {
        this.slots = slots;
        size = 0;
        loadFactor = .75f;
        hashTable = (HashEntry<K, V>[]) new HashEntry[slots];
        for (int i = 0; i < slots; i++) {
            hashTable[i] = null;
        }
    }

    /**
     * Constructor to initialize instance of class, declares slots to default slots,
     * declares the loadFactor to default value, and new HashEntry[] table
     * with the size given by slots.
     */
    public CustomHashTable() {
        size = 0;
        this.slots = 128;
        loadFactor = .75f;
        hashTable = (HashEntry<K, V>[]) new HashEntry[slots];
        for (int i = 0; i < slots; i++) {
            hashTable[i] = null;
        }
    }

    /**
     * @param key   Key to be added to the table.
     * @param value Value that key will store.
     */
    public void insert(K key, V value) {
        int j = Math.abs(key.hashCode() % slots);
        HashEntry head = hashTable[j];
        HashEntry temp = new HashEntry(key, value);
        if (head == null) {
            hashTable[j] = temp;
            size++;
        } else {
            while (head.next != null && !head.next.getKey().equals(key)) {
                head = head.next;
            }
            head.next = temp;
        }

    }

    /**
     * @param key Key being searched from the HashMember[] table.
     * @return slot value the key is in.
     */
    public V search(K key) {
        int j = Math.abs(key.hashCode() % slots);

        HashEntry head = hashTable[j];

        while (head != null) {
            if (head.getKey().equals(key)) {
                return (V) head.getValue();
            }
            head = head.next;
        }
        return null;
    }

    /**
     * @param key Generic Key.
     * @return value paired to key given as parameter.
     */
    public V remove(K key) {

        int j = key.hashCode() % slots;
        HashEntry head = hashTable[j];
        if (head == null) {
            return null;
        }
        if (head.getKey().equals(key)) {
            V value = (V) head.getValue();
            head = head.next;
            hashTable[j] = head;
            --size;
            return value;
        } else {
            HashEntry next = null;
            while (head != null) {
                if (head.getKey().equals(key)) {
                    next.next = head.next;
                    --size;
                    return (V) head.getValue();
                }
                next = head;
                head = head.next;
            }
            --size;
            return null;
        }
    }

    /**
     * @return size of hashTable.
     */
    public int getSize() {
        return size;
    }

    /**
     * Method to resize hash table if table is filled up to loadFactor percentage.
     */
    private boolean needsToResize() {
        return size >= (slots * loadFactor);
    }

    /**
     * Generic inner class that structures the entries to be added to the hashTable.
     *
     * @param <K> Generic Key for class.
     * @param <V> Generic Value for class.
     */
    private static class HashEntry<K, V> {

        private K key;
        private V value;
        HashEntry<K, V> next;

        /**
         * Public constructor initializing variables.
         *
         * @param key   Assign key from constructor.
         * @param value Assign value from constructor.
         */
        public HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }

        /**
         * @return key generic value.
         */
        K getKey() {
            return key;
        }

        /**
         * @return value generic value.
         */
        V getValue() {
            return value;
        }

    }
}
