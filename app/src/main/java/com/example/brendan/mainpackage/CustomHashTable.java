package com.example.brendan.mainpackage;

/**
 * Using Algorithms and Functions based off of
 * https://mitpress.mit.edu/books/introduction-algorithms pseudo-code
 */

public class CustomHashTable<K, V> {
    //If no initial table size is given, default to INIT_SIZE
    static final int INIT_SIZE = 128;

    //Two necessities for building hash table being the amount of slots and the load factor
    private float loadFactor;
    private int slots;
    private HashEntry[] table;
    private int size;

    /**
     * Constructor to initialize instance of class, taking slots as a parameter,
     * declares the loadFactor to default value, and new HashMember[] table
     * with the size given by slots.
     *
     * @param slots Size of the hash table
     */
    public CustomHashTable(int slots) {
        this.slots = slots;
        size = 0;
        this.loadFactor = .75f;
        this.table = new HashEntry[slots];
    }

    public CustomHashTable() {
        size = 0;
        this.slots = INIT_SIZE;
        this.loadFactor = .75f;
        this.table = new HashEntry[slots];
    }

    /**
     * @param key   Key to be added to the HashMember[] table
     * @return the slot key was added to
     */
    public int insert(K key,V value) {
        int i = 0;
        do {
            int j = Math.abs(key.hashCode() % slots);
            if (table[j] == null) {
                HashEntry<K, V> element = new HashEntry<>(key, value);
                table[j] = element;
                return j;
            } else {
                i++;
            }

        } while (i < slots);

        return -1;
    }

    /**
     * @param key Key being searched from the HashMember[] table
     * @return slot value the key is in
     */
    public int search(K key) {
        int i = 0;
        int j;
        do {
            j = Math.abs(key.hashCode() % slots);
            if (table[j] != null) {
                if (table[j].getKey().equals(key)) {
                    return j;
                }
            }
            i++;
        } while (table[j] != null || i < slots);
        return -1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Method to resize hash table if table is filled up to loadFactor percentage.
     */
    private boolean resize() {
        if (size < (slots * loadFactor)) {
            return false;
        } else {
            return true;
        }
    }
}
