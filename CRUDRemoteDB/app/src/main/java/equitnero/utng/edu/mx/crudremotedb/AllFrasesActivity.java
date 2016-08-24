package equitnero.utng.edu.mx.crudremotedb;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by Elena Quintero on 8/23/2016.
 */

public class AllFrasesActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> frasesList;

    // url to get all products list
    private static String url_all_frases = "http://192.168.1.70/frases/get_all_frases.php";
    // you can find it on local server.
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRASES = "frases";
    private static final String TAG_IDFRASE = "idFrase";
    private static final String TAG_AUTOR = "autor";

    // products JSONArray
    JSONArray frases = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_frases);

        // Hashmap for ListView
       frasesList = new ArrayList<HashMap<String, String>>();


        new LoadAllFrases().execute();

        // Get listview
        ListView lv = getListView();



        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.idFrase)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditFraseActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_IDFRASE, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllFrases extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllFrasesActivity.this);
            pDialog.setMessage("Cargando frases... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_frases, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Todos los productos: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    frases = json.getJSONArray(TAG_FRASES);


                    for (int i = 0; i < frases.length(); i++) {
                        JSONObject c = frases.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_IDFRASE);
                        String autor = c.getString(TAG_AUTOR);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_IDFRASE, id);
                        map.put(TAG_AUTOR, autor);

                        // adding HashList to ArrayList
                        frasesList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewFraseActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllFrasesActivity.this, frasesList,
                            R.layout.list_item, new String[] { TAG_IDFRASE,
                            TAG_AUTOR},
                            new int[] { R.id.idFrase, R.id.autor });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
