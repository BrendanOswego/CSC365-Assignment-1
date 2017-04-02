package com.example.brendan.mainpackage.datastrctures;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Brendan on 3/12/2017.
 */

public class Node implements Serializable {

    int index; //Index in RAF
    int t; //Degree of the BTree
    int n; //number of keys in node
    boolean leaf; //is leaf
    String[] keys; //array of keys
    Node[] children; //child nodes in this node

    public Node(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        keys = new String[(2 * t) - 1];
        children = new Node[2 * t];
        this.index = 0;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public Node[] getChildren() {
        return children;
    }

    public void setChildren(Node[] children) {
        this.children = children;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public boolean nodeIsFull(Node x) {
        for (int i = 0; i < x.keys.length; i++) {
            if (x.keys[i] != null) return false;
        }
        return true;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
