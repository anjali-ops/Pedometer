package com.example.pedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private ArrayList<HistoryItems> mHistoryList;
    private TextView textView1;
    private TextView tv_speed;
    private  double magnitudePrevious = 0;
    private  Integer stepCount = 0;
    private int timeInterval  = 1000;

    //currentSpeed in m/s and timeInterval in s.
    private float instantaneousSpeed = 0;
    private float oldSpeed =0;
    private float totalDistanceTravelled = 0;
    //timer Start time
    private long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            //timeInterval = (int) (System.currentTimeMillis() - startTime)/1000;
            float averageSpeed = (instantaneousSpeed + oldSpeed)/2;
            calculateStep(averageSpeed);
            textView1.setText(stepCount.toString());
            oldSpeed = instantaneousSpeed;
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_speed = findViewById(R.id.tv_speed);
        textView1 = findViewById(R.id.TextView1);

        loadHistoryData();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }else{
            //Start the program if Permission is Granted
            doStuff();
        }

        this.updateSpeed(null);

        this.calculateStep(0);
        textView1.setText(stepCount.toString());
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void calculateTotalDistanceTravelled(float averageSpeed){
        //Distance Travelled in meter
        float instantaneousDistanceTravelled = (averageSpeed * timeInterval / 1000);
        totalDistanceTravelled += instantaneousDistanceTravelled;
    }

    private void calculateStep(float averageSpeed){
        calculateTotalDistanceTravelled(averageSpeed);
        float tempStepCount = totalDistanceTravelled / 0.7f;
        stepCount = (int)tempStepCount;
        if(tempStepCount - stepCount>0.5) stepCount+=1;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                clearAndSaveToHistoryList();
                Toast.makeText(this, "Reset Selected",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item2:
                openHistoryActivity();
                Toast.makeText(this, "History Selected",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item3:
                openAboutActivity();
                Toast.makeText(this, "About Selected",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void clearAndSaveToHistoryList(){
        if(stepCount == 0){
            return;
        }
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        mHistoryList.add(new HistoryItems(currentDate,currentTime,stepCount.toString()));
        saveHistoryData();
        totalDistanceTravelled = 0;
    }

    public void saveHistoryData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mHistoryList);
        //editor.clear();
        editor.putString("HistoryItemsList", json);
        editor.apply();
    }

    public void loadHistoryData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared Preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("HistoryItemList",null);
        Type type = new TypeToken<ArrayList<HistoryItems>>() {}.getType();
        mHistoryList = gson.fromJson(json, type);

        if(mHistoryList == null){
            mHistoryList = new ArrayList<>();
        }
    }

    public void openAboutActivity(){
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    public void openHistoryActivity(){
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    protected void onPause(){
        super.onPause();
        saveHistoryData();
        timerHandler.removeCallbacks(timerRunnable);
    }

    protected void onStop(){
        super.onStop();
        saveHistoryData();
    }

    protected void onResume(){
        super.onResume();
        loadHistoryData();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            CLocation myLocation = new CLocation(location);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void doStuff(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this,"Waiting for GPS Connection", Toast.LENGTH_SHORT).show();
    }

    private void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        if(location != null){
            //Speed in meter/Seconds
            nCurrentSpeed = location.getSpeed();
            instantaneousSpeed = nCurrentSpeed+4f;
            //Change in Km/h
            nCurrentSpeed = nCurrentSpeed * 3.6f;
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%1.1f", nCurrentSpeed);

        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");

        tv_speed.setText(strCurrentSpeed);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doStuff();
            }else {
                finish();
            }
        }
    }
}