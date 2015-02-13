package com.andreamontanari.mythesis;

import com.andreamontanari.mythesis.algorithm.aggregation.Aggregation;
import com.andreamontanari.mythesis.algorithm.aggregation.Element;
import com.andreamontanari.mythesis.algorithm.aggregation.Node;
import com.andreamontanari.mythesis.sweepline.Intersection;
import com.andreamontanari.mythesis.sweepline.Interval1D;
import com.andreamontanari.mythesis.sweepline.Interval2D;
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
 *Versione algoritmo server-side
 *
 *
 */
public class FourthActivity extends Activity {

    JSONArray jsonArray;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    LatLng coords;
    private Marker myMarker;
    public final int numIcons = 300;

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
    public Person[] people;

    View load;

    private static List<ParseObject>allObjects = new ArrayList<ParseObject>();

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

        setContentView(R.layout.activity_fourth);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        rects = new Interval2D[numIcons];
        points = new com.andreamontanari.mythesis.sweepline.Point[numIcons];
        people = new Person[numIcons];
        lats = longs = new double[numIcons];

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");

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

        load = (View) findViewById(R.id.load);
        load.setX(100);
        load.setY(200);
        load.setVisibility(View.VISIBLE);

        /********************************************
         *Richiedo icone da inserire al Server
         *Inserisco posizioni scaricate dal server
         *****************************************/
        final ParseQuery parseQuery = new ParseQuery("TesiReal");
        parseQuery.setLimit(numIcons);
        //parseQuery.whereEqualTo("Online", 1);
        parseQuery.findInBackground(getAllObjects());

    }

    FindCallback getAllObjects() {
        return new FindCallback() {
            @Override
            public void done(List list, com.parse.ParseException e) {
                if (e == null) {

                    allObjects.addAll(list);
                    int skip = 0;
                    int limit = 1000;
                    if (list.size() == limit) {
                        skip = skip + limit;
                        ParseQuery query = new ParseQuery("TesiReal");
                        query.setSkip(skip);
                        query.setLimit(limit);
                        query.findInBackground(getAllObjects());
                    }
                    //We have a full PokeDex
                    else {
                        //USE FULL DATA AS INTENDED
                        List<ParseObject> l = list;
                        for (ParseObject po : l) {
                                projection = map.getProjection();

                                String id = String.valueOf(po.getInt("ID"));
                                String name = po.getString("Nome");
                                String surname = po.getString("Cognome");
                                String lat = po.getString("Latitudine");
                                String lng = po.getString("Longitudine");
                                //String immagine = po.getString("Immagine"); //da aggiungere
                                String accuracy = String.valueOf(po.getInt("Accuratezza"));
                                //String online = String.valueOf(po.getInt("Online"));
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
                                int xmax = xmin - 100;
                                int ymax = ymin - 100;

                                lats[Id] = lt;
                                longs[Id] = ln;

                                points[Id] = new com.andreamontanari.mythesis.sweepline.Point(xmax, ymax, id);
                                rects[Id] = new Interval2D(new Interval1D(xmax, xmin), new Interval1D(ymax, ymin), points[Id]);
                                people[Id] = new Person(name, surname, lat, lng, amici, accuracy);

                            }

                        Intersection.sweepline(numIcons, points, rects); //creo grafo dei conflitti


                        Q = new ArrayList<Node>();
                        ANS = new ArrayList<Node>();

                        //creo la lista di tutti i nodi Q con rilevanza stabilita in base all'amicizia, grado di sovrapposizione inizializzato a 0
                        for (int i=0; i<numIcons; i++) {
                            Q.add(new Node(points[i].getId(), points[i], people[i].getFriends(), 0, people[i].getAccuracy()));
                            ANS.add(new Node(points[i].getId(), points[i], people[i].getFriends(), 0, people[i].getAccuracy()));
                        }

                        F = new ArrayList<Element>();

                        F = Aggregation.realAggregation(1, 0, 50, ANS);

                        int count = 0;
                        map.clear();
                        for (Element ex : F) {
                            if (ex.show) {
                                count++;
                                //inserisco me stesso (meme)
                                LatLng coords = new LatLng(Double.parseDouble(people[Integer.parseInt(ex.id)].lat), Double.parseDouble(people[Integer.parseInt(ex.id)].lng));
                                if (!ex.aggregator) {
                                    map.addMarker(new MarkerOptions()
                                            .position(coords)
                                            .title(people[Integer.parseInt(ex.id)].getCompleteName())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.library)));    //singolo
                                } else {
                                    //inserisco l'aggregatore (quadrifoglio per ora)
                                    map.addMarker(new MarkerOptions()
                                            .position(coords)
                                            .title("Gruppo")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar))); //gruppo
                                }
                            }
                        }
                        load.setVisibility(View.INVISIBLE);
                        Toast.makeText(FourthActivity.this, count+" elementi mostrati su "+ numIcons +" online",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
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
