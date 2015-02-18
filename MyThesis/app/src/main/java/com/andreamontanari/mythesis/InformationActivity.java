package com.andreamontanari.mythesis;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by andreamontanari on 18/02/15.
 */
public class InformationActivity extends Activity {

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

    public  void facebookMe(View v) {
        Context context = getApplicationContext();
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/andrea.montanari.9081"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/andrea.montanari.9081"));
            startActivity(intent);
        }
    }

    public void mailMe(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"andrea.montanari92@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, R.string.mailinfo);
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, "Mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InformationActivity.this, R.string.mailerr, Toast.LENGTH_SHORT).show();
        }
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

    public void toProducer(View v) {
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
