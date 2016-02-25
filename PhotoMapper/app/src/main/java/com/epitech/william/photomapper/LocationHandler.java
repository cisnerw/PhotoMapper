package com.epitech.william.photomapper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by willi_000 on 22/02/2016.
 */
public class LocationHandler {
    private static final LocationHandler instance = new LocationHandler();

    private static final long LOCATION_REFRESH_TIME = 1000;
    private static final float LOCATION_REFRESH_DISTANCE = 100;

    private double mLatitude;
    private double mLongitude;
    private float mAccuracy;
    private double mAltitude;
    private LocationManager mLocationManager;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            mAltitude = location.getAltitude();
            mAccuracy = location.getAccuracy();
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
    };

    public LocationHandler() {

    }

    public void init(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            mAltitude = location.getAltitude();
            mAccuracy = location.getAccuracy();
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    public static LocationHandler getInstance() {
        return instance;
    }

    public LatLng getCoordinate() {
        return (new LatLng(mLatitude, mLongitude));
    }

    public float getmAccuracy() {
        return mAccuracy;
    }

    public double getmAltitude() {
        return mAltitude;
    }
}
