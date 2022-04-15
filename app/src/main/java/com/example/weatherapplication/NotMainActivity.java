package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotMainActivity extends AppCompatActivity {

    private TextInputEditText editText;
    private TextView temperatureText;
    private TextView pressureText;
    private TextView humidityText;
    private TextView windText;
    private TextView clearText;
    private TextView feelsLikeText;
    private Button button;

    String API_key = "319cc31f4a6c2714bf9b3b69f384390f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.text_field);
        temperatureText = findViewById(R.id.temperature);
        pressureText = findViewById(R.id.pressure);
        humidityText = findViewById(R.id.humidity);
        windText = findViewById(R.id.wind_num);
        feelsLikeText = findViewById(R.id.feels_like_num);
        clearText = findViewById(R.id.cloudy);

        //intent information
        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            String initialCity = intent.getStringExtra(Intent.EXTRA_TEXT);
            editText.setText(initialCity);
            String site = "http://api.openweathermap.org/data/2.5/weather?q=" + initialCity + "&appid=" + API_key + "&units=metric&lang=eng";
            new GetData().execute(site);
        }

        //button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //warning
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(NotMainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
                } else {
                    //sending the request
                    String city = editText.getText().toString();
                    String site = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_key + "&units=metric&lang=eng";
                    new GetData().execute(site);
                }
            }
        });
    }

    private class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            //connection to the source
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                //processing the data
                InputStream stream = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //close all staff
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String TAG = "Result";
            Log.d(TAG, result);
            try {
                //creating JSONObjects
                JSONObject object = new JSONObject(result);
                JSONObject main = object.getJSONObject("main");
                JSONObject wind = object.getJSONObject("wind");
                JSONArray weather = object.getJSONArray("weather");
                JSONObject description = weather.getJSONObject(0);

                //getting data from JSON
                double temp = main.getDouble("temp");
                double press = main.getDouble("pressure");
                double hum = main.getDouble("humidity");
                double feels_like = main.getDouble("temp_min");
                double windNum = wind.getDouble("speed");
                String descriptionText = description.getString("description");

                //show of information
                temperatureText.setText(String.valueOf((int) Math.round(temp)));
                pressureText.setText(String.valueOf((int) Math.round(press)));
                humidityText.setText(String.valueOf((int) Math.round(hum)));
                feelsLikeText.setText(String.valueOf((int) Math.round(feels_like)));
                windText.setText(String.valueOf((int) Math.round(windNum)));
                clearText.setText(descriptionText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}