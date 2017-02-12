package com.example.brendan.mainpackage;

import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

import java.util.ArrayList;

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
    private ArrayList<V> listOfAddedEntries;


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
        listOfAddedEntries = new ArrayList<>();
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
        listOfAddedEntries = new ArrayList<>();
    }

    /**
     * @param key   Key to be added to the table
     * @param value Value that key will store
     */
    public void insert(K key, V value) {
        int j = Math.abs(key.hashCode() % slots);
        HashEntry head = hashTable[j];
        HashEntry temp = new HashEntry(key, value);
        //Empty bucket
        if (head == null) {
            hashTable[j] = temp;
        } else {
            //Bucket is not empty
            while (head.next != null && !head.next.getKey().equals(key)) {
                head = head.next;
            }
            head.next = temp;
        }
        size++;
    }

    /**
     * @param key Key being searched from the HashMember[] table
     * @return slot value the key is in
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

    public int totalSlots() {
        return slots;
    }

    public HashEntry[] printTable() {
        return hashTable;
    }

    public ArrayList<V> getAllValues() {
        return listOfAddedEntries;
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
