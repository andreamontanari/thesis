package com.andreamontanari.mythesis.algorithm.sweepline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConflictGraph {
	
    int n;
    public static List<Integer>[] adj;
    
    @SuppressWarnings("unchecked")
	public ConflictGraph(int num) {
    	
        n = num;
        //inizializzo la lista di adiacenza che contiene il grafo dei conflitti
        adj = (List<Integer>[])new List[n];
        for (int i = 0; i < n; i++) 
        	//per ogni slot aggiungo una Lista di interi
            adj[i] = new ArrayList<Integer>();
    }
    
    //funzione per aggiungere un arco dal nodo i al nodo j -- O(1)
    public void addEdge(int i, int j) {
        adj[i].add(j);
    }
    
    //funzione per rimuovere un arco da i a j -- O(deg(i)), deg(i) grado del nodo = numero di archi uscenti
    public void removeEdge(int i, int j) {
        Iterator<Integer> it = adj[i].iterator();
        while (it.hasNext()) {
            if (it.next() == j) {
                it.remove();
                return;
            }
        }    
    }
    
    //controlla se esiste l'arco i--j -- O(deg(i)), deg(i) grado del nodo = numero di archi uscenti
    public boolean hasEdge(int i, int j) {
        return adj[i].contains(j);
    }
    
    //ritorna la lista di adiacenza del nodo i -- O(1)
    public static List<Integer> outEdges(int i) {
        return adj[i];
    }
    
    //ritorna la lista di archi entranti (cercando da ogni nodo) -- O(n+m), n numero vertici, m numero archi
    public List<Integer> inEdges(int i) {
        List<Integer> edges = new ArrayList<Integer>();
        for (int j = 0; j < n; j++)
            if (adj[j].contains(i))    
            	edges.add(j);
        return edges;
    }
}
