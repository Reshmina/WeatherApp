package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,GestureDetector.OnGestureListener {
    private String url="https://api.openweathermap.org/data/2.5/forecast?mode=json&appid=2215bb88ae43c1cd66e40339f8dd0fa6&units=metric&q=";
    private String urlCurrent="https://api.openweathermap.org/data/2.5/weather?mode=json&appid=2215bb88ae43c1cd66e40339f8dd0fa6&units=metric&q=";
    ImageView icon;
    DrawerLayout scrl;
    TextView time1, time2, time3, time4, time5, time6;
    TextView temp1, temp2, temp3, temp4, temp5, temp6;
    CardView cardView1, cardView2, cardView3, cardView4;
    TextView cardTemp1,cardTemp2,cardTemp3,cardTemp4;
    TextView cardDay1,cardDay2,cardDay3,cardDay4;
    TextView day1, day2, day3, day4, day5, day6;
    TextView place, day;
    TextView sunrise, sunset;
    ActionBarDrawerToggle toggle;
    private ProgressBar progressBar;
    NavigationView nv;
    TextView dateView;
    int x=0;
    String dateArray[];
    private Context mcontext;
    String cityselected;
    private GestureDetectorCompat gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;
        progressBar=findViewById(R.id.progressbar);
        day = findViewById(R.id.day);
        icon = findViewById(R.id.imageview);
        place=findViewById(R.id.place);
        time1=findViewById(R.id.time1);time2=findViewById(R.id.time2);time3=findViewById(R.id.time3); time4=findViewById(R.id.time4);
        time5=findViewById(R.id.time5);time6=findViewById(R.id.time6);temp1=findViewById(R.id.temp1);temp2=findViewById(R.id.temp2);
        temp3=findViewById(R.id.temp3);temp4=findViewById(R.id.temp4);temp5=findViewById(R.id.temp5); temp6=findViewById(R.id.temp6);
        dateView=findViewById(R.id.date);
        scrl=findViewById(R.id.scrol);
        day1=findViewById(R.id.day1);
        day2=findViewById(R.id.day2);
        day3=findViewById(R.id.day3);
        day4=findViewById(R.id.day4);
        day5=findViewById(R.id.day5);
        day6=findViewById(R.id.day6);
        //for nav-drawer
        toggle=new ActionBarDrawerToggle(this, scrl,R.string.Open,R.string.Close);
        scrl.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Temperature");
        Intent intent = getIntent();
        cityselected = (intent.getStringExtra("city")==null)?"Kolkata,IN":intent.getStringExtra("city");
        place.setText(cityselected);



        //The Hamburger icon is applied to the action bar for working with the nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = findViewById(R.id.design_navigation_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.clouds:
                        Intent intent1 = new Intent(mcontext, Clouds.class);
                        intent1.putExtra("city",cityselected);
                        startActivity(intent1);
                        break;
                    case R.id.winds:
                        Intent intent2 = new Intent(mcontext, Winds.class);
                        intent2.putExtra("city",cityselected);
                        startActivity(intent2);
                        break;
                    case R.id.temperature:
                        scrl.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        nv.getMenu().getItem(0).setChecked(true);

        gestureDetector = new GestureDetectorCompat(this,this );
        cardView1 = findViewById(R.id.card1);
        cardView1.setOnTouchListener(this);
        cardView2 = findViewById(R.id.card2);
        cardView2.setOnTouchListener(this);
        cardView3 = findViewById(R.id.card3);
        cardView3.setOnTouchListener(this);
        cardView4 = findViewById(R.id.card4);
        cardView4.setOnTouchListener(this);
        cardTemp1=findViewById(R.id.cardtemp1);
        cardTemp2=findViewById(R.id.cardtemp2);
        cardTemp3=findViewById(R.id.cardtemp3);
        cardTemp4=findViewById(R.id.cardtemp4);
        cardDay1=findViewById(R.id.cardDay1);
        cardDay2=findViewById(R.id.cardDay2);
        cardDay3=findViewById(R.id.cardDay3);
        cardDay4=findViewById(R.id.cardDay4);

      load_data(cityselected);
      load_sundata(cityselected);
    }
    public void onClick(View view)
    {
        int pos=0;
        switch (cityselected){
            case "Baghdogra,IN":pos=0; break;
            case "Bangalore,IN":pos=1;break;
            case  "Bangkok,TH":pos=2;break;
            case  "Beijing,CN":pos=3;break;
            case  "Berlin,DE":pos=4;break;
            case "Buenos Aires,AR":pos=5;break;
            case "Cairo,EG":pos=6;break;
            case "Chennai,IN":pos=7;break;
            case "Dhaka,BD":pos=8;break;
            case "Delhi,IN":pos=9;break;
            case "Islamabad,PK":pos=10;break;
            case "Jakarta,ID":pos=11;break;
            case "Kabul,AF":pos=12;break;
            case "Kathmandu,NP":pos=13;break;
            case "Kolkata,IN":pos=14;break;
            case "London,GB":pos=15;break;
            case "Madrid,ES":pos=16;break;
            case "Moscow,RU":pos=17;break;
            case "Mughal Sarai,IN":pos=18;break;
            case "Mumbai,IN":pos=19;break;
            case "New York,US":pos=20;break;
            case "Paris,FR</item":pos=21;break;
            case "Pyongyang,KP":pos=22;break;
            case "Shangai,CN":pos=23;break;
            case "Singapore,SG":pos=24;break;
            case "Tokyo,JP":pos=25;break;
            case "Varanasi,IN": pos=26; break;
            case "Vientiane,LA": pos=27;break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        place=findViewById(R.id.place);
        final List<String> cities = Arrays.asList(getResources().getStringArray(R.array.Select));
        builder.setTitle("Select").setSingleChoiceItems(R.array.Select, pos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {}

        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i){
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                load_data(cities.get(selectedPosition));
                place.setText(cities.get(selectedPosition));
                cityselected=cities.get(selectedPosition);
                load_sundata(cityselected);
                Toast.makeText(mcontext, cities.get(selectedPosition), Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            // removes the dialog from the screen

        }
    });

            AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void load_sundata(String city)
    {
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlCurrent+city, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        try {
                            String sunrise_time = response.getJSONObject("sys").getString("sunrise");
                            String sunset_time = response.getJSONObject("sys").getString("sunset");

                            Date result=new Date(Long.parseLong(sunrise_time)*1000);
                            DateFormat simple = new SimpleDateFormat("h:mm a");
                            sunrise.setText("Sunrise : "+simple.format(result));
                            result=new Date(Long.parseLong(sunset_time)*1000);
                            sunset.setText("Sunset  : "+simple.format(result));
                        }
                        catch (Exception e) {
                            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
// add it to the RequestQueue
        queue.add(getRequest);

    }
    public void load_data(String city)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url+city, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        JSONArray a = null;

                        try {
                            a = response.getJSONArray("list");
                            dateArray=new String[5];
                            Log.d("RESPONSE", a.getJSONObject(0).getString("dt"));
                            String temp = a.getJSONObject(0).getJSONObject("main").getString("temp");
                            //creating the date array for fetching forecasts. The date array stores the dates of the upcoming four days.
                            //It will be used to display the weekday on card-view and send dates to forecast activity
                            int k=0;
                            float tempsum=0f;
                            dateView.setText(a.getJSONObject(1).getString("dt_txt").substring(0,10));
                            String date = a.getJSONObject(1).getString("dt_txt");
                            day.setText(changeDateToDay(date.substring(0,10)));
                            for(int i=0; i<a.length() ;i++)
                            {
                                String dateForForecast = a.getJSONObject(i).getString("dt_txt");
                                    tempsum = tempsum + Float.parseFloat(a.getJSONObject(i).getJSONObject("main").getString("temp"));
                                    if (i % 8 == 0) {
                                        float avgtemp = tempsum / 8f;
                                        avgtemp = Math.round(avgtemp * 10) / 10.0f;
                                        tempsum = 0;
                                        if (k == 1) {
                                            cardTemp1.setText(String.valueOf(avgtemp) + "°c");
                                            cardDay1.setText(changeDateToDay(dateForForecast.substring(0, 10)));
                                        } else if (k == 2) {
                                            cardTemp2.setText(String.valueOf(avgtemp) + "°c");
                                            cardDay2.setText(changeDateToDay(dateForForecast.substring(0, 10)));
                                        } else if (k == 3) {
                                            cardTemp3.setText(String.valueOf(avgtemp) + "°c");
                                            cardDay3.setText(changeDateToDay(dateForForecast.substring(0, 10)));
                                        } else if (k == 4) {
                                            cardTemp4.setText(String.valueOf(avgtemp) + "°c");
                                            cardDay4.setText(changeDateToDay(dateForForecast.substring(0, 10)));
                                        }
                                        dateArray[k++] = date.substring(0, 10);

                                    }
                                }

                            int val = (int) Math.round(Double.parseDouble(temp));
                            changegradient(val);
                            //set the day for each point of the graph
                            date = a.getJSONObject(0).getString("dt_txt");
                            day1.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            date = a.getJSONObject(1).getString("dt_txt");
                            day2.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            date = a.getJSONObject(2).getString("dt_txt");
                            day3.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            date = a.getJSONObject(3).getString("dt_txt");
                            day4.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            date = a.getJSONObject(4).getString("dt_txt");
                            day5.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            date = a.getJSONObject(5).getString("dt_txt");
                            day6.setText((changeDateToDay(date.substring(0,10))).substring(0,3));
                            //linear graph construction
                            LineChartView lineChartView = findViewById(R.id.chart);

                            String[] axisData = {"0", "1", "2", "3", "4", "5"};
                            int[] yAxisData = new int[]{30,31,32,33,34,35};

                            for(int i=0;i<6;i++)
                            {
                                String temperature = a.getJSONObject(i).getJSONObject("main").getString("temp");
                                yAxisData[i]=(int)Double.parseDouble(temperature);
                            }
                            List yAxisValues = new ArrayList();
                            List axisValues = new ArrayList();


                            Line line = new Line(yAxisValues).setColor(Color.parseColor("#ffffff"));

                            for (int i = 0; i < axisData.length; i++) {
                                axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
                            }

                            for (int i = 0; i < yAxisData.length; i++) {
                                yAxisValues.add(new PointValue(i, yAxisData[i]));
                            }

                            List lines = new ArrayList();
                            lines.add(line);

                            LineChartData data = new LineChartData();
                            data.setLines(lines);//accepts arraylist so that the line can show-up on the UI


                            lineChartView.setLineChartData(data);
                            //end of chart construction
                            time1.setText(getTime(a.getJSONObject(0).getString("dt_txt")));
                            time2.setText(getTime(a.getJSONObject(1).getString("dt_txt")));
                            time3.setText(getTime(a.getJSONObject(2).getString("dt_txt")));
                            time4.setText(getTime(a.getJSONObject(3).getString("dt_txt")));
                            time5.setText(getTime(a.getJSONObject(4).getString("dt_txt")));
                            time6.setText(getTime(a.getJSONObject(5).getString("dt_txt")));

                            temp1.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(0).getJSONObject("main").getString("temp")))+"°c");
                            temp2.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(1).getJSONObject("main").getString("temp")))+"°c");
                            temp3.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(2).getJSONObject("main").getString("temp")))+"°c");
                            temp4.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(3).getJSONObject("main").getString("temp")))+"°c");
                            temp5.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(4).getJSONObject("main").getString("temp")))+"°c");
                            temp6.setText(String.valueOf((int)Double.parseDouble(a.getJSONObject(5).getJSONObject("main").getString("temp")))+"°c");

                        } catch (Exception e) {
                            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void changegradient(int val)
    {
        TextView mainTemp = findViewById(R.id.mainTemp);
        float var = val;
        mainTemp.setText((var) + "°c");
        //code for icon change

        if (var >= 30.0) {
            icon.setImageResource(R.drawable.summer);
        } else if (var < 15.0) {
            icon.setImageResource(R.drawable.winter);
        } else if (var >= 15.0 && var < 30.0) {
            icon.setImageResource(R.drawable.spring);
        }
        //code for background gradient colour change
        int r=0, g=0, b=0;
        //setting values of rgb
        if(var<-10.0)
        {
            r = 121;
            g = 178;
            b = 232;
        }
        else if(var>=-10.0 && var<=20.0) {
            r = 64;
            g = 64+((int)var+10)*3;
            b = 255;
        }
        else if(var>20.0 && var<=50.0 )
        {
            r = 247;
            g = 247 - (int)((var-20.0f)*5.5f);
            b = 82;
        }
        else if(var>50.0)
        {
            r= 235;
            g= 33;
            b= 33;
        }
        String hex = String.format("#%02x%02x%02x", r, g, b); //converting rgb into hex string
        String grey = "#FFFFFF";
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(hex)));
        getSupportActionBar().setElevation(0);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(hex), Color.parseColor(grey)});
        progressBar.setVisibility(View.INVISIBLE);
        scrl.setBackground(gradientDrawable);
    }
    public String changeDateToDay(String date)
    {
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
                return "Thursday";
            case 2:
                return "Friday";
            case 3:
                return "Saturday";
            case 4:
                return  "Sunday";
            case 5:
                return  "Monday";
            case 6:
                return  "Tuesday";
            case 7:
                return  "Wednesday";
                default:
                    return "";
        }
    }
    public String getTime(String t)
    {
        int size = cityselected.length();
        String countryCode = cityselected.substring(size - 2, size);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat df1 = new SimpleDateFormat("h a", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try{
            Date date = df.parse(t);
            df1.setTimeZone(TimeZone.getTimeZone(countryCode));
            String formattedTime = df1.format(date);
            return formattedTime ;
        }
        catch(Exception e)
        {
            return e.toString();
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId()==R.id.card1)
            {
                x=1;
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        else if(view.getId()==R.id.card2)
        {
            x=2;
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
        else if(view.getId()==R.id.card3)
        {
            x=3;
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
        else if(view.getId()==R.id.card4)
        {
            x=4;
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if(e2.getX()-e1.getX()>0)
        {
            //Toast.makeText(this, "flinged left to right", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Forecast.class);

                                if (x == 1)
                                {
                                    intent.putExtra("Date", dateArray[1]);
                                    intent.putExtra("City", cityselected);
                                    startActivity(intent);
                                }
                                else if (x == 2)
                                {
                                    intent.putExtra("Date", dateArray[2]);
                                    intent.putExtra("City", cityselected);
                                    startActivity(intent);
                                }
                                else if (x == 3)
                                {
                                    intent.putExtra("Date", dateArray[3]);
                                    intent.putExtra("City", cityselected);
                                    startActivity(intent);
                                }
                                else if (x == 4)
                                {
                                    intent.putExtra("Date", dateArray[4]);
                                    intent.putExtra("City", cityselected);
                                    startActivity(intent);
                                }

                        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (scrl.isDrawerOpen(GravityCompat.START)) {
            scrl.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }
}