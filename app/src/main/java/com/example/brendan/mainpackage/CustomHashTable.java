package com.example.brendan.mainpackage;

import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

/**
 * Using Algorithms and Functions based off of
 * https://mitpress.mit.edu/books/introduction-algorithms pseudo-code
 */
@SuppressWarnings("unchecked")
public class CustomHashTable<K, V> {
    private static final String TAG = CustomHashTable.class.getName();

    private float loadFactor;
    private int slots;
    private int size;
    private HashEntry<K,V>[] hashTable;


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
        loadFactor = .75f;
        hashTable = (HashEntry<K,V>[])new HashEntry[slots];
    }

    public CustomHashTable() {
        size = 0;
        this.slots = 128;
        loadFactor = .75f;
        hashTable = (HashEntry<K,V>[])new HashEntry[slots];
    }

    /**
     * @param key   Key to be added to the table
     * @param value Value that key will store
     */
    public void insert(K key, V value) {

        int j = Math.abs(key.hashCode() % slots);
        HashEntry head = hashTable[j];
        while (head != null) {
            if (head.getKey().equals(key)) {
                head.setValue(value);
                return;
            }
            head = head.next;
        }
        ++size;
        head = hashTable[j];
        HashEntry<K, V> newElement = new HashEntry<>(key, value);
        newElement.next = head;
        hashTable[j] = newElement;
    }

    /**
     * @param key Key being searched from the HashMember[] table
     * @return slot value the key is in
     */
    public V search(K key) {
        int j = Math.abs(key.hashCode() % slots);

        HashEntry<K, V> head = hashTable[j];

        while (head != null) {
            if (head.getKey().equals(key)) {
                return head.getValue();
            }
            head = head.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    /**
     * Method to resize hash table if table is filled up to loadFactor percentage.
     */
    private boolean resize() {
        return size >= (slots * loadFactor);
    }


    private static class HashEntry<K, V> {

        private K key;
        private V value;
        HashEntry<K, V> next;

        public HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }

        K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        V getValue() {
            return value;
        }

        void setValue(V value) {
            this.value = value;
        }
    }
}
