package com.example.pedometer;

import android.location.Location;

public class CLocation extends Location {
    public CLocation(Location location){
        super(location);
    }

    @Override
    public float distanceTo(Location dest) {
        //Distance in meter
        float nDistance = super.distanceTo(dest);
        return nDistance;
    }

    @Override
    public double getAltitude() {
        //Altitude in Meter
        double nAltitude = super.getAltitude();
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        //Speed in meter/seconds
        float nSpeed = super.getSpeed();
        return nSpeed;
    }

    @Override
    public float getAccuracy() {
        //Accuracy in meters
        float nAccuracy = super.getAccuracy();
        return nAccuracy;
    }
}
