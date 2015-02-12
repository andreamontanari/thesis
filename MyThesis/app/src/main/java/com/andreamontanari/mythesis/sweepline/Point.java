package com.andreamontanari.mythesis.sweepline;

//classe Punto rappresentante un punto dello spazio
public class Point {

	private int x;
	private int y;
	private String id;
	
	//un punto ha sempre una coppia di coordinate x,y e un ID
	public Point(int x, int y, String id){
  	  	this.setX(x);
    	this.y=y;
    	this.id=id;
	}
	
	public String toString(){
    	return "("+this.id +","+ this.getX()+","+this.y+")";
	}
	//metodi getter
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public String getId() {
		return id;
	}
	
	//metodi setter
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
}