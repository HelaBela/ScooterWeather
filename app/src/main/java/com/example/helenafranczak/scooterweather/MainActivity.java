package com.example.helenafranczak.scooterweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** Adapter for the list of earthquakes */
   // private WeatherAdapter adapter;

    public static final String LOG_TAG = MainActivity.class.getName();

   // private static final String USGS_REQUEST_URL ="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22Sydney%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    /** Adapter for the list of earthquakes */
   // private WeatherAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Weather today= new Weather(15, 22, "sunny");

        ArrayList<Weather> weather = new ArrayList<>();
      weather.add( new Weather(15, 22, "sunny"));

        ListView weatherList = ( ListView) findViewById(R.id.list);

        WeatherAdapter adapter = new WeatherAdapter(this, weather);

        weatherList.setAdapter(adapter);

    }
}
