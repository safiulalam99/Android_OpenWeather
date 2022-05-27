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

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements LocationListener{
    private RequestQueue  queue; //= Volley.newRequestQueue(this);
    Location location;
    String url,str;
    double longitude, latitude;
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
        //instantiate the requests queue

        queue = Volley.newRequestQueue(this);
    }

    public void fetchData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    //callback for successful request
//                Toast.makeText(this, response, Toast.LENGTH_LONG).show();

                    //1 . parse the data from json object
                    parseJsonAndUpdateUI(response);
                }, error -> {

            Toast.makeText(this, "some error", Toast.LENGTH_LONG).show();
            //callback for wheneevr something goes wring
        }
        );
        queue.add(stringRequest);
    }

    private void getLocation(){
        if(checkPermissions())
        {
            if(isLocationEnabled())
            {
                //FInal lat and long
//                fusedLocation
            }
            else
            {
               // open setting
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }
        else
        {
            //req permission again
            requestPermission();
    }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER );
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},100
        );
    }

    public boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        ==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openForecast(View view) {
        //opening forecast Button when clicked
        // 1. Create an intent for opening ForecastActiity
        Intent intent = new Intent(this, forecastActivity.class);
        // 2. send some data to the other activity with the intent

        // parameters: key for the data AND value  for the data
        // 3. start acticity through intent
        startActivity(intent);
    }

    public void  updateWeather(View view)  {
        // Todo: gett he user input
        userLocationInput = (EditText)findViewById(R.id.userInput);
        str = userLocationInput.getText().toString();
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

            Toast.makeText(this, "some error", Toast.LENGTH_LONG).show();
            //callback for wheneevr something goes wring
        }
        );
        queue.add(stringRequest);
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
        tempData(weatherDescription, temperature, windspeed);

    }

    public void tempData(String weatherDescription, double temperature, double windspeed){
        TextView weatherdescriptionTextView =  findViewById(R.id.textViewWeatherDescription);
        weatherdescriptionTextView.setText(weatherDescription);
        // temp
        TextView temperatureTextView = findViewById(R.id.textViewTemperature);
        temperatureTextView.setText(""+temperature+ "C");
        //wind
        TextView windspeedTextView = findViewById(R.id.TextViewWindspeed);
        windspeedTextView.setText(""+windspeed+"m/s");
    }

    public void startGPS() {
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
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    0
            );
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
//            double latitude = lastknownLocation.getLatitude();

    }
    public void loc( View view) {
        try {
            startGPS();
        }catch (Exception e){
            e.printStackTrace();
        }

        // we now can read the lat and long from the "location" parameter
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //
        //Todo : update UI
        TextView gpsTextView = (TextView) findViewById(R.id.latLong);
        gpsTextView.setText("Lat: "+latitude + "\nLong: "+longitude);
    }
    @Override
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
