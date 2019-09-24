package in.wangziq.fitnessrecorder.GetLocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MyLocation implements LocationListener {

    private final Context mContext;
    private final Activity mActivity;

    // flag for GPS status
    private boolean canGetLocation = false;

    private Location location;
    public static double latitude;
    public static double longitude;
    public static double speed;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    private LocationManager locationManager;

    public MyLocation(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        getLocation ();
    }

    /**
     * Function to get the user's current location
     */
    private void getLocation() {

        if (ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( mActivity , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        try {
            locationManager = (LocationManager) mContext.getSystemService ( Context.LOCATION_SERVICE );

            boolean isGPSEnabled = locationManager.isProviderEnabled ( LocationManager.GPS_PROVIDER );
            boolean isNetworkEnabled = locationManager.isProviderEnabled ( LocationManager.NETWORK_PROVIDER );

            Log.v ( "isGPSEnabled", "=" + isGPSEnabled );

            Log.v ( "isNetworkEnabled", "=" + isNetworkEnabled );

            if (!isGPSEnabled && !isNetworkEnabled) {
                showSettingsAlert ();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;

                    if (ActivityCompat.checkSelfPermission ( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( mContext, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates (
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(MyLocation.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public double getSpeed(){
        if (location != null)
            speed = location.getSpeed();
        return speed;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Настройки GPS");
        alertDialog.setMessage("GPS не включен. Вы хотите перейти в меню настроек?");
        alertDialog.setPositiveButton("Настройки",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Отменить",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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
}
