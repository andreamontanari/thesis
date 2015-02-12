package com.andreamontanari.mythesis;

/**
 * Created by andreamontanari on 12/02/15.
 */
public class Person {

    String nome;
    String cognome;
    String lat;
    String lng;
    String amici;

    public Person(String nome, String cognome, String lat, String lng, String amici) {
        this.nome = nome;
        this.cognome = cognome;
        this.lat = lat;
        this.lng = lng;
        this.amici = amici;
    }

    public int getFriends() {
        return Integer.parseInt(amici);
    }
    public String getCompleteName() {
        return nome+" "+cognome;
    }


}