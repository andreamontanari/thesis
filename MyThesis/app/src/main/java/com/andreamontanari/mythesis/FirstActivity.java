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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        final GoogleMap map = mapFragment.getMap();
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
        new InsertAsyncTask().execute("http://alwaysdreambig.altervista.org/insert.php");

        /********************************************
         *Richiedo icone da inserire al Server
         *********************************************/
        new HttpAsyncTask().execute("http://alwaysdreambig.altervista.org/request.php");

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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    //invia coordinate correnti al server
    public class InsertAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return INSERT(urls[0]);
        }
    }

    public String INSERT(String url) {
        /*
        name = edname.getText().toString();
        surname = edsurname.getText().toString();
        mail = edmail.getText().toString();
        lastpost = edlastpost.getText().toString();
        lat = edlat.getText().toString();
        lng = edlng.getText().toString();
        id = edid.getText().toString();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("surname", surname));
        nameValuePairs.add(new BasicNameValuePair("mail", mail));
        nameValuePairs.add(new BasicNameValuePair("lastpost", lastpost));
        nameValuePairs.add(new BasicNameValuePair("lat", lat));
        nameValuePairs.add(new BasicNameValuePair("long", lng));
        nameValuePairs.add(new BasicNameValuePair("id", id));
       */
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid Host Address",
                    Toast.LENGTH_LONG).show();
        }
        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {

            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
                //Toast.makeText(getBaseContext(), "Inserted Successfully",
                //Toast.LENGTH_SHORT).show();
                Log.d("PROVA", "inserted");
            } else {
                //Toast.makeText(getBaseContext(), "Sorry, Try Again",
                //  Toast.LENGTH_LONG).show();
                Log.d("PROVA", "not inserted, try again");
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
        return "Okay";
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

