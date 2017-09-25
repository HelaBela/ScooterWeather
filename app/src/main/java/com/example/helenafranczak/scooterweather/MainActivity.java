package com.example.helenafranczak.scooterweather;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView speedText;
    TextView tempText;
    TextView conditionText;
    ImageView bike;
    ImageView weather;
    TextView warning;
    TextView quoteText;
    TextView city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        city = (TextView)findViewById(R.id.cityName);

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("name");
        city.setText(s);

//        if(b!=null)
//        {
//            String j =(String) b.get("name");
//            Textv.setText(j);
//        }



        quoteText=(TextView)findViewById(R.id.quote);
        speedText = (TextView) findViewById(R.id.speed);
        tempText = (TextView) findViewById(R.id.temp);
        conditionText = (TextView) findViewById(R.id.condition);

        bike = (ImageView) findViewById(R.id.bikeImg);
        weather = (ImageView) findViewById(R.id.weatherImg);

        warning = (TextView) findViewById(R.id.warning);

        try {
            String encodedCityName= URLEncoder.encode(city.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20%28select%20woeid%20from%20geo.places%281%29%20where%20text%3D%22"+encodedCityName+"%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather ", Toast.LENGTH_LONG).show();

        }

        DownloadQuote quote = new DownloadQuote();
        String result1=null;

        try {
            result1=  quote.execute("http://helenafranczak.com/app/").get();
            String[] splitResult1=result1.split("\\r?\\n");

//            Pattern p= Pattern.compile("<h4>(.*?)</h4>");
//            Matcher m = p.matcher(splitResult1[0]);

//            while(m.find()){
//
//                System.out.println(m.group(1));
//
//            }

            Random random = new Random();
            int choosenPosition=random.nextInt(splitResult1.length);

            quoteText.setText(splitResult1[choosenPosition]);


            //return splitResult1[choosenPoz];

//            Random random = new Random();
//            choosen = random.nextInt(sentences.size());
//
//
//            quoteText.setText(String.valueOf(choosen));




        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }



    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();


                InputStreamReader reader = new InputStreamReader(in);


                int data = reader.read();


                while (data != -1) {

                    char current = (char) data;


                    result += current;


                    data = reader.read();


                }

                return result;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject queryResult = jsonObject.getJSONObject("query");
                JSONObject resultsResult = (JSONObject) queryResult.get("results");
                JSONObject channelResult = (JSONObject) resultsResult.get("channel");
                JSONObject windResult = (JSONObject) channelResult.get("wind");

                String speed = "";
                String temp = "";

                speed = windResult.getString("speed");
                temp = windResult.getString("chill");

                Long speedkm = Math.round(Integer.parseInt(speed) * 1.60934);

                Long tempC = Math.round((Integer.parseInt(temp) - 32) * 0.555);

                speedText.setText("wind speed: " + String.valueOf(speedkm) + " km/h");
                tempText.setText("wind temp: " + String.valueOf(tempC) + " C");


                if (speedkm <= 25) {

                    //not ideal, not too bad

                    bike.setImageResource(R.drawable.ic_motorcycle_white_48dp);
                    warning.setText("enjoy");

                } else if (speedkm > 25 && speedkm < 32) {

                    //not ideal, not too bad

                    bike.setImageResource(R.drawable.ic_motorcycle_white_48dp);
                    warning.setText("not ideal");

                } else if (speedkm >= 32 && speedkm < 40) {

                    //watch out

                    warning.setText("watch out");
                    bike.setImageResource(R.drawable.ic_motorcycle_white_48dp);

                } else if (speedkm >= 40 && speedkm < 48) {

                    warning.setText("windy");

                    bike.setImageResource(R.drawable.ic_toys_white_24dp);

                    //no bike

                } else if (speedkm >= 48 && speedkm < 56) {

                    bike.setImageResource(R.drawable.ic_error_outline_white_48dp);

                    warning.setText("heavy struggle");

                } else if (speedkm >= 56) {

                    bike.setImageResource(R.drawable.ic_error_outline_white_48dp);

                    warning.setText("don't chance it");

                }


                JSONObject itemResult = (JSONObject) channelResult.get("item");

                JSONArray forecastArray = itemResult.getJSONArray("forecast");

                for (int i = 0; i < forecastArray.length(); i++) {

                    JSONObject text = forecastArray.getJSONObject(0);

                    String condition = "";

                    condition = text.getString("text");

                    conditionText.setText(condition);

                    if (condition.contains("Cloudy") || condition.contains("cloudy") || condition.contains("cloud") || condition.contains("cloudy") || condition.contains("foggy")) {

                        weather.setImageResource(R.drawable.ic_cloud_queue_white_48dp);

                    } else if (condition.contains("sunny") || condition.contains("sun") || condition.contains("Sun") || condition.contains("Sunny") || condition.contains("clear") || condition.contains("Clear")) {

                        weather.setImageResource(R.drawable.ic_wb_sunny_white_48dp);

                    } else if (condition.contains("rain") || (condition.contains("rainy")) || condition.contains("Rainy") || condition.contains("rain") || condition.contains("Hail") || condition.contains("hail")
                            || condition.contains("showers") || condition.contains("Showers") || condition.contains("drizzle") || condition.contains("Drizzle") || condition.contains("storm") || condition.contains("Storm")
                            || condition.contains("snow") || condition.contains("Snow")) {

                        weather.setImageResource(R.drawable.ic_grain_white_48dp);

                    } else if (condition.contains("hot") || condition.contains("Hot")) {

                        weather.setImageResource(R.drawable.ic_whatshot_white_24dp);


                    } else if (condition.contains("thunderstorms") || condition.contains("Thunderstorms")) {

                        weather.setImageResource(R.drawable.ic_flash_on_white_24dp);

                    } else if (condition.contains("widny") || condition.contains("Windy")) {

                        weather.setImageResource(R.drawable.ic_toys_white_24dp);


                    } else {

                        weather.setImageResource(R.drawable.ic_sentiment_very_satisfied_white_48dp);
                    }


                }


                //Log.i("website", forecastArray.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public class DownloadQuote extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            String result1 = "";
            URL url2;


            HttpURLConnection urlConnection2 = null;


            try {

                url2 = new URL(urls[0]);


                urlConnection2 = (HttpURLConnection) url2.openConnection();

                InputStream in2 = urlConnection2.getInputStream();

                InputStreamReader reader2 = new InputStreamReader(in2);


                int data2 = reader2.read();

                while (data2 != -1) {

                    char current2 = (char) data2;

                    result1 += current2;

                    data2 = reader2.read();

                }

                return result1;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }

            return null;
        }

//        @Override
//        protected void onPostExecute(String quote) {
//            super.onPostExecute(quote);
//
//
//
//
//        }


    }

}
