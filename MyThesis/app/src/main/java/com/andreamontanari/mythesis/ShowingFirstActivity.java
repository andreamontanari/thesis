package com.andreamontanari.mythesis;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by andreamontanari on 17/02/15.
 */
public class ShowingFirstActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing);

        // definisco un array di stringhe (nomi dei componenti del gruppo)
        String[] nameproducts = new String[] { "Product1", "Product2", "Product3" };

        // definisco un ArrayList
        final ArrayList<String> listp = new ArrayList<String>();
        for (int i = 0; i < nameproducts.length; ++i) {
            listp.add(nameproducts[i]);
        }
        // recupero la lista dal layout
        final ListView mylist = (ListView) findViewById(R.id.listView);

        // creo e istruisco l'adattatore
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SecondActivity.aggregated);

        // inietto i dati
        mylist.setAdapter(adapter);
    }
}
