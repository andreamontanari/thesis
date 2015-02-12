package com.andreamontanari.mythesis.algorithm.aggregation;

import com.andreamontanari.mythesis.sweepline.Point;

public class Element {
	public Point position;
	public boolean show, aggregator;
	public String id;
	public Node aggegratedWith;
	
	public Element(String id, Point position, boolean show, boolean aggregator, Node aggregatedWith) {
		this.id = id;
		this.position = position;
		this.show = show;
        this.aggregator = aggregator;
		this.aggegratedWith = aggregatedWith;
		
	}


    //CONTROLLA SHOW
	public String toString() {
		return "("+id+", "+ aggegratedWith +", "+ show +")";
	}
}
