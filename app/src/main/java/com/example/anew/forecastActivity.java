package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class forecastActivity extends AppCompatActivity {
    private RequestQueue queue; //= Volley.newRequestQueue(this);
    String apiKey="863c718b89bcf17e9c84a17bfcf0f18b";
    String api1="https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        //Read intent data if available//
        // if there is city name given. Read it and put it on screen
        // if string extra is noat there z`
        Bundle bundle = getIntent().getExtras();
//        Double latitude = bundle.getDouble("lat");
        String longitude = bundle.getString("long");
        String latitude = bundle.getString("lat");
        // lets put the intent on the scREEN
        TextView lat = findViewById(R.id.foreCastName);
        TextView lon = findViewById(R.id.forecastTemp);
        if(true) {
            lat.setText(""+latitude);
            lon.setText(""+longitude);
        }else {
            lat.setText(""+"Error");
            lon.setText(""+"Error");
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
