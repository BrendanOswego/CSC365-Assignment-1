package com.example.brendan.mainpackage.datastrctures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by brendan on 4/10/17.
 */

public class Node implements Serializable {
    private static final String TAG = Node.class.getName();
    public int index;
    int n;
    int t;
    String keys[];
    int children[];
    boolean leaf;
    BTree tree;

    Node(int t, boolean leaf, BTree tree) {
        this.t = t;
        keys = new String[(2 * t) - 1];
        children = new int[2 * t];
        this.leaf = leaf;
        n = 0;
        this.tree = tree;
    }


    public void traverse() throws IOException, ClassNotFoundException {
        if (leaf) {
            for (int i = 0; i < t; i++) {
                if (keys[i] != null) {
                    System.out.println(keys[i] + "\n");
                }
            }
        } else {
            for (int i = 0; i < t; i++) {
                Node child = readNode(children[i]);
                if (child != null) {
                    child.traverse();
                }
            }
        }
    }

    public Node search(Node x, String key) throws IOException, ClassNotFoundException {
        int i = 0;
        while (i < n && key.compareTo(x.keys[i]) > 0) {
            i++;
        }
        if (keys[i].equals(key)) {
            return this;
        }
        if (leaf) {
            return null;
        } else {
            Node child = null;
            for (int j = 0; j < children.length; j++) {
                child = readNode(x.children[i]);
            }
            if (child != null) {
                return search(child, key);
            }
            return null;
        }
    }

    public Node readNode(int index) throws IOException, ClassNotFoundException {
        String name = "node_" + index;
        File f = new File(tree.context.getFilesDir(), name);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        Node n = (Node) in.readObject();
        in.close();
        return n;
    }


}
