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
    private HashEntry<K, V>[] hashTable;


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
        hashTable = (HashEntry<K, V>[]) new HashEntry[slots];
        //Make sure all elements are null from beginning
        for (int i = 0; i < slots; i++) {
            hashTable[i] = null;
        }
    }

    public CustomHashTable() {
        size = 0;
        this.slots = 128;
        loadFactor = .75f;
        hashTable = (HashEntry<K, V>[]) new HashEntry[slots];
        //Make sure all elements are null from beginning
        for (int i = 0; i < slots; i++) {
            hashTable[i] = null;
        }
    }

    /**
     * @param key   Key to be added to the table
     * @param value Value that key will store
     */
    public void insert(K key, V value) {
        int j = Math.abs(key.hashCode() % slots);
        HashEntry head = hashTable[j];
        HashEntry toAdd = new HashEntry(key, value);
        if (head == null) {
            hashTable[j] = toAdd;
            ++size;
        } else {
            while (head != null) {
                if (head.getKey().equals(key)) {
                    head.setValue(value);
                    ++size;
                    break;
                }
                head = head.next;
            }
            if (head == null) {
                head = hashTable[j];
                toAdd.next = head;
                hashTable[j] = toAdd;
                ++size;
            }
        }
        if ((size * loadFactor) >= slots) {
            //Rehash
            HashEntry[] newTable = hashTable;
            slots = slots * 2;
            for (int i = 0; i < slots; i++) {
                hashTable[i] = null;
            }
            for (HashEntry newHead : newTable) {
                while (newHead != null) {
                    insert((K) newHead.getKey(), (V) newHead.getValue());
                    newHead = newHead.next;
                }
            }
        }

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

    /**
     * @param key Generic Key
     * @return
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

    public int getSize() {
        return size;
    }

    /**
     * Method to resize hash table if table is filled up to loadFactor percentage.
     */
    private boolean needsToResize() {
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
