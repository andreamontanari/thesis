package com.andreamontanari.mythesis.algorithm.aggregation;

import com.andreamontanari.mythesis.sweepline.Point;

public class Element {
	public Point position;
	public boolean show;
	public String id;
	public Node aggegratedWith;
	
	public Element(String id, Point position, boolean show, Node aggregatedWith) {
		this.id = id;
		this.position = position;
		this.show = show;
		this.aggegratedWith = aggregatedWith;
		
	}


    //CONTROLLA SHOW
	public String toString() {
		return "("+id+", "+ aggegratedWith +", "+ show +")";
	}
}
