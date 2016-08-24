package equitnero.utng.edu.mx.crudremotedb;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
/**
 * Created by Elena Quintero on 8/23/2016.
 */
public class NewFraseActivity extends Activity {


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputAutor;
    EditText inputFrase;
    EditText inputTipoFrase;
    EditText inputRating;

    String autor;
    String frase;
    String tipoFrase;
    String rating;


    private static String url_create_frase = "http://192.168.1.70/frases/create_frase.php";


    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_frase);


        inputAutor = (EditText) findViewById(R.id.inputAutor);
        inputFrase = (EditText) findViewById(R.id.inputFrase);
        inputTipoFrase = (EditText) findViewById(R.id.inputTipoFrase);
        inputRating = (EditText) findViewById(R.id.inputRating);


        Button btnCreateFrase = (Button) findViewById(R.id.btnCreateFrase);


        btnCreateFrase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new CreateNewFrase().execute();
            }
        });
    }


    class CreateNewFrase extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             autor = inputAutor.getText().toString();
             frase = inputFrase.getText().toString();
             tipoFrase = inputTipoFrase.getText().toString();
             rating = inputRating.getText().toString();
            pDialog = new ProgressDialog(NewFraseActivity.this);
            pDialog.setMessage("Creando frase..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("autor", autor));
            params.add(new BasicNameValuePair("frase", frase));
            params.add(new BasicNameValuePair("tipoFrase", tipoFrase));
            params.add(new BasicNameValuePair("rating", rating));


            JSONObject json = jsonParser.makeHttpRequest(url_create_frase,
                    "POST", params);


            Log.d("Create Response", json.toString());


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Intent i = new Intent(getApplicationContext(), AllFrasesActivity.class);
                    startActivity(i);


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
}