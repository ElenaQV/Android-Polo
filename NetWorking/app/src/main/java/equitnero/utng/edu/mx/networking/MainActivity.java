package equitnero.utng.edu.mx.networking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import  javax.xml.parsers.DocumentBuilder;
import  javax.xml.parsers.DocumentBuilderFactory;
import  javax.xml.parsers.ParserConfigurationException;
import  org.w3c.dom.Document;
import  org.w3c.dom.Element;
import  org.w3c.dom.Node;
import  org.w3c.dom.NodeList;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView txtTexto;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          txtTexto =(TextView)findViewById(R.id.txt_text);
       new AccessWebServiceTask().execute("orange");
       // new DownloadText().execute("http://utng.edu.mx");
      //  ImageView img = (ImageView)findViewById(R.id.img);
       // new DownloadImageTask().execute("http://www.mayoff.com/5-01cablecarDCP01934.jpg");
    }
    private String wordDefinition(String word){
        InputStream in = null;
         String  strDefinition="";
        try{
            in = OpenHttpConnection("http://services.aonaware.com/DictService/DictService.asmx/Define?word="+word);
            Document doc = null;
            DocumentBuilderFactory dbf =  DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try{
               db = dbf.newDocumentBuilder();
               doc =  db.parse(in);
            }catch (ParserConfigurationException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();
            NodeList definitionElements = doc.getElementsByTagName("Definition");

            for(int i=0;i<definitionElements.getLength();i++){
                Node itemNode = definitionElements.item(i);
                if(itemNode.getNodeType()==Node.ELEMENT_NODE){
                    Element definitionElement = (Element)  itemNode;
                    NodeList wordDefinitionElements = (definitionElement).getElementsByTagName("WordDefinition");
                    for(int j = 0;j<wordDefinitionElements.getLength();j++){
                        Element wordDefinitionElement = (Element) wordDefinitionElements.item(j);
                        NodeList textNodes = ((Node)wordDefinitionElement).getChildNodes();
                        strDefinition+=  ((Node) textNodes.item(0)).getNodeValue()+".\n";

                    }
                }
            }

        }catch (IOException e1){
            Log.d("NetworkingActivity",e1.getLocalizedMessage());

        }
        return strDefinition;
    }

    private class AccessWebServiceTask extends  AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls) {
            return wordDefinition(urls[0]);
        }

        protected  void onPostExecute(String result){
            txtTexto.setText(result);
        }
    }

    private String downloadText(String url){
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try{
            in = OpenHttpConnection(url);
        }catch(IOException e){
            Log.d("Networking2",e.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str="";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try{
            while((charRead = isr.read(inputBuffer))>0){
                String reaString= String.copyValueOf(inputBuffer,0,charRead);
                str+= reaString;
                inputBuffer = new char[BUFFER_SIZE];
            }
        }catch(IOException e){
            Log.d("Networking",e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException{

        InputStream in = null;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                in = httpConn.getInputStream();
            }
        }
        catch(Exception ex){
            Log.d("Networking",ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }



    private Bitmap DownloadImage(String URL){
        Bitmap bitmap = null;
        try{
            InputStream in = OpenHttpConnection(URL);
            //Decode an input stream into a bitmap
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch(IOException e){
            Log.d("NetworkingActivity", e.getLocalizedMessage());
        }
        return bitmap;
    }

    private class DownloadText extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls) {
            return downloadText(urls[0]);
        }

        protected  void onPostExecute(String result){
            txtTexto.setText(result);
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            return DownloadImage(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = (ImageView)findViewById(R.id.img);
            imageView.setImageBitmap(result);
        }
    }
}
