package com.andreamontanari.mythesis;

import com.andreamontanari.mythesis.util.SystemUiHider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.List;


/**
 * Prima demo dell'esperimento - caso semplice con un solo attributo di rilevanza (no algoritmi)
 *
 * @see SystemUiHider
 */
public class FirstActivity extends Activity {

    JSONArray jsonArray;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    LatLng latlng;
    LatLng coords;
    private Marker myMarker;
    private GoogleMap map;
    int numIcons = 100;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");


        /*********************************
         *recupero la posizione dell'utente
         *********************************/

        LatLng myPosition = new LatLng(46.0809952,13.2136444);

        //inserisco il marker dell'utente e muovo la camera sul punto trovato
        final Marker myMarker = map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Position"));
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18)); //muovo la camera alla posizione latlng indicata (livello zoom 18 o 19)
        map.moveCamera( CameraUpdateFactory.newLatLngZoom(myPosition , 19));
        /********************************************
         *Richiedo icone da inserire al Server
         *Inserisco posizioni scaricate dal server
         *****************************************/

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tesi");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    // your logic here
                    for (ParseObject po : parseObjects) {

                        String id = String.valueOf(po.getInt("ID"));
                        String name = po.getString("Nome");
                        String surname = po.getString("Cognome");
                        String lat = po.getString("Latitudine");
                        String lng = po.getString("Longitudine");
                        String immagine = po.getString("Immagine"); //da aggiungere
                        String amici = String.valueOf(po.getInt("Amici"));

                        Double lt = Double.parseDouble(lat);
                        Double ln = Double.parseDouble(lng);

                        latlng = new LatLng(lt, ln);

                        if (amici.equals("1")) {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(name + " " + surname)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.friends))); //amico
                        } else {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(name + " " + surname)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.notfriends))); //non amico
                        }
                    }
                    Toast.makeText(FirstActivity.this,  numIcons+" users displayed out of "+ numIcons,Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(FirstActivity.this, "Si è verificato un errore nella ricezione dei dati, riprovare", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
           // mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
