package com.example.helenafranczak.scooterweather;

import android.content.Intent;

/**
 * Created by helenafranczak on 8/6/17.
 */

public class Weather {

    private Integer mTemp;

    private Integer mSpeed;

    private String mCondition;


    public Weather (Integer temperature, Integer speed, String condition){

        mTemp = temperature;
        mSpeed = speed;
        mCondition = condition;

    }


    public Integer getTemperature(){return mTemp;}

    public Integer getSpeed(){return mSpeed;}

    public String getCondition(){return mCondition;}


}
