package in.wangziq.fitnessrecorder.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import in.wangziq.fitnessrecorder.Adapters.MyAdapter;
import in.wangziq.fitnessrecorder.R;


public class MainActivityFirst extends AppCompatActivity {
    ListView listView;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
        checkPermissionLocation();
        onClickNext();
        initComponents();
    }

    private void onClickNext() {
        FloatingActionButton floatingActionButton = findViewById(R.id.button_addd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityFirst.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAdapter adapter = new MyAdapter(this, MapsActivity.arrayList);
        listView.setAdapter(adapter);
    }


    private void checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void initComponents() {
        listView = findViewById(R.id.list_view);
        MyAdapter adapter = new MyAdapter(this, MapsActivity.arrayList);
        listView.setAdapter(adapter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
}
