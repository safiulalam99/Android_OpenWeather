package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.w3c.dom.Text;

public class forecastActivity extends AppCompatActivity {
    private RequestQueue queue; //= Volley.newRequestQueue(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Read intent data if available//
        // if there is city name given. Read it and put it on screen
        // if string extra is noat there z`
        String cityName = getIntent().getStringExtra("CITY_NAME");
        // lets put the intent on the scREEN
        TextView weatherForecastCityName = findViewById(R.id.textViewForecastCityName);
        if(cityName != null) {
            weatherForecastCityName.setText(cityName);
        }else {
            weatherForecastCityName.setText("Helsinki");
        }
        }


    public void backButton(View view) {
            // Creating explicit intent
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
    }

    public void futureWeatherUpdates(String response){

    }
}
