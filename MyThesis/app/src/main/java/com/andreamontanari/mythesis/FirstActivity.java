package com.andreamontanari.mythesis;

import android.app.Notification;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    LatLng coords;
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
       map.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18)); //move camera to the latLng position (18 or 19)

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
       // new HttpAsyncTask().execute("http://alwaysdreambig.altervista.org/thesis/request.php");

        /*****************************************
         *Inserisco posizioni scaricate dal server
         *****************************************/
        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tesi");
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    // object will be your row of Tesi
                    String name = parseObject.getString("Nome");
                    Log.d("NOME", name);
                } else {
                    // something went wrong
                    Log.d("NOME", "non trovato");
                }
            }
        });
*/
        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Tesi");
        query2.findInBackground(new FindCallback<ParseObject>() {
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
                        //String email = po.getString("Email");
                        String immagine = po.getString("Immagine");
                        String amici = String.valueOf(po.getInt("Amici"));

                        Log.d("id", id);
                        Log.d("nome", name);
                        Log.d("cognome", surname);
                        Log.d( "lat, long:", lat + "  " + lng);
                       // Log.d("mail", email);
                        Log.d("picture", immagine);
                        Log.d("picture", amici);

                        Double lt = Double.parseDouble(lat);
                        Double ln = Double.parseDouble(lng);

                        // getLocation(lt, ln, 0.005);

                        latlng = new LatLng(lt, ln);

                        if (amici.equals("1")) {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(name + " " + surname)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.quadrifoglio))); //amico
                        } else {
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(name + " " + surname)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.library))); //non amico
                        }
                    }
                } else {
                    // handle Parse Exception here
                }
            }
        });


        //RIVEDI
        Projection projection = map.getProjection();
        Point screenPosition = projection.toScreenLocation(new LatLng(46.0809952,13.2136444));
        Log.d("point screen", screenPosition.toString());
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

                    Double lt = Double.parseDouble(lat);
                    Double ln = Double.parseDouble(lng);

                   // getLocation(lt, ln, 0.005);

                    latlng = new LatLng(lt, ln);

                    myMarker = map.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name + " " + surname)
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

    public void getLocation(double x0, double y0, double radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        coords = new LatLng(foundLatitude, foundLongitude);

        System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );
    }

    /*
    public class UpdateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return UPDATE(urls[0]);
        }
    }

    public String UPDATE(String url) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lau)));
        nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(lnu)));
        Log.d("PROVA", id+ " " + lau +" " + lnu );

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("Update - pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Update - Fail 1", e.toString());
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
            Log.e("Update - pass 2", "connection success ");
        } catch (Exception e) {
            Log.e("Update - Fail 2", e.toString());
        }

        try {

            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
                //Toast.makeText(getBaseContext(), "Inserted Successfully",
                //Toast.LENGTH_SHORT).show();
                Log.d("PROVA", "Updated");
            } else {
                //Toast.makeText(getBaseContext(), "Sorry, Try Again",
                //  Toast.LENGTH_LONG).show();
                Log.d("PROVA", "not updated, try again");
            }
        } catch (Exception e) {
            Log.e("Update - Fail 3", e.toString());
        }
        new HttpAsyncTask().execute("http://alwaysdreambig.altervista.org/thesis/request.php");
        return "Okay";
    }
*/
}

