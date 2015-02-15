package com.andreamontanari.mythesis;

import java.util.List;

/**
 * Created by andreamontanari on 12/02/15.
 */
public class Person {
    String id;
    String nome;
    String cognome;
    String lat;
    String lng;
    String amici;
    String accuratezza;

    public Person(String id, String nome, String cognome, String lat, String lng, String amici, String accuratezza) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.lat = lat;
        this.lng = lng;
        this.amici = amici;
        this.accuratezza = accuratezza;
    }

    public int getFriends() {
        return Integer.parseInt(amici);
    }
    public String getCompleteName() {
        return nome+" "+cognome;
    }
    public int getAccuracy() { return Integer.parseInt(accuratezza); }

    public static Person getPersonById(String id, List<Person> people) {
        Person p = null;
        for (Person x : people) {
            if (x.id.equals(id)) {
                p = x;
                return p;
            }
        }
        return p;
    }
}
