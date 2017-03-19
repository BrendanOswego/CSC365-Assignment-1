package com.example.brendan.mainpackage.datastrctures;

import java.io.Serializable;

/**
 * Created by Brendan on 3/12/2017.
 */

public class BTree implements Serializable {

    Node root;

    public int height(Node node){
        if(node == null) return -1;
        int left = height(node.left);
        int right = height(node.right);
        return (left >= right) ? left + 1 : right + 1;
    }
    public void insert(Node n,double data){

    }

}
