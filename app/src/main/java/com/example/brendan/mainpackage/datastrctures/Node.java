package com.example.brendan.mainpackage.datastrctures;

import android.content.Context;
import android.os.Environment;

import com.example.brendan.mainpackage.CityFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

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
    ArrayList<String> traversalList;
    transient Context context;

    Node(int t, boolean leaf, BTree tree) {
        this.t = t;
        keys = new String[(2 * t) - 1];
        children = new int[2 * t];
        this.leaf = leaf;
        n = 0;
        this.tree = tree;
        traversalList = new ArrayList<>();
    }

    public void traverse() throws IOException, ClassNotFoundException {
        if (leaf) {
            for (int i = 0; i < t; i++) {
                if (keys[i] != null) {
                    System.out.println(keys[i] + "\n");
                    traversalList.add(keys[i]);
                }
            }
        } else {
            for (int i = 0; i < t; i++) {
                Node child = tree.readNode(children[i]);
                if (child != null) {
                    child.traverse();
                }
            }
        }
    }

    public ArrayList<String> getTraversalList() {
        return traversalList;
    }

}
