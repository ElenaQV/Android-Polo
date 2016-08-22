package equitnero.utng.edu.mx.remotedb;


    import java.util.ArrayList;
    import org.apache.http.NameValuePair;
    import org.apache.http.message.BasicNameValuePair;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import android.app.Activity;
    import android.os.Bundle;
    import android.os.StrictMode;
    import equitnero.utng.edu.mx.remotedb.CustomHttpClient;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;


/**
 * Created by Elena on 22-Aug-16.
 */
    public class JSONUseActivity extends Activity {
    EditText byear;
    Button submit;
    TextView tv;

    String returnString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byear = (EditText) findViewById(R.id.editText1);
        submit = (Button) findViewById(R.id.submitbutton);
        tv = (TextView) findViewById(R.id.showresult);


        submit.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {


                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();


                postParameters.add(new BasicNameValuePair("birthyear",
                        byear.getText().toString()));
                String response = null;

                try {
                    response = CustomHttpClient.executeHttpPost(

                            "http://192.168.1.70/jsonscript.php",
                            postParameters);


                    String result = response.toString();


                    try{
                        returnString = "";
                        JSONArray jArray = new JSONArray(result);
                        for(int i=0;i<jArray.length();i++){
                            JSONObject json_data = jArray.getJSONObject(i);
                            Log.i("log_tag","id: "+json_data.getInt("id")+
                                    ", name: "+json_data.getString("name")+
                                    ", sex: "+json_data.getInt("sex")+
                                    ", birthyear: "+json_data.getInt("birthyear")
                            );

                            returnString += "\n" + json_data.getString("name") + " -> "+ json_data.getInt("birthyear");
                        }
                    }
                    catch(JSONException e){
                        Log.e("log_tag", "Error parsing data "+e.toString());
                    }

                    try{
                        tv.setText(returnString);
                    }
                    catch(Exception e){
                        Log.e("log_tag","Error in Display!" + e.toString());;
                    }
                }
                catch (Exception e) {
                    Log.e("log_tag","Error in http connection!!" + e.toString());
                }
            }
        });
    }
}

