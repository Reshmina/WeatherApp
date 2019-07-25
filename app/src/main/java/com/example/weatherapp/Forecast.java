package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Forecast extends AppCompatActivity {
    private String url="https://api.openweathermap.org/data/2.5/forecast?mode=json&appid=2215bb88ae43c1cd66e40339f8dd0fa6&q=";
    Context mcontext=this;
    TextView date, temperature, humidity, clouds, pressure, day, wind, city;
    String weekday;
    LinearLayout mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent intent = getIntent();
        final String str = intent.getStringExtra("Date");
        final String citySelected = (intent.getStringExtra("City")==null)?("Kolkata"):(intent.getStringExtra("City"));
        date = findViewById(R.id.date);
        temperature = findViewById(R.id.temperature);
        wind = findViewById(R.id.wind);
        humidity = findViewById(R.id.humidity);
        clouds = findViewById(R.id.clouds);
        pressure = findViewById(R.id.pressure);
        day = findViewById(R.id.day);
        mainlayout = findViewById(R.id.layout);
        city = findViewById(R.id.city);
        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+citySelected, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        JSONArray a;
                        //Toast.makeText(mcontext, str, Toast.LENGTH_SHORT).show();

                        try {
                            a = response.getJSONArray("list");
                            Log.d("RESPONSE", a.getJSONObject(0).getString("dt"));
                            //creating the date array for fetching forecasts. The date array stores the dates of the upcoming four days.
                            if(str.length()==10)
                            {
                                weekday = changeDateToDay(str);
                                day.setText(weekday);
                                date.setText(str);
                            }

                            else
                                Toast.makeText(mcontext, "date problem = "+str, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(mcontext, "LOL", Toast.LENGTH_SHORT).show();
                            for(int i=0; i<a.length(); i=i+8)
                            {
                                String datestring = a.getJSONObject(i).getString("dt_txt");
                                if (str.equals(datestring.substring(0,10)))
                                {
                                    String temp = a.getJSONObject(i).getJSONObject("main").getString("temp");
                                    float temperatureValue = (Float.parseFloat(temp)-273.0f);//set temperature
                                    temperatureValue=Math.round(temperatureValue*10)/10.0f;
                                    temperature.setText(String.valueOf(temperatureValue)+"°c");
                                    String press = a.getJSONObject(i).getJSONObject("main").getString("pressure");//set pressure
                                    pressure.setText(press+"hPa");
                                    String speed = a.getJSONObject(0).getJSONObject("wind").getString("speed");//set wind speed
                                    float wind_speed = Float.parseFloat(speed) * (18/5f);
                                    wind_speed = Math.round(wind_speed*100)/100f;
                                    wind.setText(String.valueOf(wind_speed)+"kmph");
                                    String index = a.getJSONObject(0).getJSONObject("clouds").getString("all");//set cloud index
                                    clouds.setText(index+"%");
                                    String humid = a.getJSONObject(i).getJSONObject("main").getString("humidity");//set humidity
                                    humidity.setText(humid+"%");
                                    city.setText(citySelected);
                                    mainlayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }

    public String changeDateToDay(String date)
    {
        String weekday = "";
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8);
        int yr = Integer.parseInt(year);
        int mon = Integer.parseInt(month);
        int dy = Integer.parseInt(day);
        Calendar cal=Calendar.getInstance();
        cal.set(yr, mon, dy);
        int val = cal.get(Calendar.DAY_OF_WEEK);
        switch(val){
            case 1:
                Toast.makeText(mcontext, "Thursday", Toast.LENGTH_SHORT).show();
                weekday = "Thursday";
                break;
            case 2:
                Toast.makeText(mcontext, "Friday", Toast.LENGTH_SHORT).show();
                weekday = "Friday";
                break;
            case 3:
                Toast.makeText(mcontext, "Saturday", Toast.LENGTH_SHORT).show();
                weekday = "Saturday";
                break;
            case 4:
                Toast.makeText(mcontext, "Sunday", Toast.LENGTH_SHORT).show();
                weekday = "Sunday";
                break;
            case 5:
                Toast.makeText(mcontext, "Monday", Toast.LENGTH_SHORT).show();
                weekday = "Monday";
                break;
            case 6:
                Toast.makeText(mcontext, "Tuesday", Toast.LENGTH_SHORT).show();
                weekday = "Tuesday";
                break;
            case 7:
                Toast.makeText(mcontext, "Wednesday", Toast.LENGTH_SHORT).show();
                weekday = "Wednesday";
                break;
        }
        return weekday;
    }
}
