package com.andreamontanari.mythesis;

import android.app.Notification;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by andreamontanari on 09/02/15.
 */
public class FirstActivity extends ActionBarActivity {

    JSONArray jsonArray;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    LatLng latlng;
    private Marker myMarker;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        /*********************************
        *Inserisco posizione utente
         *********************************/

        //recupero la posizione dell'utente
        LatLng myPosition = new LatLng(46.079816, 13.231234);

        //inserisco il marker dell'utente e muovo la camera sul punto trovato
        map.addMarker(new MarkerOptions()
                .position(myPosition)
                .title("Hello world"));
       //map.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19)); //move camera to the latLng position

        //add custom marker --> png file
         final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
         Marker melbourne = map.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bank)));

        /********************************************
        *Invio coordinate correnti al Server
        *********************************************/
        //new InsertAsyncTask().execute("http://alwaysdreambig.altervista.org/insert.php");

        /********************************************
         *Richiedo icone da inserire al Server
         *********************************************/
        new HttpAsyncTask().execute("http://alwaysdreambig.altervista.org/thesis/request.php");

        /*****************************************
         *Inserisco posizioni scaricate dal server
         *****************************************/

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            Log.d("CLIENT", httpclient.toString());

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            Log.d("RESPONSE", httpResponse.toString());
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.d("STREAM", inputStream.toString());
            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    //request icons class
    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            String jsonString = result;

            try {
                //Turn the JSON string into an array of JSON objects
                jsonArray = new JSONArray(jsonString);
                JSONObject jsonObject = null;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("ID");
                        String name = jsonObject.getString("Nome");
                        String surname = jsonObject.getString("Cognome");
                        String lat = jsonObject.getString("Latitudine");
                        String lng = jsonObject.getString("Longitudine");
                        String email = jsonObject.getString("Email");
                        String immagine = jsonObject.getString("Immagine");
                        String amici = jsonObject.getString("Amici");

                        Log.d("id", id);
                        Log.d("nome", name);
                        Log.d("cognome", surname);
                        Log.d( "lat, long:", lat + "  " + lng);
                        Log.d("mail", email);
                        Log.d("picture", immagine);
                        Log.d("picture", amici);

                    Double la = 46.079816;
                    Double lni = 13.231234;


                    Double lt = Double.parseDouble(lat);
                    Double ln = Double.parseDouble(lng);
                    latlng = new LatLng(lt, ln);

                    myMarker = map.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name+" "+surname)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.library)));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
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

}

