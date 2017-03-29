package com.example.brendan.mainpackage.datastrctures;

import android.support.annotation.NonNull;

/**
 * Created by Brendan on 3/12/2017.
 */

public class Node<K> {
    K key;
    Node right;
    Node left;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }
}
