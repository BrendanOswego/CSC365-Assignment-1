package com.example.brendan.mainpackage;

/**
 * Using Algorithms and Functions based off of
 * https://mitpress.mit.edu/books/introduction-algorithms pseudo-code
 */

public class CustomHashTable{
    //If no initial table size is given, default to INIT_SIZE
    private static final int INIT_SIZE = 128;

    //Two necessities for building hash table being the amount of slots and the load factor
    private float loadFactor;
    private int slots;
    private HashMember[] table;

    /**
     * Constructor to initialize instance of class, taking slots as a parameter,
     * declares the loadFactor to default value, and new HashMember[] table
     * with the size given by slots.
     * @param slots Size of the hash table
     */
    public CustomHashTable(int slots){
        this.slots = slots;
        this.loadFactor = .75f;
        this.table = new HashMember[slots];
    }

    /**
     * Constructor to initialize instance of class, taking loadFactor as a parameter, declaring the
     * slots variable to default value, and new HashMember[] table with the size given by slots.
     * by slots
     * @param loadFactor How full the hash table can get before it needs to be resized
     */
    public CustomHashTable(float loadFactor) {
        this.slots = INIT_SIZE;
        this.loadFactor = loadFactor;
        this.table = new HashMember[slots];
    }

    /**
     * Constructor to initialize instance fo class, taking slots, and loadFactor as parameters,
     * and declaring new HashMember[] table with the size given by slots.
     * @param slots Size of the hash table
     * @param loadFactor How full the hash table can get before it needs to be resized
     */
    public CustomHashTable(int slots,float loadFactor){
        this.slots = slots;
        this.loadFactor = loadFactor;
        this.table = new HashMember[slots];
    }

    /**
     *
     * @param key String to be added to the HashMember[] table
     * @return the slot key was added to
     */
    public int insert(String key){
        int i = 0;
        do{
            int j = getHashValue(key);
            if(table[j] == null){
                table[j] = new HashMember(key,j);
                return j;
            }else {
                i++;
            }
        }while(i < slots);

        throw new Error("Hash Table Overflow");
    }

    /**
     *
     * @param key String to be searched from the HashMember[] table
     * @return slot value the key is in
     */
    public int search(String key){
        int i = 0;
        int j;
        do{
            j = getHashValue(key);
            if(table[j] != null) {
                if (table[j].getKey().equals(key)) {
                    return j;
                }
            }
            i++;
        }while(table[j] != null || i < slots);
        return -1;
    }

    /**
     *
     * @param key String that will be converted into a hash integer to be inserted into HashMember[]
     *            table.
     * @return value of calculated hash.
     */
    private int getHashValue(String key){
        int hash = 0;

        for(int i =1;i < key.length();i++){
            hash ^= (hash << 5) + (hash >> 2) + (int)key.charAt(i);
        }
        hash = hash % slots;

        //Got -33 for one of them so need to take abs of hash
        return Math.abs(hash);
    }

    /**
     * Inner class that contains the structure for a single element in the custom Hash Table
     */
    private static class HashMember{

        private String key;
        private int value;

        /**
         * Constructor to initialize instance of class, taking key and value as parameters.
         * @param key String that acts as key in hash table
         * @param value Integer that acts as value in hash table
         */
        public HashMember(String key,int value){
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
