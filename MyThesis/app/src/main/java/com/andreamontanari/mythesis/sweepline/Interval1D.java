package com.andreamontanari.mythesis.sweepline;


//Implementazione di un intervallo a 1 dimensione con coordinate intere
public class Interval1D implements Comparable<Interval1D> {
    public final int low;   // estremo sinistro
    public final int high;  // estremo destro

    // codizione iniziale: left <= right
    public Interval1D(int left, int right) {
        if (left <= right) {
            this.low  = left;
            this.high = right;
        }
        else throw new RuntimeException("Intervallo illegale");
    }

    // l'intervallo interseca l'intervallo that?
    public boolean intersects(Interval1D that) {
        if (that.high < this.low) return false;
        if (this.high < that.low) return false;
        return true;
    }

    //l'intervallo a interseca b?
    public boolean contains(int x) {
        return (low <= x) && (x <= high);
    }

    public int compareTo(Interval1D that) {
        if      (this.low  < that.low)  return -1;
        else if (this.low  > that.low)  return +1;
        else if (this.high < that.high) return -1;
        else if (this.high < that.high) return +1;
        else                            return  0;
    }

    public String toString() {
        return "[" + low + ", " + high + "]";
    }

}
