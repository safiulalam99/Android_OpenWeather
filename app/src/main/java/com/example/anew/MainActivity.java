package com.example.anew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private RequestQueue queue; //= Volley.newRequestQueue(this);
    Location location;
    String url, str;
    TextView tvLat, tvLong;
    double longitude;
    double latitude;
    String weatherDescription ;
    int temperature;
    double windspeed ;
    String locationName ;
    String url1 = "https://api.openweathermap.org/data/2.5/find?q=";
    String url2 = "&units=metric&appid=863c718b89bcf17e9c84a17bfcf0f18b";
    EditText userLocationInput;
    String mainUrl = "https://api.openweathermap.org/data/2.5/find?q=dhaka&units=metric&appid=863c718b89bcf17e9c84a17bfcf0f18b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inCreate method ot set up the gui
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView locationInput = (TextView) findViewById(R.id.userInput);
        tvLat = findViewById(R.id.latLong2);
        //instantiate the requests queue

        queue = Volley.newRequestQueue(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("lat",latitude);
        savedInstanceState.putDouble("long",longitude);
        savedInstanceState.putString("wDescription",weatherDescription);
        savedInstanceState.putString("wName",locationName);
        savedInstanceState.putInt("temp",temperature);
        savedInstanceState.putDouble("wind",windspeed);


    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null)
        {
            latitude = savedInstanceState.getDouble("lat");
            longitude = savedInstanceState.getDouble("long");
            windspeed = savedInstanceState.getDouble("wind");
            weatherDescription = savedInstanceState.getString("wDescription");
            locationName = savedInstanceState.getString("wName");
            temperature = savedInstanceState.getInt("temp");;
            TextView textview = findViewById(R.id.textViewName);
            textview.setText(locationName);
            TextView weatherdescriptionTextView =  findViewById(R.id.textViewWeatherDescription);
            weatherdescriptionTextView.setText(weatherDescription);
            TextView windspeedTextView = findViewById(R.id.TextViewWindspeed);
            windspeedTextView.setText(""+windspeed+"m/s");
            TextView temperatureTextView = findViewById(R.id.textViewTemperature);
            temperatureTextView.setText(""+temperature+ "C");
        }
        else
        {
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show();
        }
    }


    public void  updateWeather(View view)  {
        // Todo: gett he user input
        userLocationInput = (EditText)findViewById(R.id.userInput);
        str = userLocationInput.getText().toString().trim();
        url = url1+str+url2;

        //replaca rhtis with real data
        // 1, make weather reqw

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            response -> {
                //callback for successful request
//                Toast.makeText(this, response, Toast.LENGTH_LONG).show();

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

            temperature = weather.getJSONArray("list").getJSONObject(0).getJSONObject("main").getInt("temp");
            locationName = weather.getJSONArray("list").getJSONObject(0).getString("name");
            windspeed = weather.getJSONArray("list").getJSONObject(0).getJSONObject("wind").getDouble("speed");
            weatherDescription = weather.getJSONObject("list").getJSONArray("weather").getJSONObject(0).getString("description");

        }catch (JSONException e){
            e.printStackTrace();
        }
        tempData();


    }

    public void tempData(){
        //name
        TextView weathertextViewName=  findViewById(R.id.textViewName);
        weathertextViewName.setText(""+locationName);

        // weather description
        TextView weatherdescriptionTextView =  findViewById(R.id.textViewWeatherDescription);
        weatherdescriptionTextView.setText(""+weatherDescription+"");
        // temp
        TextView temperatureTextView = findViewById(R.id.textViewTemperature);
        temperatureTextView.setText(""+temperature+ "C");
        //wind
        TextView windspeedTextView = findViewById(R.id.TextViewWindspeed);
        windspeedTextView.setText(""+windspeed+"m/s");
    }

    public void startGPS(View view) {
        //todo: start listening to users location through the Location manager
        // Todo 1: -location permission specified in android manifest file
        // Todo 2: ask users permission run-time before accessing GPS (dangerous permission)
        // Todo : Update the lat and ling in the UI

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Todo : check if user has granted permiaaion for gps
        // todo if not, prompt the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: We dont have permission so ask it here
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    0
            );
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 0,  this);
//            double latitude = lastknownLocation.getLatitude();

            if(latitude!=0.0 && longitude!=0.0){
                Intent intent = new Intent(this, forecastActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putDouble("Lat", latitude);
                bundle.putString("lat", String.valueOf(latitude));
                bundle.putString("long", String.valueOf(longitude));


                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Fetching..", Toast.LENGTH_SHORT).show();
            }
    }

//    @Override
    public void onLocationChanged( Location location) {
        // we now can read the lat and long from the "location" parameter
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //
        //Todo : update UI
        TextView gpsTextView = (TextView) findViewById(R.id.latLong);
        gpsTextView.setText("Lat: "+latitude + "\nLong: "+longitude);
    }


}
