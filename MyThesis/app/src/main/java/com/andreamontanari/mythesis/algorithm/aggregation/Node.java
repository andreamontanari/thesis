package com.andreamontanari.mythesis.algorithm.aggregation;

import com.andreamontanari.mythesis.algorithm.sweepline.Point;

import java.util.List;

public class Node {

	public Point position;
	public int relevance;
	public String id;
	public int degree;
    public int accuracy;

    //costruttore della classe Node, utilizzata in Aggregation
    //relevance parametro chiave nella scelta dell'aggregatore (come accuracy)
    //degree indica il grado di sovrapposzione
    public Node(String id, Point position, int relevance, int degree, int accuracy) {
        this.id = id;
        this.position = position;
        this.relevance = relevance;
        this.degree = degree;
        this.accuracy = accuracy;
    }

    public String toString() {
		return id;
	}

    //dato un id e una lista, restituisce il nodo con tale id presente nella lista
	public static Node getNode(String id, List<Node> list) {
		Node n = null;
		for (int i = 0; i<list.size(); i++) {
			if (id.matches(list.get(i).id)){
				n = list.get(i);
				return n;
			}
		}
		return n;
	}

}
