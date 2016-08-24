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


    private ProgressDialog pDialog;


    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> frasesList;


    private static String url_all_frases = "http://192.168.1.70/frases/get_all_frases.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRASES = "frases";
    private static final String TAG_IDFRASE = "idFrase";
    private static final String TAG_AUTOR = "autor";


    JSONArray frases = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_frases);


       frasesList = new ArrayList<HashMap<String, String>>();


       new LoadAllFrases().execute();


        ListView lv = getListView();



        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String pid = ((TextView) view.findViewById(R.id.idFrase)).getText().toString();


                Intent in = new Intent(getApplicationContext(),
                        EditFraseActivity.class);

                in.putExtra(TAG_IDFRASE, pid);


                startActivityForResult(in, 100);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    class LoadAllFrases extends AsyncTask<String, String, String> {

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

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_all_frases, "GET", params);


            Log.d("Todas las frases: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    frases = json.getJSONArray(TAG_FRASES);


                    for (int i = 0; i < frases.length(); i++) {
                        JSONObject c = frases.getJSONObject(i);


                        String id = c.getString(TAG_IDFRASE);
                        String autor = c.getString(TAG_AUTOR);


                        HashMap<String, String> map = new HashMap<String, String>();


                        map.put(TAG_IDFRASE, id);
                        map.put(TAG_AUTOR, autor);


                        frasesList.add(map);
                    }
                } else {

                    Intent i = new Intent(getApplicationContext(),
                            NewFraseActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            AllFrasesActivity.this, frasesList,
                            R.layout.list_item, new String[] { TAG_IDFRASE,
                            TAG_AUTOR},
                            new int[] { R.id.idFrase, R.id.autor });

                    setListAdapter(adapter);
                }
            });

        }

    }
}
