package com.andreamontanari.mythesis;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by andreamontanari on 09/02/15.
 */
public class TryActivity extends FragmentActivity {

    static double  latitude, longitude;

    Bitmap bitmap, fin;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng latlng;
    GPSTracker gps;
    private Marker myMarker;

    TextView tname, tlastpost, tage;

    Marker loadmk;
    View load, show;
    //map async
    String lat, lng, email, picture, dataora;

    //user async
    String userid, useremail, username, usersurname, userlastPost, usersex, userbirthday, userpicture;

    JSONArray jsonUser = null;
    JSONArray jsonArray = null;
    Bitmap icon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setUpMapIfNeeded();

        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = (size.y)-2*(size.x/4)+120;

        findViewById(R.id.map).getLayoutParams().height = height;
        */

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            mMap = mapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                try {
                    setUpMap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() throws IOException {



        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        //get the map
        mMap = mapFragment.getMap();

        //questa parte serve a catturare le coordinate gps dalla classe che usavo anche nella mia app
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {


            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String lat = Double.toString(latitude);
            String lon = Double.toString(longitude);

            latlng = new LatLng(latitude, longitude);
            Log.d("LAT", "Latitude: " + lat);
            Log.d("LONG", "Longitude: "+lon);

            //per mostrare il pallino blu
            //mMap.setMyLocationEnabled(true);

            //per il movimento della "telecamera", zoom in
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude,longitude), 18));

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bank);

            Bitmap fin = addWhiteBorder(bitmap);
            fin = addGreenBorder(fin);

            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("andrea.montanari92@gmail.com")
                    .icon(BitmapDescriptorFactory.fromBitmap(fin)));
        };

        new MapAsyncTask().execute("http://alwaysdreambig.altervista.org/ubi/requestMappa.php");
    }


    private Bitmap addBlueBorder (Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLUE);
        p.setStrokeWidth(9);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }

    private Bitmap addGreenBorder (Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);
        p.setStrokeWidth(9);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }

    private Bitmap addWhiteBorder (Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(9);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }
    //USELESS
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
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

    public class MapAsyncTask extends AsyncTask<String, Void, String> implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
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


                    lat = jsonObject.getString("Latitudine");
                    lng = jsonObject.getString("Longitudine");
                    email = jsonObject.getString("Email");
                    picture = jsonObject.getString("ProfilePic");
                    dataora = jsonObject.getString("UltimoAccesso");

                    Log.d("OGGETTO", Integer.toString(i));
                    Log.d( "lat, long:", lat + "  " + lng);
                    Log.d("mail", email);
                    Log.d("picture", picture);
                    Log.d("ultimo accesso", dataora);


                    Double lt = Double.parseDouble(lat);
                    Double ln = Double.parseDouble(lng);
                    latlng = new LatLng(lt, ln);


                    mMap.setOnMarkerClickListener(this);

                    mMap.setOnMapClickListener(this);

                    new ImageDownloader().execute(picture);

                    while (icon == null) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Bitmap fin = addWhiteBorder(icon);
                    saveToInternalStorage(fin, email);

                    fin = addBlueBorder(fin);

                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(email)
                            .icon(BitmapDescriptorFactory.fromBitmap(fin)));

                    icon = null;

                };

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        public boolean onMarkerClick(final Marker marker) {

            //recupero la mail del marker
            String ti = marker.getTitle();
            //prendo le info utente dalla tabella UTENTI
            //new UserAsyncTask().execute(ti);

            Projection projection = mMap.getProjection();
            LatLng markerLocation = marker.getPosition();
            Point screenPosition = projection.toScreenLocation(markerLocation);


            int zoom = (int) mMap.getCameraPosition().zoom;
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude + (double)90/Math.pow(2, zoom), marker.getPosition().longitude), zoom);

            mMap.moveCamera(cu);

            projection = mMap.getProjection();
            markerLocation = marker.getPosition();
            screenPosition = projection.toScreenLocation(markerLocation);

            int x = screenPosition.x - 450;
            int y = screenPosition.y - 950;

            //AGGIORNARE CON ICONA CLICCATA (salvare img una volta sola e riprenderla poi)
            ImageView img = (ImageView) findViewById(R.id.imageView);
            /*
            load = (View) findViewById(R.id.load);
            load.setX(x);
            load.setY(y);
            load.setVisibility(View.VISIBLE);
            loadmk = marker;
               */
            return true;
        }

        @Override
        public void onMapClick(LatLng latLng) {

        }

    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
        }

        private Bitmap downloadBitmap(String url) {
            // initialize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        icon = Bitmap.createScaledBitmap(bitmap, 150,150,false);
                        Log.d("PROVA", "icon creato");
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }
    }
    public String GETS(String email) {

        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            Log.d("CLIENT", httpclient.toString());

            String emailValue = URLEncoder.encode(email, "UTF-8");

            String URL = "http://alwaysdreambig.altervista.org/requestUtenti.php?Email="+emailValue;

            HttpGet httpGet = new HttpGet(URL);

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);
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


    public class UserAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... email) {

            return GETS(email[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String jsonString = result;

            try {
                //Turn the JSON string into an array of JSON objects
                jsonUser = new JSONArray(jsonString);
                JSONObject jsonObject = null;

                jsonObject = jsonUser.getJSONObject(0);

                userid = jsonObject.getString("ID");
                useremail = jsonObject.getString("Email");
                username = jsonObject.getString("Nome");
                usersurname = jsonObject.getString("Cognome");
                userlastPost = jsonObject.getString("LastPost");
                usersex = jsonObject.getString("Sesso");
                userpicture = jsonObject.getString("ProfilePic");
                userbirthday = jsonObject.getString("DataNascita");

                tname.setText(username+" "+usersurname);
                tlastpost.setText(userlastPost);
                tage.setText(userbirthday);
                showView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showView() {
        load.setVisibility(View.INVISIBLE);
        Projection projection = mMap.getProjection();
        LatLng markerLocation = loadmk.getPosition();
        Point screenPosition = projection.toScreenLocation(markerLocation);


        int zoom = (int) mMap.getCameraPosition().zoom;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(loadmk.getPosition().latitude + (double)90/Math.pow(2, zoom), loadmk.getPosition().longitude), zoom);

        mMap.moveCamera(cu);

        projection = mMap.getProjection();
        markerLocation = loadmk.getPosition();
        screenPosition = projection.toScreenLocation(markerLocation);

        int x = screenPosition.x - 450;
        int y = screenPosition.y - 950;

        //AGGIORNARE CON ICONA CLICCATA
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setImageBitmap(loadImageFromStorage(useremail));

        /*
        View vi = (View) findViewById(R.id.popup);
        vi.setX(x);
        vi.setY(y);
        vi.setVisibility(View.VISIBLE);
        */

    }

    private String saveToInternalStorage(Bitmap bitmapImage, String title){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,title+".png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("IMGSAVED", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String title)
    {
        Bitmap b = null;
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File f=new File(directory, title+".png");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }


    public void messageMe(View v) {
        Toast.makeText(this, "Message typing", Toast.LENGTH_SHORT).show();
    }

    public void pokeMe(View v) {
        Toast.makeText(this, "Poke sending", Toast.LENGTH_SHORT).show();
    }

}