package com.andreamontanari.mythesis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Console;

/**
 * Created by andreamontanari on 09/02/15.
 */
public class ChooseActivity  extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void startDemo(View v) {
        Button b = (Button)v;
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
        }
    }

}