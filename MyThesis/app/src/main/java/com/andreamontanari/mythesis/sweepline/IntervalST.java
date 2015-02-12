package com.andreamontanari.mythesis.sweepline;

import java.util.LinkedList;

//Implementazione di un Interval Search Tree generato con l'utilizzo di un BST randominzzato.
public class IntervalST<Value>  {

    private Node root;   // radice del BST

    // BST helper node data type
    //Costruttori utili al BST
    private class Node {
        Interval1D interval;      // chiave
        Value value;              // valore associato
        Node left, right;         // sotto alberi sx dx
        int N;                    // dicmensione del sottoalbero radicato in questo nodo
        int max;                  // estremo maggiore del sottoalbero radicato in questo nodo

        Node(Interval1D interval, Value value) {
            this.interval = interval;
            this.value    = value;
            this.N        = 1;
            this.max      = interval.high;
        }
    }


   /*************************************************************************
    *  Ricerca
    *************************************************************************/

    public boolean contains(Interval1D interval) {
        return (get(interval) != null);
    }

    //ritorna il valore associate alla chiave, altrimenti null
    public Value get(Interval1D interval) {
        return get(root, interval);
    }
    
    private Value get(Node x, Interval1D interval) {
        if (x == null)                  return null;
        int cmp = interval.compareTo(x.interval);
        if      (cmp < 0) return get(x.left, interval);
        else if (cmp > 0) return get(x.right, interval);
        else              return x.value;
    }


   /*************************************************************************
    *  Inserimento random
    *************************************************************************/
    public void put(Interval1D interval, Value value) {
        if (contains(interval)) {
        	//System.out.println("duplicate"); 
        	remove(interval);  }
        root = randomizedInsert(root, interval, value);
    }

    //inserisce il nuovo nodo alla radice con probabilita uniforme
    private Node randomizedInsert(Node x, Interval1D interval, Value value) {
        if (x == null) return new Node(interval, value);
        if (Math.random() * size(x) < 1.0) return rootInsert(x, interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0)  x.left  = randomizedInsert(x.left,  interval, value);
        else          x.right = randomizedInsert(x.right, interval, value);
        fix(x);
        return x;
    }

    private Node rootInsert(Node x, Interval1D interval, Value value) {
        if (x == null) return new Node(interval, value);
        int cmp = interval.compareTo(x.interval);
        if (cmp < 0) { x.left  = rootInsert(x.left,  interval, value); x = rotR(x); }
        else         { x.right = rootInsert(x.right, interval, value); x = rotL(x); }
        return x;
    }


   /*************************************************************************
    *  Cancellazione
    *************************************************************************/
    private Node joinLR(Node a, Node b) { 
        if (a == null) return b;
        if (b == null) return a;

        if (Math.random() * (size(a) + size(b)) < size(a))  {
            a.right = joinLR(a.right, b);
            fix(a);
            return a;
        }
        else {
            b.left = joinLR(a, b.left);
            fix(b);
            return b;
        }
    }

    //rimuove e ritorna il valore associato all'intervallo dato in input, altrimenti null
    public Value remove(Interval1D interval) {
        Value value = get(interval);
        root = remove(root, interval);
        return value;
    }

    private Node remove(Node h, Interval1D interval) {
        if (h == null) return null;
        int cmp = interval.compareTo(h.interval);
        if      (cmp < 0) h.left  = remove(h.left,  interval);
        else if (cmp > 0) h.right = remove(h.right, interval);
        else              h = joinLR(h.left, h.right);
        fix(h);
        return h;
    }


   /*************************************************************************
    *  Ricerca di intervalli
    *************************************************************************/

    //funzione che ritorna l'intervallo nella SD che interseca con il dato intervallo, altrimenti null -- O(log N)
    public Interval1D search(Interval1D interval) {
        return search(root, interval);
    }

    //controlla nel sottoalbero radicato nel nodo x
    public Interval1D search(Node x, Interval1D interval) {
        while (x != null) {
            if (interval.intersects(x.interval))  return x.interval;
            else if (x.left == null)             x = x.right;
            else if (x.left.max < interval.low)  x = x.right;
            else                                 x = x.left;
        }
        return null;
    }


    //ritorna tutti gli intervalli nella SD che intersecano con l'intervallo dato -- O(R log N) R numero di intersezioni
    public Iterable<Interval1D> searchAll(Interval1D interval) {
        LinkedList<Interval1D> list = new LinkedList<Interval1D>();
        searchAll(root, interval, list);
        return list;
    }

  //controlla nel sottoalbero radicato nel nodo x
    public boolean searchAll(Node x, Interval1D interval, LinkedList<Interval1D> list) {
         boolean found1 = false;
         boolean found2 = false;
         boolean found3 = false;
         if (x == null)
            return false;
        if (interval.intersects(x.interval)) {
            list.add(x.interval);
            found1 = true;
        }
        if (x.left != null && x.left.max >= interval.low)
            found2 = searchAll(x.left, interval, list);
        if (found2 || x.left == null || x.left.max < interval.low)
            found3 = searchAll(x.right, interval, list);
        return found1 || found2 || found3;
    }


   /*************************************************************************
    *  Funzioni utili su alberi binari
    *************************************************************************/

    //ritorna il numero di nodi nel sottoalbero radicato in x
    public int size() { return size(root); }
    private int size(Node x) { 
        if (x == null) return 0;
        else           return x.N;
    }

    //altezza dell'albero, albero vuoto = 0
    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return 0;
        return 1 + Math.max(height(x.left), height(x.right));
    }


   /*************************************************************************
    *  Funzione ausiliarie per BST
    *************************************************************************/

    // fix auxilliar information (subtree count and max fields)
    private void fix(Node x) {
        if (x == null) return;
        x.N = 1 + size(x.left) + size(x.right);
        x.max = max3(x.interval.high, max(x.left), max(x.right));
    }

    private int max(Node x) {
        if (x == null) return Integer.MIN_VALUE;
        return x.max;
    }

    // precondition: a is not null
    private int max3(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    // right rotate
    private Node rotR(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        fix(h);
        fix(x);
        return x;
    }

    // left rotate
    private Node rotL(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        fix(h);
        fix(x);
        return x;
    }


   /*************************************************************************
    *  Debugging functions that test the integrity of the tree
    *************************************************************************/

    /*
    
    // check integrity of subtree count fields
    public boolean check() { return checkCount() && checkMax(); }

    // check integrity of count fields
    private boolean checkCount() { return checkCount(root); }
    private boolean checkCount(Node x) {
        if (x == null) return true;
        return checkCount(x.left) && checkCount(x.right) && (x.N == 1 + size(x.left) + size(x.right));
    }

    private boolean checkMax() { return checkMax(root); }
    private boolean checkMax(Node x) {
        if (x == null) return true;
        return x.max ==  max3(x.interval.high, max(x.left), max(x.right));
    }

*/
    
    
}

