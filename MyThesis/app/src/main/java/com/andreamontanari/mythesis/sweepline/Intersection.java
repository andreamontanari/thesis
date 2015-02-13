package com.andreamontanari.mythesis.sweepline;

import android.util.Log;

import com.andreamontanari.mythesis.SecondActivity;

public class Intersection {

	public static Interval2D[] rects; 
	public static MinPQ<Event> pq;
	public static IntervalST<Interval2D> st;
	public static Point[] points;
	public static ConflictGraph G;

	//funzione per creare nr icone (all'interno di un'area x * y) di grandezza icDim
    public static void createRects(int nr, int x, int y, int icDim) {
    		rects = new Interval2D[nr];
    		points = new Point[nr];
    		//System.out.println("Stampo i punti creati random: ");
         for (int i = 0; i < nr; i++) {
             int xmin = (int) (x * Math.random());
             int ymin = (int) (y * Math.random());
             int xmax = xmin + icDim;
             int ymax = ymin + icDim;
             points[i] = new Point(xmin, ymin, String.valueOf(i));
             rects[i] = new Interval2D(new Interval1D(xmin, xmax), new Interval1D(ymin, ymax), points[i]);
             //System.out.println(points[i]);
         }
         //inizializzo il grafo dei conflitti
         G = new ConflictGraph(nr);
         
         
    }
    //funzione che aggiunge gli eventi alla coda di priorita' 
    public static void createEvents(int nr, Interval2D[] rects, Point[] p) {
        //inizializzo la coda di priorita'
    		pq = new MinPQ<Event>();
        for (int i = 0; i < nr; i++) {
            Event e1 = new Event(rects[i].intervalX.low,  rects[i], p[i]);
            Event e2 = new Event(rects[i].intervalX.high, rects[i], p[i]);
            pq.insert(e1);
            pq.insert(e2);
        }
    }
    //funzione che ritorna il punto, date in input le coordinate (x,y)
    public static Point getPoint(int x, int y,Point[] points) {
    	Point p = null;
    	for (int i=0; i< points.length; i++) {
    		if (points[i].getX() == x && points[i].getY() == y) {
    			p = points[i];
    			return p;
    		}
    	}
    	return p;
    }
    
    //codice dell'algoritmo sweep line
    public static void sweepLineAlg(Point[] points)  {
    	st = new IntervalST<Interval2D>();
        while (!pq.isEmpty()) {
        	
            Event e = pq.delMin();
            int time = e.time;
            Interval2D rect = e.rect;
            Point point = e.p;
           
            // next event is the right endpoint of interval i
            if (time == rect.intervalX.high)
                st.remove(rect.intervalY);
            // next event is the left endpoint of interval i
            else {
                for (Interval1D x : st.searchAll(rect.intervalY)) {
                    //System.out.println("Intersection:  " + rect + ", " + st.get(x)); //st.get(x) get the rect intersected
                    int coordX = st.get(x).intervalX.low;
                    int coordY = st.get(x).intervalY.low;
                    Point p2 = getPoint(coordX, coordY, points);
                    //aggiungo l'arco
                    G.addEdge(Integer.parseInt(point.getId()),Integer.parseInt(p2.getId()));
                    G.addEdge(Integer.parseInt(p2.getId()),Integer.parseInt(point.getId()));
                }
                st.put(rect.intervalY, rect);
            }
        }
 
    }
    
    public static long firstPart(int numIcons, int icDim, int xMax, int yMax) {
    	
   	 long startTime = System.currentTimeMillis();
    	//crea 10 box compresi in un piano che ha x->0..50, y->0..100
    	createRects(numIcons, xMax, yMax, icDim);

    	//creo gli eventi
    	createEvents(numIcons, rects, points);

    	//System.out.println("Lancio lo sweep line algorithm: ");
         //algoritmo sweepline
    	 sweepLineAlg(points);
    	 long stopTime = System.currentTimeMillis();
         long elapsedTime = stopTime - startTime;
         //System.out.println("Sweep alg time: "+elapsedTime);
    	 return elapsedTime;
         //System.out.println("Stampo il grafo dei conflitti: ");
         /*
    	//stampa il grafo dei conflitti
    	for (int i=0; i < numIcons;i++) {
    		System.out.println(i+": "+ConflictGraph.outEdges(i));
    	}
    	*/
    	
    }

    public static void sweepline(int numIcons,Point[] points,  Interval2D[] rects) {

        G = new ConflictGraph(numIcons);

        createEvents(numIcons, rects, points);

        sweepLineAlg(points);

    }

}

