package in.wangziq.fitnessrecorder.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

import in.wangziq.fitnessrecorder.DB.DBhelper;
import in.wangziq.fitnessrecorder.GetLocation.MyLocation;
import in.wangziq.fitnessrecorder.MyModel;
import in.wangziq.fitnessrecorder.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    public double lat, lng, sp;
    Activity activity = this;
    Button buttonStart, buttonStop;
    private int seconds = 0;
    private boolean running;
    Runnable run;
    final Handler handler = new Handler();
    ArrayList<MarkerOptions> markers = new ArrayList<>();
    public static ArrayList<MyModel> modelArrayList = new ArrayList<>();
    public static ArrayList<MyModel> arrayList = new ArrayList<>();
    TextView textViewTime, textViewDist;
    DBhelper dBhelper;
    MyModel model = new MyModel();
    Marker currentMarker = null;
    ImageView buttonPuls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        initialiseViewsAndComponents();
        onClickStart();
        onClickStop();
        onClickPuls();
        runTimer();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MyLocation myLocation = new MyLocation(activity, activity);
        lat = myLocation.getLatitude();
        lng = myLocation.getLongitude();
        mMap = googleMap;
        LatLng my_location = new LatLng(lat, lng);
        currentMarker = mMap.addMarker(new MarkerOptions()
                .position(my_location)
                .title("My location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
    }

    @SuppressLint("SetTextI18n")
    private void coordUpdate(){
        MyLocation myLocation = new MyLocation(this, this);
        lat = myLocation.getLatitude ();
        lng = myLocation.getLongitude ();
        sp = (myLocation.getSpeed() * 167)/10000;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(15)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);

        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }

        currentMarker = mMap.addMarker(new MarkerOptions()
                .title("my location")
                .position(new LatLng(lat, lng)));

        markers.add(new MarkerOptions()
                .title("my location")
                .position(new LatLng(lat, lng)));

        if (markers.size() > 2) {
            textViewDist.setText((int) kolvoDistanse(markers) + " m");
            if ((int) kolvoDistanse(markers) >= 10 ) {
                mMap.addPolyline(new PolylineOptions()
                        .add(markers.get(markers.size() - 2).getPosition(),
                                markers.get(markers.size() -1).getPosition()).color(Color.RED));
            }
        }
    }

    public void onClickStart(){
        buttonStart.setTag(1);
        buttonStart.setText("Старт");
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    buttonStart.setText("Пауза");
                    v.setTag(0);
                    running = true;
                    run = new Runnable() {
                        @Override
                        public void run() {
                            coordUpdate();
                            handler.postDelayed ( this, 3000 );
                        }
                    };
                    handler.post ( run );
                }
                if (status == 0) {
                    buttonStart.setText("Старт");
                    v.setTag(1);
                    buttonStop.setClickable(true);
                    running = false;
                    handler.removeCallbacks(run);
                }
            }
        });
    }

    public void onClickStop(){
        buttonStop.setText("Сохранить");
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setTag(0);
                running = false;
                seconds = 0;
                model.setTime(textViewTime.getText().toString());
                model.setDistanse(textViewDist.getText().toString());
                dBhelper.addNewItem(model);
                modelArrayList.add(model);
                arrayList = dBhelper.getAllNewsItems();
                markers.clear();
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickPuls() {
        buttonPuls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable(){

            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                textViewTime.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public double distanse (double lat1, double log1, double lat2, double log2) {
        Location locationOne = new Location("");
        locationOne.setLatitude(lat1);
        locationOne.setLongitude(log1);
        Location locationTwo = new Location("");
        locationTwo.setLatitude(lat2);
        locationTwo.setLongitude(log2);
        return locationOne.distanceTo(locationTwo);
    }

    public double kolvoDistanse (ArrayList<MarkerOptions> markerOptions) {
        double distanse = 0;

        for (int i = 1; i < markerOptions.size(); i++) {
            double lat1 = markerOptions.get(i - 1).getPosition().latitude;
            double log1 = markerOptions.get(i - 1).getPosition().longitude;
            double lat2 = markerOptions.get(i).getPosition().latitude;
            double log2 = markerOptions.get(i).getPosition().longitude;
            distanse += distanse(lat1, log1, lat2, log2);
        }
        return distanse;
    }

    private void initialiseViewsAndComponents() {
        textViewTime = findViewById(R.id.timer_view);
        textViewDist = findViewById(R.id.distanse_view);
        buttonStop = findViewById(R.id.button_stop);
        buttonStart = findViewById(R.id.start_pause);
        dBhelper = new DBhelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        buttonPuls = findViewById(R.id.button_puls);
    }
}
