package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Winds extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
private String url="https://api.openweathermap.org/data/2.5/forecast?mode=json&appid=2215bb88ae43c1cd66e40339f8dd0fa6&q=";
    private String urlCurrent="https://api.openweathermap.org/data/2.5/weather?mode=json&appid=2215bb88ae43c1cd66e40339f8dd0fa6&q=";
    DrawerLayout scrl;
    CardView cardView1, cardView2, cardView3, cardView4;
    TextView cardTemp1,cardTemp2,cardTemp3,cardTemp4;
    TextView cardDay1,cardDay2,cardDay3,cardDay4;
    TextView windspeed;
    TextView dateView;
    TextView place;

    ActionBarDrawerToggle toggle;
    private GestureDetectorCompat gestureDetector;
    NavigationView nv;
    int x=0;
    private ProgressBar progressBar;
    String dateArray[];
    String cityselected;
    private Context mcontext;
    TextView sunrise, sunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winds);
        scrl=findViewById(R.id.scrol);
        mcontext=this;
        progressBar=findViewById(R.id.progressbar);
        place=findViewById(R.id.place);
        dateView=findViewById(R.id.date);

        Intent intent = getIntent();
        cityselected = (intent.getStringExtra("city")==null)?"Kolkata,IN":intent.getStringExtra("city");
        place.setText(cityselected);
        Toast.makeText(mcontext, cityselected, Toast.LENGTH_SHORT).show();
        //for nav-drawer
        toggle=new ActionBarDrawerToggle(this, scrl,R.string.Open,R.string.Close);
        scrl.addDrawerListener(toggle);
        toggle.syncState();

        //#6day
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6daaee")));
        getSupportActionBar().setElevation(0);
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
                        finish();
                        break;
                    case R.id.winds:
                        scrl.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.temperature:
                        Intent intent3 = new Intent(mcontext, MainActivity.class);
                        intent3.putExtra("city",cityselected);
                        startActivity(intent3);
                        finish();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        nv.getMenu().getItem(2).setChecked(true);
        gestureDetector = new GestureDetectorCompat(this,this);
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
        windspeed = findViewById(R.id.windspeed);
        load_data(cityselected);
        load_sundata(cityselected);
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
public void load_data(String city){
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
                        dateArray = new String[5];
                        String speed = a.getJSONObject(0).getJSONObject("wind").getString("speed");
                        float speed_val = Float.parseFloat(speed) * (18/5f);
                        speed_val = (Math.round(speed_val*10))/10f;
                        windspeed.setText(String.valueOf(speed_val)+"kmph");
                        int k=0;
                        float windspeed=0f;
                        dateView.setText(a.getJSONObject(0).getString("dt_txt").substring(0,10));
                        for(int i=0; i<a.length() ;i++)
                        {
                            String date = a.getJSONObject(i).getString("dt_txt");
                            windspeed=windspeed+Float.parseFloat(a.getJSONObject(i).getJSONObject("wind").getString("speed"));
                            if(i%8==0){
                                float avgWindSpeed=(windspeed/8f)*(18/5);
                                avgWindSpeed=Math.round(avgWindSpeed*10)/10.0f;
                                windspeed=0;
                                if(k==1){
                                    cardTemp1.setText(String.valueOf(avgWindSpeed)+"kmph");
                                    cardDay1.setText(changeDateToDay(date.substring(0,10)));
                                }else if(k==2){
                                    cardTemp2.setText(String.valueOf(avgWindSpeed)+"kmph");
                                    cardDay2.setText(changeDateToDay(date.substring(0,10)));
                                }else if(k==3){
                                    cardTemp3.setText(String.valueOf(avgWindSpeed)+"kmph");
                                    cardDay3.setText(changeDateToDay(date.substring(0,10)));
                                }else if(k==4){
                                    cardTemp4.setText(String.valueOf(avgWindSpeed)+"kmph");
                                    cardDay4.setText(changeDateToDay(date.substring(0,10)));
                                }
                                dateArray[k++] = date.substring(0,10);

                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("RESPONSE", a.getJSONObject(0).getString("dt"));
                        TextView mainTemp = findViewById(R.id.mainTemp);
                        String temp = a.getJSONObject(0).getJSONObject("main").getString("temp");
                    }
                    catch (Exception e)

                    {
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
//AlertDialogue for choosing cities
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
    AlertDialog.Builder builder = new AlertDialog.Builder(Winds.this);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
