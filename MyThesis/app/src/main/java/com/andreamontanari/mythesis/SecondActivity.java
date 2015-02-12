package com.andreamontanari.mythesis;

import com.andreamontanari.mythesis.algorithm.aggregation.Aggregation;
import com.andreamontanari.mythesis.algorithm.aggregation.Element;
import com.andreamontanari.mythesis.algorithm.aggregation.Node;
import com.andreamontanari.mythesis.sweepline.Event;
import com.andreamontanari.mythesis.sweepline.Intersection;
import com.andreamontanari.mythesis.sweepline.Interval1D;
import com.andreamontanari.mythesis.sweepline.Interval2D;
import com.andreamontanari.mythesis.sweepline.IntervalST;
import com.andreamontanari.mythesis.sweepline.MinPQ;
import com.andreamontanari.mythesis.util.SystemUiHider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *Versione algoritmo client-side
 *
 *
 */
public class SecondActivity extends Activity {

    LatLng latlng;
    private GoogleMap map;
    Projection projection;
    double[] lats, longs;
    Point screenPosition;

    //sweepline
    public Interval2D[] rects;
    public com.andreamontanari.mythesis.sweepline.Point[] points;

     //min-max aggregation
    public static List<Element> F;
    public static List<Node> ANS;
    public static List<Node> Q;
    public int[] friends;

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

        setContentView(R.layout.activity_second);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");

        rects = new Interval2D[100];
        points = new com.andreamontanari.mythesis.sweepline.Point[100];
        friends = new int[100];
        lats = longs = new double[100];

        /*********************************
         *Inserisco posizione utente
         *********************************/

        //recupero la posizione dell'utente
        LatLng myPosition = new LatLng(46.0809952,13.2136444);

        //inserisco il marker dell'utente e muovo la camera sul punto trovato
        final Marker myMarker = map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Position"));
       // map.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19)); //move camera to the latLng position (18 or 19)
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
                        projection = map.getProjection();

                        String id = String.valueOf(po.getInt("ID"));
                        String name = po.getString("Nome");
                        String surname = po.getString("Cognome");
                        String lat = po.getString("Latitudine");
                        String lng = po.getString("Longitudine");
                        String immagine = po.getString("Immagine"); //da aggiungere
                        String amici = String.valueOf(po.getInt("Amici"));

                        Double lt = Double.parseDouble(lat);
                        Double ln = Double.parseDouble(lng);
                        int Id = Integer.parseInt(id);

                        latlng = new LatLng(lt, ln);

                        if (amici.equals("1")) {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.quadrifoglio))); //amico
                        } else {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.library))); //non amico
                        }

                        screenPosition = projection.toScreenLocation(latlng);

                        int xmin = screenPosition.x;
                        int ymin = screenPosition.y;
                        int xmax = xmin - 75;
                        int ymax = ymin - 75;

                        lats[Id] = lt;
                        longs[Id] = ln;


                        points[Id] = new com.andreamontanari.mythesis.sweepline.Point(xmax, ymax, id);
                        Log.d("PUNTO", points[Id].toString());
                        rects[Id] = new Interval2D(new Interval1D(xmax, xmin), new Interval1D(ymax, ymin), points[Id]);
                        friends[Id] = Integer.parseInt(amici);
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "Si è verificato un errore nella ricezione dei dati, riprovare", Toast.LENGTH_SHORT).show();
                }

                Intersection.sweepline(100, points, rects); //creo grafo dei conflitti


                Q = new ArrayList<Node>();
                ANS = new ArrayList<Node>();

                //creo la lista di tutti i nodi Q con rilevanza stabilita in base all'amicizia, grado di sovrapposizione inizializzato a 0
                for (int i=0; i<100; i++) {
                    Q.add(new Node(points[i].getId(), points[i], friends[i], 0));
                    ANS.add(new Node(points[i].getId(), points[i], friends[i], 0));
                }

                F = new ArrayList<Element>();

                F = Aggregation.aggregation(0, 0, ANS);

                int count = 0;
                map.clear();
                for (Element ex : F) {
                    if (ex.show) {
                        count++;
                        //inserisco me stesso (meme)
                        LatLng coords = getGeoCoords(new Point(ex.position.getX(), ex.position.getY()));
                        //LatLng coords = new LatLng(lats[Integer.parseInt(ex.position.getId())], longs[Integer.parseInt(ex.position.getId())]);
                        Log.d("LATLNG", coords.toString());
                        Log.d("PROVE di eq", ex.toString());
                        if (!ex.aggregator) {
                            map.addMarker(new MarkerOptions()
                                    .position(coords)
                                    .title(ex.id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.library))); //singolo
                        } else {
                            //inserisco l'aggregatore (quadrifoglio per ora)
                            map.addMarker(new MarkerOptions()
                                    .position(coords)
                                    .title("Gruppo")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar))); //gruppo
                        }
                    }
                }

                Log.d("FINE", "FINE, "+count+" elementi mostrati su 100");

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

    public LatLng getGeoCoords(Point screenPosition) {
        Projection projection = map.getProjection();

        return projection.fromScreenLocation(screenPosition);

    }
}
