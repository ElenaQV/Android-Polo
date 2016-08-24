package equitnero.utng.edu.mx.crudremotedb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity{

    Button btnViewFrases;
    Button btnNewFrase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);


        btnViewFrases = (Button) findViewById(R.id.btnViewFrases);
        btnNewFrase = (Button) findViewById(R.id.btnNewFrase);


        btnViewFrases.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AllFrasesActivity.class);
                startActivity(i);

            }
        });


        btnNewFrase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), NewFraseActivity.class);
                startActivity(i);

            }
        });
    }
}