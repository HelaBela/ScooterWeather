package com.example.helenafranczak.scooterweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helenafranczak on 8/6/17.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {


    public WeatherAdapter(Context context, List<Weather> weather){

        super(context, 0 , weather);
    }


    @Override

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView=convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }



        Weather currentWeather= getItem(position);

        TextView tempView= (TextView)listItemView.findViewById(R.id.temp);
        tempView.setText(currentWeather.getTemperature());

        TextView speedView= (TextView)listItemView.findViewById(R.id.speed);
        speedView.setText(currentWeather.getSpeed());

        TextView conditionView= (TextView)listItemView.findViewById(R.id.condition);
        conditionView.setText(currentWeather.getCondition());



        return listItemView;

    }

    }






