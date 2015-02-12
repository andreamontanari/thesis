package com.andreamontanari.mythesis.algorithm.aggregation;

import android.util.Log;

import com.andreamontanari.mythesis.sweepline.ConflictGraph;
import com.andreamontanari.mythesis.sweepline.Intersection;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Aggregation {
	
	/*************************************************************************************
	 * S lista di nodi da ritornare, ANS lista dei nodi "attivi", Q insieme di tutti i nodi
	 *************************************************************************************/

    public static List<Element> F;
	public static List<Element> S;
	public static List<Node> ANS;
	public static List<Node> Q;
	public static long[] sweepTime;
	public static long[] aggregationTime;
	public static long[] totalTime;
	public static int[] iconeMedia;
	
	public static int[] overlaps;
	
	public static void initSet(int numIcons) {
		
		 int relevance = (int) (10 * Math.random()); //rilevanza attributo da 0 a 10

		 Q = new ArrayList<Node>();
		 ANS = new ArrayList<Node>();
		
		//creo la lista di tutti i nodi Q con rilevanza arbitraria, grado di sovrapposizione inizializzato a 0
		for (int i=0; i<numIcons; i++) {
			Q.add(new Node(Intersection.points[i].getId(), Intersection.points[i], relevance, 0));
			ANS.add(new Node(Intersection.points[i].getId(), Intersection.points[i], relevance, 0));
		}
		S = new ArrayList<Element>();
    	
	}
	
	//calcolo il grado di sovrapposizione per ogni nodo della lista list
	//il grado equivale al numero di archi collegati al nodo
	public static void calculateDeg(List<Node> list) {			// -- O(N) con N numero di elementi di list
		overlaps = new int[list.size()];
		for (int i=0; i<list.size(); i++) {
			Node n = list.get(i);
			n.degree = ConflictGraph.outEdges(i).size();
			overlaps[i] = n.degree;
		}
	}
	
	//ritorna il nodo di grado di sovrapposizione massimo
	public static Node getMaxDegNode(List<Node> ANS) {	// -- O(N) con N numero di elementi di ANS
		Node n = null;
		n = ANS.get(0);
		for (int i = 1; i<ANS.size(); i++) {
			if (n.degree < ANS.get(i).degree) {
				n = ANS.get(i);
			}
		}
		return n;
	}
	
	//ritorna il nodo di rilevanza massima
	public static Node getMaxRelNode() {	// -- O(N) con N numero di elementi di ANS
		Node n = null;
		n = ANS.get(0);
		for (int i = 1; i<ANS.size(); i++) {
			if (n.relevance < ANS.get(i).relevance) {
				n = ANS.get(i);
			}
		}
		return n;
	}
	
	//ritorna il nodo di grado di sovrapposizione minimo
	public static Node getMinDegNode() {	// -- O(N) con N numero di elementi di ANS
		Node n = null;
		n = ANS.get(0);
		for (int i=1; i<ANS.size(); i++) {
			if (n.degree > ANS.get(i).degree) {
				n = ANS.get(i);
			}
		}
		return n;
	}
	
	//calcola la lista di nodi da inserire -- gt : grade Threshold , rt : relevance Threshold
	public static List<Element> minAggregation(double gt, int rt, List<Node> ANS) {

        S = new ArrayList<Element>();
		boolean b = true;
		
		if (ANS.size() == 0) 
		{ 
			return S; 
			} else {
			//calcolo i gradi di sovrapposizione per tutti i nodi di ANS -- solo la prima volta
			if (b)
				calculateDeg(ANS);		// O(N)
				b = false;
				
			while (ANS.size() > 0) {	//O(N)
			Node n = getMaxDegNode(ANS); //recupero il nodo con grado di sovrapposizione massimo  -- O(N)
			if (n.degree > gt && n.relevance > rt) {
				//Aggiungo il nodo aggregatore n alla lista di output S
				Element e = new Element(n.id, n.position, true, n);
				S.add(e);		//O(1)
				//Adj List di n
				List<Integer> adjList = new ArrayList<Integer>();
				adjList = ConflictGraph.outEdges(Integer.parseInt(n.id)); //O(1)
				if (adjList.size() > 0) {
				for (int j=0; j < adjList.size(); j++) {
					Node aggregated = Node.getNode(adjList.get(j).toString(), ANS);
					if (aggregated != null) {
					Element aggr = new Element(aggregated.id, aggregated.position, false, n);
					S.add(aggr);	
					ANS.remove(aggregated);
					}
				}
				}
				ANS.remove(n);
			} else {
				Element e = new Element(n.id, n.position, true, n);
				S.add(e);		//O(1)
				ANS.remove(n);
			}
		 }	
		}
        int co = 0;
        for (Element e : S) {
            if (e.show) {
                co++;
            }

        }
		return S;
	}
	
	//calcola la lista di nodi da inserire -- gt : grade Threshold , rt : relevance Threshold
	public static List<Element> maxAggregation(double gt, int rt) {
		boolean b = true;
		
		if (ANS.size() == 0) 
		{ 
			return S; 
			} else {
			//calcolo i gradi di sovrapposizione per tutti i nodi di ANS -- solo la prima volta
			if (b) 
				calculateDeg(ANS);		// O(N)
				b = false;
				
				while (ANS.size() > 0) {	//O(N)
					Node n = getMinDegNode(); //recupero il nodo con grado di sovrapposizione massimo  -- O(N)
					if (n.degree > gt && n.relevance > rt) {
						//Aggiungo il nodo aggregatore n alla lista di output S
						Element e = new Element(n.id, n.position, true, n);
						S.add(e);		//O(1)
						//Adj List di n
						List<Integer> adjList = new ArrayList<Integer>();
						adjList = ConflictGraph.outEdges(Integer.parseInt(n.id)); //O(1)
						if (adjList.size() > 0) {
						for (int j=0; j < adjList.size(); j++) {
							Node aggregated = Node.getNode(adjList.get(j).toString(), ANS);
							if (aggregated != null) {
							Element aggr = new Element(aggregated.id, aggregated.position, false, n);
							S.add(aggr);	
							ANS.remove(aggregated);
							}
						}
						}
						ANS.remove(n);
					} else {
						Element e = new Element(n.id, n.position, true, n);
						S.add(e);		//O(1)
						ANS.remove(n);
					}
				 }	
				}
				return S;
			}
	
	//Algoritmo di minima aggregazione (inizializzazione+algoritmo)
	public static void secondPartA(int numIcon, double gradeT, int relevance) {
		initSet(numIcon);
		minAggregation(gradeT, relevance, ANS);
	}

    public static List<Element> aggregation(double gradeT, int relevance, List<Node> ANS) {
        return minAggregation(gradeT, relevance, ANS);
    }
	
	//Algoritmo di massima aggregazione (inizializzazione+algoritmo)
	public static void secondPartB(int numIcon, double gradeT, int relevance) {
		initSet(numIcon);
		maxAggregation(gradeT, relevance);
	}
	
	public static double getAvgOverlaps(int nr, int relT) {
		Intersection.firstPart(nr, 50, 500, 1000);
		initSet(nr);
		calculateDeg(ANS);
		
		double tot = 0;
		for (int i=0; i<overlaps.length; i++) {
			tot = tot + overlaps[i];
		}
		return (tot/overlaps.length);
	}

	public static void main(String[] args) {
		for (int x=50; x<=1600; x=2*x){
		double[] avgA = new double[10];
		for (int i=0; i< 10; i++) {
		//System.out.println(0.25);
		avgA[i] = getAvgOverlaps(x, 3);
		}
		double tot = 0;
		for (int i=0; i< 10; i++) {
			tot += avgA[i];
		}
		System.out.println(tot/10);
		}
	}
}
