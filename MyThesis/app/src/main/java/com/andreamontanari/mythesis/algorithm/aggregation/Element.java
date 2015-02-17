package com.andreamontanari.mythesis.algorithm.aggregation;

import com.andreamontanari.mythesis.algorithm.sweepline.Point;

import java.util.List;

public class Element {
	public Point position;
	public boolean show, aggregator;
	public String id;
	public Node aggegratedWith;

    //costruttore della classe Element, utilizzata in Aggregation
    //show indica se l'elemento corrente viene o meno visualizzato sulla mappa
    //aggregator indica se l'elemento è un aggregatore o un singolo
    //l'oggetto aggregatedWith è il nodo al quale l'elemento è aggregato
	public Element(String id, Point position, boolean show, boolean aggregator, Node aggregatedWith) {
		this.id = id;
		this.position = position;
		this.show = show;
        this.aggregator = aggregator;
		this.aggegratedWith = aggregatedWith;
	}

    //funzione per ritorna un elemento presente in list, dato il suo id
    public static Element getElementById(String id, List<Element> list) {
        Element e = null;
        for (Element x : list) {
            if (x.id == id) {
                e = x;
                return e;
            }
        }
        return e;
    }

	public String toString() {
		return "("+id+", "+ aggegratedWith +", "+ show +")";
	}
}
