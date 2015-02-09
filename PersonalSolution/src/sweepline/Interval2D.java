package sweepline;


//Implementazione di un intervallo a 2 dimensioni con coordinate intere
public class Interval2D { 
    public final Interval1D intervalX;   // intervallo X
    public final Interval1D intervalY;   // intervallo y
    public final Point origin;
   
    public Interval2D(Interval1D intervalX, Interval1D intervalY, Point origin) {
        this.intervalX = intervalX;
        this.intervalY = intervalY;
        this.origin = origin;
    }

    // l'intervallo 2D a interseca b?
    public boolean intersects(Interval2D b) {
        if (!intervalX.intersects(b.intervalX)) return false;
        if (!intervalY.intersects(b.intervalY)) return false;
        return true;
    }

    // l'intervallo 2D contiene (x,y)?
    public boolean contains(int x, int y) {
        return intervalX.contains(x) && intervalY.contains(y);
    }

    public String toString() {
        return intervalX + " x " + intervalY;
    }

}
