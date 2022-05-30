package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class forecastActivity extends AppCompatActivity {
    int temperature, pressure, humidity;
    double windspeed;
    String weatherDescription, locationName,feelW;


    private RequestQueue queue; //= Volley.newRequestQueue(this);

    String apiKey="863c718b89bcf17e9c84a17bfcf0f18b";
    String api1="https://api.openweathermap.org/data/2.5/weather?lat=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        queue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();
        String longitude = bundle.getString("long");
        String latitude = bundle.getString("lat");
        String main=api1+latitude+"&lon="+longitude+"&appid="+apiKey+"&units=metric";
        // lets put the intent on the scREEN
        TextView lat = findViewById(R.id.foreCastName);
        TextView lon = findViewById(R.id.forecastTemp);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, main,
                response -> {
                    //1 . parse the data from json object
                    parseJsonAndUpdateUI(response);
                }, error -> {

            Toast.makeText(this, "No location", Toast.LENGTH_LONG).show();
            //callback for wheneevr something goes wring
        }
        );
        queue.add(stringRequest);

        }

    private void parseJsonAndUpdateUI(String response) {

        //parse the data from api
        //1. Convert the response ro json objext
        try {
            JSONObject weather = new JSONObject(response);

//            temperature = weather.getJSONObject("main").getDouble("temp");

            temperature =  weather.getJSONObject("main").getInt("temp");
            locationName = weather.getString("name");
            feelW =  weather.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherDescription = weather.getJSONArray("weather").getJSONObject(0).getString("description");
            humidity =  weather.getJSONObject("main").getInt("humidity");
            pressure =  weather.getJSONObject("main").getInt("pressure");
            windspeed =  weather.getJSONObject("wind").getDouble("speed");

        }catch (JSONException e){
            e.printStackTrace();
        }
        tempData();
    }


    public void tempData(){
        //name
        TextView weathertextViewName=  findViewById(R.id.foreCastName);
        weathertextViewName.setText(""+locationName);

        // temp
        TextView temperatureTextView = findViewById(R.id.forecastTemp);
        temperatureTextView.setText(""+temperature+ "C");
        //wind
        TextView windspeedTextView = findViewById(R.id.forecastWind);
        windspeedTextView.setText("Wind: "+windspeed+"m/s");

        TextView DescriptionTextView = findViewById(R.id.forecastDescription);
        DescriptionTextView.setText(""+weatherDescription+"");

        TextView humid = findViewById(R.id.forecasthumid);
        humid.setText("Humidity: "+humidity+"");

        TextView presss = findViewById(R.id.forecastpressure);
        presss.setText("Pressure: "+pressure+"");

        TextView feelss = findViewById(R.id.feelsLike);
        feelss.setText(""+feelW+"");
    }

    public void backButton(View view) {
            // Creating explicit intent
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
    }

    public void futureWeatherUpdates(String response){

    }
}
