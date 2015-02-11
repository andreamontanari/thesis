package com.andreamontanari.mythesis;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");

        ParseObject tesi = new ParseObject("Tesi");
        tesi.put("ID", 0);
        tesi.put("Nome", "Andrea");
        tesi.put("Cognome", "Montanari");
        tesi.put("Immagine", "https://graph.facebook.com/10152229194102933/picture?type=large");
        tesi.put("Latitudine", 45.9580284);
        tesi.put("Longitudine", 12.6673076);
        tesi.put("Amici", false);

        tesi.put("ID", 1);
        tesi.put("Nome", "Marco");
        tesi.put("Cognome", "Rossi");
        tesi.put("Immagine", "https://graph.facebook.com/10152229194102933/picture?type=large");
        tesi.put("Latitudine", 45.9581284);
        tesi.put("Longitudine", 12.7873076);
        tesi.put("Amici", false);
        tesi.saveInBackground();
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void begin(View view) {
        Intent i = new Intent(this, ChooseActivity.class);
        startActivity(i);
    }
}
