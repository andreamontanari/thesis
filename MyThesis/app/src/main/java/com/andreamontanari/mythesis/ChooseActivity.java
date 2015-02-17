package com.andreamontanari.mythesis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by andreamontanari on 09/02/15.
 */

//attivita' nella quale l'utente sceglie quale demo avviare
public class ChooseActivity  extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentView(R.layout.activity_choose);

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

    //metodo che al click di uno dei 4 pulsanti avvia la demo selezionata
    public void startDemo(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        if (buttonText.equals("Demo1")) {
            Intent i = new Intent(this, FirstActivity.class);
            startActivity(i);
        } else if (buttonText.equals("Demo2")) {
            Intent i = new Intent(this, SecondActivity.class);
            startActivity(i);
        } else if (buttonText.equals("Demo3")) {
            Intent i = new Intent(this, ThirdActivity.class);
            startActivity(i);
        } else if (buttonText.equals("Demo4")) {
            Intent i = new Intent(this, FourthActivity.class);
            startActivity(i);
        }
    }

}