package com.andreamontanari.mythesis.algorithm.sweepline;

public class Event implements Comparable<Event> {
    int time;
    Interval2D rect;
    Point p;

    public Event(int time, Interval2D rect, Point p) {
        this.time = time;
        this.rect = rect;
        this.p = p;
    }

    public int compareTo(Event that) {
        if      (this.time < that.time) return -1;
        else if (this.time > that.time) return +1;
        else                            return  0; 
    }

}