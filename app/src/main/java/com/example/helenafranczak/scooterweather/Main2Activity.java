package com.example.helenafranczak.scooterweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Main2Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        final EditText name= (EditText) findViewById(R.id.cityName);
        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Log.i("citi", name.getText().toString());
               Intent ii = new Intent(Main2Activity.this,MainActivity.class);
              ii.putExtra("name",name.getText().toString());
              startActivity(ii);
            }
        });



    }
}