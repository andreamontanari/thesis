package com.andreamontanari.mythesis;

import com.andreamontanari.mythesis.algorithm.aggregation.Aggregation;
import com.andreamontanari.mythesis.algorithm.aggregation.Element;
import com.andreamontanari.mythesis.algorithm.aggregation.Node;
import com.andreamontanari.mythesis.algorithm.sweepline.ConflictGraph;
import com.andreamontanari.mythesis.algorithm.sweepline.Intersection;
import com.andreamontanari.mythesis.algorithm.sweepline.Interval1D;
import com.andreamontanari.mythesis.algorithm.sweepline.Interval2D;
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
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *Seconda demo dell'esperimento - caso semplice con un solo attributo di rilevanza (versione algoritmo client-side)
 *
 *
 */
public class SecondActivity extends Activity implements GoogleMap.OnMarkerClickListener {

    public final int numIcons = 100;

    LatLng latlng;
    private GoogleMap map;
    Projection projection;
    double[] lats, longs;
    Point screenPosition;
    View load;

    //sweepline
    public Interval2D[] rects;
    public com.andreamontanari.mythesis.algorithm.sweepline.Point[] points;

     //min-max aggregation
    public static List<Element> F;
    public static List<Node> ANS;
    public static List<Node> Q;
    public Person[] people;

    //onmarkerclick
    public static List<String> aggregated;
    public List<Person> persons;

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

        //carico la struttura statica dell'interfaccia dal file conenuto in res/layout/activity_second.xml
        setContentView(R.layout.activity_second);

        //recupero il mapfragment che contiene l'oggetto GoogleMap
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        //inizializzo la libreria Parse
        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");

        //dichiarazione delle liste ausiliarie
        rects = new Interval2D[numIcons];
        points = new com.andreamontanari.mythesis.algorithm.sweepline.Point[numIcons];
        people = new Person[numIcons];
        lats = longs = new double[numIcons];

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

        //aggiungo la finestra di caricamento fino a quando la computazione non è terminata
        load = (View) findViewById(R.id.load);
        load.setX(100);
        load.setY(200);
        load.setVisibility(View.VISIBLE);

        //eseguo la query (SELECT * FROM Tesi)
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tesi");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject po : parseObjects) {
                        //recupero la proiezione della mappa sul display fisico del display
                        projection = map.getProjection();

                        //recupero tutti gli attributi degli utenti caricati in Tesi
                        String id = String.valueOf(po.getInt("ID"));
                        String name = po.getString("Nome");
                        String surname = po.getString("Cognome");
                        String lat = po.getString("Latitudine");
                        String lng = po.getString("Longitudine");
                        String amici = String.valueOf(po.getInt("Amici"));

                        Double lt = Double.parseDouble(lat);
                        Double ln = Double.parseDouble(lng);
                        int Id = Integer.parseInt(id);

                        //inizializzo l'oggetto LatLng con le coordinate geografiche dell'utente trovato
                        latlng = new LatLng(lt, ln);

                        if (amici.equals("1")) {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.friends))); //amico
                        } else {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.notfriends))); //non amico
                        }

                        //recupero le coordinate spaziali relative al display del punto sulla mappa
                        screenPosition = projection.toScreenLocation(latlng);

                        int xmin = screenPosition.x;
                        int ymin = screenPosition.y;
                        int xmax = xmin - 100;
                        int ymax = ymin - 100;

                        //inizializzo gli array ausiliari per i due algoritmi
                        lats[Id] = lt;
                        longs[Id] = ln;
                        points[Id] = new com.andreamontanari.mythesis.algorithm.sweepline.Point(xmax, ymax, id);
                        rects[Id] = new Interval2D(new Interval1D(xmax, xmin), new Interval1D(ymax, ymin), points[Id]);
                        people[Id] = new Person(id, name, surname, lat, lng, amici, "");
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "Si è verificato un errore nella ricezione dei dati, riprovare", Toast.LENGTH_SHORT).show();
                }
                long starts = System.currentTimeMillis();

                Intersection.sweepline(numIcons, points, rects); //creo grafo dei conflitti

                long sweep = System.currentTimeMillis() - starts;
                Log.d("TEMPO sweep", "" + sweep);

                Q = new ArrayList<Node>();
                ANS = new ArrayList<Node>();

                //creo la lista di tutti i nodi Q con rilevanza stabilita in base all'amicizia, grado di sovrapposizione inizializzato a 0
                for (int i=0; i<numIcons; i++) {
                    Q.add(new Node(points[i].getId(), points[i], people[i].getFriends(), 0, 0));
                    ANS.add(new Node(points[i].getId(), points[i], people[i].getFriends(), 0, 0));
                }

                F = new ArrayList<Element>();

                long starta = System.currentTimeMillis();

                F = Aggregation.aggregation(1, 0, ANS);

                long time = System.currentTimeMillis() - starta;

                Log.d("TEMPO aggr", "" + time);
                int count = 0;
                map.clear();
                for (Element ex : F) {
                    map.setOnMarkerClickListener(SecondActivity.this);
                    if (ex.show) {
                        count++;
                        LatLng coords = new LatLng(Double.parseDouble(people[Integer.parseInt(ex.id)].lat), Double.parseDouble(people[Integer.parseInt(ex.id)].lng));
                        if (!ex.aggregator) { // se non è un aggregatore
                            map.addMarker(new MarkerOptions()
                                    .position(coords)
                                    .title(people[Integer.parseInt(ex.id)].getCompleteName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.friends)));    //singolo
                        } else { //se ex è un aggregatore
                            map.addMarker(new MarkerOptions()
                                    .position(coords)
                                    .title("Group:" + ex.id)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.group))); //gruppo
                        }
                    }
                }
                load.setVisibility(View.INVISIBLE); //nascondo la finestra di caricamento
                Toast.makeText(SecondActivity.this,  count+" users displayed out of "+ numIcons +" online",Toast.LENGTH_LONG).show();
            }

        });

        //aggiungo il punto in cui mi trovo
        myPosition = new LatLng(46.0809952,13.2136444);

        //inserisco il marker dell'utente e muovo la camera sul punto trovato
        final Marker Marker = map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("My Position"));


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

    //funzione del MarkerListener che prende i membri del gruppo per visualizzarli in una nuova activity (ShowingFirstActivity)
    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getTitle().startsWith("Group")) {
            aggregated = new ArrayList<String>();
            persons = new ArrayList<Person>();
            for (int i=0; i<people.length; i++) { //inizializzo persons con tutte le persone contenute nella lista people
                persons.add(people[i]);
            }
            String res = marker.getTitle();
            String[] splitted = res.split(":");
            int elid = Integer.parseInt(splitted[1]);
            List<Integer> adj = ConflictGraph.outEdges(elid); //recupero la lista di nodi adiacenti al nodo con id elid
            for (Integer i : adj) {
                String id = String.valueOf(i);
                aggregated.add(Person.getPersonById(id, persons).getCompleteName());
            }

            Intent i = new Intent(this, ShowingFirstActivity.class);
            startActivity(i);
        }
        return false;
    }
}
