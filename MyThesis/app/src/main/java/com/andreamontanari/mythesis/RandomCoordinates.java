package com.andreamontanari.mythesis;


import java.util.Random;

/**
 * Created by andreamontanari on 13/02/15.
 */

//classe ausiliaria per la generazione di coordinate casuali
public class RandomCoordinates {

    public static double[] lats = new double[1000];
    public static double[] longs = new double[1000];

    public static void main(String[] args) {

        for (int i=0; i<1000; i++) {
            lats[i] = getLats(46.080966, 13.213656, 20);
            longs[i] = getLongs(46.080966, 13.213656, 20);
        }

        for (int j=0; j<1000; j++) {
            System.out.print(lats[j]+", ");
        }
    }

    //funzione che ritorna una longitudine casuale
    public static double getLongs(double x0, double y0, int radius) {

        Random random = new Random();
        //converte il raggio da metri in gradi
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        double foundLongitude = y + y0;
        return foundLongitude;
    }

    //funzione che ritorna una latitudine casuale
    public static double getLats(double x0, double y0, int radius) {

        Random random = new Random();

        //converte il raggio da metri in gradi
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        //sistema la coordinata x al diminuire della distanza est-ovest
        double new_x = x / Math.cos(y0);

        double foundLatitude = new_x + x0;
        return foundLatitude;
    }
}