package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.temporal.IsoFields;
import java.util.Scanner;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private RequestQueue  queue; //= Volley.newRequestQueue(this);
    String url = "https://api.openweathermap.org/data/2.5/find?q=dhaka&units=metric&appid=863c718b89bcf17e9c84a17bfcf0f18b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inCreate method ot set up the gui
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the requests queue

        queue = Volley.newRequestQueue(this);
    }


    public void openForecast(View view) {
        //opening forecast Button when clicked
        // 1. Create an intent for opening ForecastActiity
        Intent intent = new Intent(this, forecastActivity.class);
        // 2. send some data to the other activity with the intent

        // parameters: key for the data AND valiue  for the data
       // intent.putExtra("CITY_NAME", "Tampere");
        // 3. start acticity through intent
        startActivity(intent);
    }

    public void updateWeather(View view)  {

        //replaca rhtis with real data
        // 1, make weather reqw

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            response -> {
                //callback for successful request

                //1 . parse the data from json object
                parseJsonAndUpdateUI(response);
            }, error -> {

            Toast.makeText(this, "some error", Toast.LENGTH_LONG).show();
            //callback for wheneevr something goes wring
        }
        );
        queue.add(stringRequest);
        // 2.
//        String weatherDescription = "Cloudy";
//        float temp = 5.4f;
//        float windspeed = 2.0f;
//        TextView weatherdescriptionTextView =  findViewById(R.id.textViewWeatherDescription);
//        weatherdescriptionTextView.setText(weatherDescription);
//        // tyemp
//        TextView temperatureTextView = findViewById(R.id.textViewTemperature);
//        temperatureTextView.setText(""+temp+ "C");
//        TextView windspeedTextView = findViewById(R.id.TextViewWindspeed);
//        windspeedTextView.setText(""+windspeed+"m/s");

    }

    private void parseJsonAndUpdateUI(String response) {
        String weatherDescription ="cloudy";
        double temperature = 2;
        double windspeed = 3;
        String locationName = "";

        //parse the data from api
        //1. Convert the response ro json objext
        try {
            JSONObject weather = new JSONObject(response);
//            temperature = weather.getJSONObject("main").getDouble("temp");
            temperature = weather.getJSONArray("list").getJSONObject(0).getJSONObject("main").getDouble("temp");
            weatherDescription = weather.getJSONArray("list").getJSONObject(0).getString("name");
            locationName = weather.getJSONArray("list").getJSONObject(0).getString("name");
            windspeed = weather.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getDouble("speed");
        }catch (JSONException e){
            e.printStackTrace();
        }
        TextView weatherdescriptionTextView =  findViewById(R.id.textViewWeatherDescription);
        weatherdescriptionTextView.setText(weatherDescription);
        // temp
        TextView temperatureTextView = findViewById(R.id.textViewTemperature);
        temperatureTextView.setText(""+temperature+ "C");
        //wind
        TextView windspeedTextView = findViewById(R.id.TextViewWindspeed);
        windspeedTextView.setText(""+windspeed+"m/s");
        //
        TextView nameTextView = findViewById(R.id.textViewName);
        nameTextView.setText(""+locationName);
    }
}