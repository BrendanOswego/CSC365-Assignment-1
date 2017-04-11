package com.example.brendan.mainpackage.datastrctures;


import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.brendan.mainpackage.CityFragment;
import com.example.brendan.mainpackage.ContextClass;
import com.example.brendan.mainpackage.R;
import com.example.brendan.mainpackage.model.NodeEntry;
import com.example.brendan.mainpackage.model.NodeList;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Generic Binary Search Tree Class.
 */

public class BTree implements Serializable {
    private static final String TAG = BTree.class.getName();
    private static final String valuesFile = "values";
    private static final String nodeTotal = "nodeTotal";
    public Node root;
    public int totalNodes;
    private int t;


    public BTree(int t) throws IOException, ClassNotFoundException {
        totalNodes = 0;
        this.t = t;
        Node root;
        String name = "node_" + 0;
        File f = new File(ContextClass.getContext().getFilesDir(), name);
        if (!f.exists()) {
            Log.v(TAG, "Write New Root");
            root = new Node(t, true, this);
            root.index = totalNodes;
            writeNode(root, root.index);
            totalNodes = totalNodes + 1;
            writeNodeTotal(totalNodes);
        } else {
            Log.v(TAG, "Reading Root");
            root = readNode(0);
        }
        Log.v(TAG, "Root number: " + root.index);
        this.root = root;
    }

    public void writeNode(Node node, int index) throws IOException {
        Log.v(TAG, "Total Keys in node " + index + " = " + node.n);
        String format = "node_" + index;
        File f = new File(ContextClass.getContext().getFilesDir(), format);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(node);
        out.flush();
        out.close();
        Log.v(TAG, "Node " + index + " written to");
    }

    public Node readNode(int index) throws IOException, ClassNotFoundException {

        String name = "node_" + index;
        File f = new File(ContextClass.getContext().getFilesDir(), name);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        Node n = (Node) in.readObject();
        in.close();
        return n;
    }

    void splitChild(Node x, int i, Node y) throws IOException, ClassNotFoundException {
        Log.v(TAG, "Splitting child");
        Node z = new Node(t, y.leaf, this);
        z.index = totalNodes;
        totalNodes = totalNodes + 1;
        writeNodeTotal(totalNodes);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++)
            z.keys[j] = y.keys[j + t];

        if (!y.leaf) {
            for (int j = 0; j < t; j++)
                z.children[j] = y.children[j + t];
        }
        y.n = t - 1;
        for (int j = x.n + 1; j >= i + 1; j--)
            x.children[j + 1] = x.children[j];

        x.children[i + 1] = z.index;

        for (int j = x.n; j >= i; j--)
            x.keys[j + 1] = x.keys[j];

        x.keys[i] = y.keys[t - 1];
        x.n = x.n + 1;
        writeNode(y, y.index);
        writeNode(z, z.index);
        writeNode(x, x.index);
    }

    public void insert(String key) throws IOException, FileNotFoundException, ClassNotFoundException {
        Node r = readNode(root.index);
        if (r.n == (2 * t) - 1) {
            Log.v(TAG, "Root full size");
            Node s = new Node(r.t, false, this);
            root = s;
            s.index = totalNodes;
            totalNodes = totalNodes + 1;
            writeNodeTotal(totalNodes);
            s.children[0] = r.index;
            splitChild(s, 0, r);
            insertNonfull(s, key);
        } else {
            Log.v(TAG, "Root Node is not full");
            insertNonfull(r, key);
        }
    }

    public void insertNonfull(Node x, String k) throws IOException, ClassNotFoundException {
        int i = x.n - 1;
        if (x.leaf) {
            while (i >= 0 && k.compareTo(x.keys[i]) < 0) {
                x.keys[i + 1] = x.keys[i];
                i = i - 1;
            }
            x.keys[i + 1] = k;
            x.n = x.n + 1;
            writeNode(x, x.index);
        } else {
            while (i >= 0 && k.compareTo(x.keys[i]) < 0) {
                i = i - 1;
            }
            i = i + 1;
            Node child = readNode(x.children[i]);
            if (child.n == (2 * t) - 1) {
                splitChild(x, i, child);
                if (k.compareTo(x.keys[i]) > 0) {
                    i = i + 1;
                }
            }
            if (x.children[i] != child.index) {
                child = readNode(x.children[i]);
            }
            insertNonfull(child, k);
        }
    }

    public void writeValues(String name, Double tavg, Double tmax) throws IOException {
        Log.v(TAG, "Writing Values...Name = " + name + " TAVG = " + tavg + " TMAX = " + tmax);
        Writer writer = null;
        BufferedWriter buffer = null;
        if (tavg == null) tavg = -999.0;
        if (tmax == null) tmax = -999.0;
        Gson gson = new Gson();
        File f = new File(ContextClass.getContext().getFilesDir(), valuesFile);
        if (!f.exists()) {
            try {
                NodeList entries = new NodeList();
                ArrayList<NodeEntry> entryList = new ArrayList<>();
                NodeEntry entry = new NodeEntry();
                entry.setName(name);
                entry.setTavg(tavg);
                entry.setTmax(tmax);
                entryList.add(entry);
                entries.setEntries(entryList);
                writer = new FileWriter(f.getAbsolutePath());
                buffer = new BufferedWriter(writer);
                gson.toJson(entries, buffer);
            } finally {
                if (buffer != null) {
                    buffer.close();
                }
                if (writer != null) {
                    writer.close();
                }

            }
        } else {
            try {

                FileInputStream fis = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fis);
                NodeList nodes = gson.fromJson(isr, NodeList.class);
                NodeEntry entry = new NodeEntry();
                entry.setTmax(tmax);
                entry.setTavg(tavg);
                entry.setName(name);
                nodes.getEntries().add(entry);
                writer = new FileWriter(f.getAbsolutePath());
                buffer = new BufferedWriter(writer);
                gson.toJson(nodes, buffer);
            } finally {
                if (buffer != null) {
                    buffer.close();
                }
                if (writer != null) {
                    writer.close();
                }

            }
        }
    }

    public NodeList readValues() throws IOException {
        Gson gson = new Gson();
        File f = new File(ContextClass.getContext().getFilesDir(), valuesFile);
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fis);
        NodeList nodes = gson.fromJson(isr, NodeList.class);
        isr.close();
        return nodes;
    }

    public void writeTree(BTree tree) throws IOException {
        String format = "btree_serialized";
        File f = new File(ContextClass.getContext().getFilesDir(), format);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(tree);
        out.flush();
        out.close();
    }

    public BTree readTree() throws IOException, ClassNotFoundException {
        String format = "btree_serialized";
        File f = new File(ContextClass.getContext().getFilesDir(), format);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        BTree n = (BTree) in.readObject();
        in.close();
        return n;
    }

    public void writeNodeTotal(int total) throws IOException {
        File f = new File(ContextClass.getContext().getFilesDir(), nodeTotal);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeInt(total);
        out.flush();
        out.close();
    }

    public int readNodeTotal() throws IOException {
        File f = new File(ContextClass.getContext().getFilesDir(), nodeTotal);
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        int n = in.readInt();
        in.close();
        return n;
    }

}
