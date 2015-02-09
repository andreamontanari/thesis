package aggregation;

import java.util.List;

import sweepline.Point;

public class Node {

	public Point position;
	public int relevance;
	public String id;
	public int degree;
	
	public Node(String id, Point position, int relevance, int degree) {
		this.id = id;
		this.position = position;
		this.relevance = relevance;
		this.degree = degree;
	}
	
	public String toString() {
		return id;
	}
	
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
