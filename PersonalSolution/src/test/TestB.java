package test;

import aggregation.Aggregation;
import aggregation.Element;
import sweepline.Intersection;


//Test che calcola: il tempo di esecuzione degli algoritmi: Sweep-line e aggregazione minima
//il tempo di esecuzione totale
//il numero di icone mostrate
public class TestB {
	
	public static long[] sweepTime;
	public static long[] aggregationTime;
	public static long[] totalTime;
	public static int[] iconeMedia;
	public static double[] gtsM = {0.47, 0.92, 1.63, 2.82, 4.2, 5.21}; //caso medio
	public static double[] gtsMin = {0.09, 0.20, 0.33, 0.55, 0.84, 1.42}; //caso minimo
	public static double[] gtsMax = {0.94, 1.84, 3.36, 5.45, 8.4, 10.42}; //caso massimo
	

	public static void main(String[] args) {
		
		int x = 15;
		
		sweepTime = new long[x];
		aggregationTime = new long[x];
		iconeMedia = new int[x];
		totalTime = new long[x];
		
		int gtcount = 0;
		//ESEGUO PER un numero di icone da 50 a 1600
		for (int i=50; i<= 1600; i=2*i) {
		System.out.println("Risultati per numero di icone "+i);
		double gt = gtsMin[gtcount];
		//per ogni numero di icone, eseguo il tutto 15 volte
		for (int j=0; j<15; j++) {
		long startTimeT = System.currentTimeMillis();
		sweepTime[j] = Intersection.firstPart(i, 5, 50, 100);
   	 	long startTime = System.currentTimeMillis();
		//aggregation		
		//System.out.println("Lancio l'algoritmo min aggregation: ");
		Aggregation.secondPartA(i, gt, 7);
		long stopTime = System.currentTimeMillis();
	    long stopTimeT = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    long elapsedTimeT = stopTimeT - startTimeT;
	    aggregationTime[j] = elapsedTime;
	    totalTime[j] = elapsedTimeT;
	    
		int tot = 0;
		for (Element n : Aggregation.S) {
			if (n.show) { tot++;
				}
		}
		iconeMedia[j] = tot;
		}
		//Tempo medio per l'algoritmo sweep-line
		System.out.print("Tempo medio di esecuzione dell'algoritmo Sweep-line ");
		   double sweepMedia = 0;
		   for (int l=0; l<15; l++ ) {
			 	sweepMedia = sweepMedia + sweepTime[l];
		    }
		   System.out.println(sweepMedia/15);
		   
			//Tempo medio per l'algoritmo di aggregazione minimo
		   System.out.print("Tempo medio di esecuzione dell'algoritmo Min Aggregation ");
	    	double aggregationMedia = 0;
		    for (int l=0; l<15; l++ ) {
		    	aggregationMedia += aggregationTime[l];
		    }
		    System.out.println(aggregationMedia/15);
		    
			//Tempo medio per l'algoritmo generale
		    System.out.print("Tempo medio di esecuzione Totale ");
			double totMedia = 0;
			for (int k=0; k<15; k++) {
				totMedia += totalTime[k];
			}
			System.out.println(totMedia/15);   
			
			 System.out.print("Numero medio di icone mostrate dall'algoritmo implementato ");
		    	double iconMedia = 0;
			    for (int l=0; l<15; l++ ) {
			    	iconMedia += iconeMedia[l];
			    }
			    System.out.println(iconMedia/15);
			
			System.out.println();
		}  
	}

}
