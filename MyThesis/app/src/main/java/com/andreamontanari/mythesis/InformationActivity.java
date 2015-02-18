package com.andreamontanari.mythesis;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by andreamontanari on 18/02/15.
 */
public class InformationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    protected void onResume() {

        super.onResume();
    }

    public void visitMe(View v) {
        try{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://alwaysdreambig.altervista.org"));
            startActivity(browserIntent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.weberr1
                    + R.string.weberr2,  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void visitUniud(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.uniud.it/"));
            startActivity(browserIntent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.weberr1
                    + R.string.weberr2,  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void visitProducer(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.deltaaskii.com/web/"));
            startActivity(browserIntent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.weberr1
                    + R.string.weberr2,  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
