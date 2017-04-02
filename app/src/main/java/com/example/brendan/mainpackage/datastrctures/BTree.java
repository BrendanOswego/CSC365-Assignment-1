package com.example.brendan.mainpackage.datastrctures;


import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.Arrays;


/**
 * Generic Binary Search Tree Class.
 */

public class BTree implements Serializable {
    private static final String TAG = BTree.class.getName();
    private static final String fileName = "btree_23";
    private static int SIZEOF_NODE = 340;
    private static int SIZEOF_INDEX = 12;
    private static int SIZEOF_EVERYTHING_BESIDES_INDEX = 328;

    RandomAccessFile raf;
    Node root;
    int t;
    long offsetPosition;
    Context context;

    boolean deleteFileOnExit = false;

    public BTree(int t, Context context) throws IOException, ClassNotFoundException {
        this.t = t;
        this.context = context;
        File f = new File(context.getCacheDir(), fileName);
        if (deleteFileOnExit) {
            boolean deleted = f.delete();
            System.out.println("Delete File: " + deleted);
            f = new File(context.getCacheDir(), fileName);
        }
        try {
            raf = new RandomAccessFile(f, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        create_tree();
    }

    Node search(Node x, String key) {
        int i = 1;
        int compare = key.compareTo(x.keys[i]);
        while (i <= x.n && compare > 0) {
            i++;
        }
        compare = key.compareTo(x.keys[i]);
        if (i <= x.n && compare == 0) {
            return x;
        } else if (x.leaf) {
            return null;
        } else {

            return search(x.children[i], key);
        }
    }

    void create_tree() throws IOException, ClassNotFoundException {
        Node x = allocate_node();
        x.leaf = true;
        x.n = 0;
        write(x);
        this.root = x;
    }

    void split_child(Node x, int i) throws IOException, ClassNotFoundException {
        Node z = allocate_node();
        Node y = x.children[i];
        z.leaf = y.leaf;
        z.n = t - 1;
        for (int j = 1; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.leaf) {
            for (int j = 1; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        y.n = t - 1;
        for (int j = x.n + 1; j > i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[i + 1] = z;
        for (int j = x.n; j > i; j++) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[i] = y.keys[t];
        x.n = x.n + 1;
        write(y);
        write(z);
        write(x);
    }

    public void insert(String key) throws IOException, ClassNotFoundException {
        Node r = root;
        if (r.n == ((2 * t) - 1)) {
            Node s = allocate_node();
            root = s;
            s.leaf = false;
            s.n = 0;
            s.children[1] = r;
            split_child(s, 1);
            insert_nonfull(s, key);
        } else {
            insert_nonfull(r, key);
        }
    }

    void insert_nonfull(Node x, String key) throws IOException, ClassNotFoundException {
        int i = x.n;
        int compare = key.compareTo(x.keys[i]);
        if (x.leaf) {
            while (i >= 1 && compare < 0) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = key;
            x.n = x.n + 1;
            write(x);
        } else {
            while (i >= 1 && compare < 0) {
                i--;
            }
            i++;
            compare = key.compareTo(x.keys[i]);
            if (x.children[i].n == (2 * t - 1)) {
                split_child(x, i);
                if (compare > 0) {
                    i++;
                }
            }
            insert_nonfull(x.children[i], key);
        }
    }

    void write(Node x) throws IOException, ClassNotFoundException {
        x.setIndex(5);
        byte[] serialize = serialize(x);
        System.out.println("SIZEOF ARR: " + serialize.length);
        raf.write(serialize);
        Node temp = deserialize(serialize);
        System.out.println("Index: " + temp.getIndex());
        offsetPosition = raf.getFilePointer();
    }


    void read() throws IOException, ClassNotFoundException {
        //TODO: Figure out what to put here later.
    }

    Node allocate_node() throws IOException {
        Node x = new Node(t, false);
        x.setIndex(5);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(x);
        oos.close();
        long length = raf.length();
        raf.setLength(raf.length() + baos.toByteArray().length);
        raf.seek(length);

        return x;
    }

    private byte[] serialize(Node obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    private Node deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (Node) o.readObject();
    }

}
