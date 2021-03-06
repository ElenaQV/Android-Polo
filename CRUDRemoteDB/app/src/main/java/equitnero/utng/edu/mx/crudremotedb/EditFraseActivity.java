package equitnero.utng.edu.mx.crudremotedb;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Elena Quintero on 8/23/2016.
 */

public class EditFraseActivity extends Activity {

    EditText txtAutor;
    EditText txtFrase;
    EditText txtTipoFrase;
    EditText txtRating;

    Button btnSave;
    Button btnDelete;

    String idFrase;


    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();


    private static final String url_frase_detials = "http://192.168.1.70/frases/get_frase_details.php";


    private static final String url_update_frase = "http://192.168.1.70/frases/update_frase.php";

    private static final String url_delete_frase = "http://192.168.1.70/frases/delete_frase.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FRASE = "frase";
    private static final String TAG_IDFRASE = "idFrase";
    private static final String TAG_AUTOR = "autor";
    private static final String TAG_FRASET = "frase";
    private static final String TAG_TIPOFRASE = "tipoFrase";
    private static final String TAG_RATING = "rating";

    JSONArray fraseObj = null;

    String autor ;
    String frase ;
    String tipoFrase ;
    String rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_frase);


        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);


        Intent i = getIntent();

        idFrase = i.getStringExtra(TAG_IDFRASE);

        new GetFraseDetails().execute();




        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new SaveFraseDetails().execute();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new DeleteFrase().execute();
            }
        });

    }


    class GetFraseDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFraseActivity.this);
            pDialog.setMessage("Cargando  los detalles de la frase");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idFrase", idFrase));


           final JSONObject json = jsonParser.makeHttpRequest( url_frase_detials, "GET", params);

            runOnUiThread(new Runnable() {
                public void run() {

                    int success;
                    try {


                        Log.d("Detalles frase", json.toString());


                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                             fraseObj = json.getJSONArray(TAG_FRASE); // JSON Array


                            JSONObject frase = fraseObj.getJSONObject(0);

                            txtAutor = (EditText) findViewById(R.id.inputAutor);
                            txtFrase = (EditText) findViewById(R.id.inputFrase);
                            txtTipoFrase= (EditText) findViewById(R.id.inputTipoFrase);
                            txtRating= (EditText) findViewById(R.id.inputRating);

                            txtAutor.setText(frase.getString(TAG_AUTOR));
                            txtFrase.setText(frase.getString(TAG_FRASET));
                            txtTipoFrase.setText(frase.getString(TAG_TIPOFRASE));
                            txtRating.setText(frase.getString(TAG_RATING));
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
        }
    }


    class SaveFraseDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFraseActivity.this);
            pDialog.setMessage("Guardando frase ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            autor = txtAutor.getText().toString();
            frase = txtFrase.getText().toString();
            tipoFrase = txtTipoFrase.getText().toString();
            rating = txtRating.getText().toString();
        }


        protected String doInBackground(String... args) {




            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_IDFRASE, idFrase));
            params.add(new BasicNameValuePair(TAG_AUTOR, autor));
            params.add(new BasicNameValuePair(TAG_FRASET, frase));
            params.add(new BasicNameValuePair(TAG_TIPOFRASE, tipoFrase));
            params.add(new BasicNameValuePair(TAG_RATING, rating));


            JSONObject json = jsonParser.makeHttpRequest(url_update_frase,
                    "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Intent i = getIntent();

                    setResult(100, i);
                    finish();
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
        }
    }


    class DeleteFrase extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditFraseActivity.this);
            pDialog.setMessage("Eliminado  frase...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {


            int success;
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("idFrase", idFrase));


                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_frase, "POST", params);


                Log.d("Delete Frase", json.toString());


                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    Intent i = getIntent();

                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

        }

    }
}