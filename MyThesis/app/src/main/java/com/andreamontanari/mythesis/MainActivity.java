package com.andreamontanari.mythesis;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    String[] la,ln;
    String[] names = {"Andrea" ,"Teresa","Gianna", "Marco", "Paolo", "Anna", "Giorgia", "Susanna", "Antonia", "Gianni", "Beppe", "Carlo","Luca", "Giovanni", "Manuel", "Francesca", "Alice", "Gabriele", "Matteo", "Lucia", "Sara", "Melissa", "Alessandra", "Stefano"};
    String[] surnames = {"Verdi","Rossi","Bianchi","Santi","Montanari","Blu","Giannelli","Camilot","Burigat","De Toni","De Carli", "Dicante", "Meneghel", "Dal Mas", "Roncaglia", "Bet","Dovier", "Zuppi", "Kostner", "Giorgi", "De Paoli", "Turri", "Verga", "Totti"};

    public static String[] lats = new String[1000];
    public static String[] longs = new String[1000];

    public ParseObject tesiReal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //CODICE PER INIZIALIZZARE DB demo2
        getRandomPoints();


        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");

        //ParseObject tesi = new ParseObject("Tesi");

        for (int i=0; i<100; i++) {
            ParseObject tesi = new ParseObject("Tesi");
            tesi.put("ID", i);
            tesi.put("Nome", randomName());
            tesi.put("Cognome", randomSurname());
            tesi.put("Immagine", "test");
            tesi.put("Latitudine", la[i]);
            tesi.put("Longitudine", ln[i]);
            tesi.put("Amici", randomAmici());
            tesi.put("Online", randomAmici());
            tesi.put("Accuratezza", randomNumber());
            tesi.saveInBackground();
        }*/

        /*

        //CODICE PER INIZIALIZZARE DB demo3
        for (int i=0; i<1000; i++) {
            lats[i] = String.valueOf(RandomCoordinates.getLats(46.0809952, 13.2136444, 50));
            longs[i] = String.valueOf(RandomCoordinates.getLongs(46.0809952, 13.2136444, 50));
        }

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "NEmJNLg1Y5x6FEUfJiDOwVXEaSrOMPbew2jALpZ9", "iOetfxLpSERC1fDCqX6Uwgvw2Ps11ufpl2TYXaei");


        for (int i=200; i<300; i++) { //come sincronizzare il processo?
            tesiReal = new ParseObject("TesiReal");
            tesiReal.put("ID", i);
            tesiReal.put("Nome", randomName());
            tesiReal.put("Cognome", randomSurname());
            tesiReal.put("Immagine", "test");
            tesiReal.put("Latitudine", lats[i]);
            tesiReal.put("Longitudine", longs[i]);
            tesiReal.put("Amici", randomAmici());
            tesiReal.put("Online", "1");
            tesiReal.put("Accuratezza", randomNumber());
            tesiReal.saveInBackground();
        }
        */
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

    public void begin(View view) {
        Intent i = new Intent(this, ChooseActivity.class);
        startActivity(i);
    }

    public void getRandomPoints() {


        int num = 100;

        String lats, lngs;
        la = new String[num];
        ln = new String[num];

        String result = "46.0809952|46.08134602|46.08058279|46.08080286|46.08080528|46.08085993|46.08067451|46.08094018|46.08073293|46.08079757|46.0811066|46.08116229|46.08110751|46.08080667|46.08094233|46.08094082|46.08102267|46.08097284|46.08109269|46.08086825|46.08130317|46.08080561|46.08110614|46.08096441|46.08072265|46.08065554|46.08086865|46.08082356|46.08077267|46.08080274|46.0810343|46.08112215|46.08087687|46.08072326|46.0807381|46.08116883|46.08108258|46.08106341|46.08125647|46.08089031|46.0807093|46.0810336|46.08100405|46.08074377|46.08096565|46.08096122|46.08085607|46.08114108|46.08059722|46.0809031|46.08117256|46.08118138|46.08075717|46.08079677|46.08078883|46.0806565|46.081124|46.08088318|46.0806479|46.08111949|46.08099301|46.0807246|46.0812208|46.0810882|46.08095562|46.08095661|46.08074923|46.08140996|46.08086123|46.08072672|46.0808404|46.08072044|46.08074621|46.08100025|46.08122714|46.08064982|46.08074424|46.08072469|46.08072794|46.08077039|46.08077327|46.08118927|46.08106705|46.08098343|46.08118688|46.08109946|46.08075123|46.08056523|46.08109067|46.08082231|46.081257|46.08114751|46.08138523|46.08127097|46.0812756|46.08085958|46.08084309|46.08059893|46.08078814|46.08128452|46.08074836&13.213644400000021|13.21356374|13.21359427|13.21367666|13.21371541|13.21323511|13.21357598|13.21312219|13.21350822|13.2138338|13.213683|13.21329988|13.21379452|13.21311062|13.21367064|13.21395659|13.21411792|13.2136416|13.21380938|13.21342899|13.21402674|13.21420072|13.21346497|13.21328038|13.21363057|13.21335013|13.2132949|13.21351077|13.21317323|13.21389199|13.21359977|13.2137318|13.21389204|13.21401573|13.21312972|13.21410981|13.21400508|13.21377803|13.21372691|13.21306899|13.21336267|13.21355618|13.21310939|13.21368605|13.21389231|13.21368577|13.21354213|13.2138107|13.2138486|13.21378827|13.21351974|13.21400855|13.2139273|13.2138894|13.21372508|13.2138939|13.21421678|13.21373161|13.21400482|13.2135059|13.21327809|13.21369146|13.21404968|13.2136047|13.21375188|13.21401997|13.21339805|13.21364756|13.2131377|13.21338459|13.21373969|13.21330259|13.21408389|13.21412982|13.21345061|13.2138211|13.21365121|13.21320304|13.21374861|13.21363823|13.21409398|13.21330359|13.21379137|13.21341832|13.21391612|13.21356692|13.21328445|13.21360871|13.21314777|13.21353843|13.21321496|13.21379804|13.21337589|13.2141221|13.2140917|13.2140959|13.21356261|13.2135974|13.21410717|13.21316793|13.21401765&1";

        String[] params = result.split("\\&");
        lats = params[0];
        lngs = params[1];
        la = lats.split("\\|"); //lista di latitudini
        ln = lngs.split("\\|"); //lista di longitudini

        String circ = params[2];
    }

    public String randomName() {

        int x = (int) (names.length * Math.random());
        if (x == names.length) {
            return names[x-1];
        } else {
            return names[x];
        }
    }

    public String randomSurname() {

        int x = (int) (surnames.length * Math.random());
        if (x == surnames.length) {
            return surnames[x-1];
        } else {
            return surnames[x];
        }
    }

    public int randomAmici() {
        Random random = new Random();
        boolean b = random.nextBoolean();
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    public int randomNumber() {
        int x = (int) (100 * Math.random());
        return x;
    }
}
