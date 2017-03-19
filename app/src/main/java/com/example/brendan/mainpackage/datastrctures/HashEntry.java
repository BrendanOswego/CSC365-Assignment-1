package com.example.brendan.mainpackage.datastrctures;

/**
 * Generic class that structures the entries to be added to the hashTable.
 *
 * @param <K> Generic Key for class.
 * @param <V> Generic Value for class.
 */
public class HashEntry<K, V> {

    K key;
    V value;
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
    public K getKey() {
        return key;
    }

    /**
     * @return value generic value.
     */
    public V getValue() {
        return value;
    }

}