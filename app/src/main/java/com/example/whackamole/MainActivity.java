package com.example.whackamole;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    CardView playButton;
    EditText userName;
    CardView hallOfFameButton;
    private final static String PLAYER_STATS = "Players_Stats";
    private final int NUMBER_OF_REQUEST = 1;
    private final int TIME_FOR_NEW_REQUEST = 6000000;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_FOR_NEW_REQUEST, 0, locationListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                Log.i("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //If device is running sdk<23
        if (Build.VERSION.SDK_INT < 23)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_FOR_NEW_REQUEST, 0, locationListener);
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, NUMBER_OF_REQUEST);


            } else
            //We have a permission
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_FOR_NEW_REQUEST, 0, locationListener);

            }
        }

        playButton = findViewById(R.id.playButton);
        hallOfFameButton = findViewById(R.id.hallOfFameButton);
        setPlayButtonStyle(playButton);

        userName = findViewById(R.id.nameText);


        setPlayClickListener(playButton);

        setHallOfFameClickListener(hallOfFameButton);




    }

    private void setHallOfFameClickListener(CardView hallOfFameButton) {
        hallOfFameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHallOfFameActivity();
            }});
        }

    private void openHallOfFameActivity() {
        Intent intent = new Intent(this,HallOfFame.class);
        intent.putExtra(DataTransferBetweenActivities.COLLECTION,PLAYER_STATS);
        intent.putExtra(DataTransferBetweenActivities.DOCUMENT,userName.getText().toString());

        startActivity(intent);
    }

    private void setPlayClickListener(final CardView playButton) {

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Cant play, please fill your name!",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    openAppActivity();
                    playButton.setCardBackgroundColor(Color.parseColor("#1EFF1E"));
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setPlayButtonStyle(playButton);
    }

    private void setPlayButtonStyle(CardView playButton) {
        if (playButton != null) {
            playButton.setCardBackgroundColor(Color.parseColor("#FF137F"));
        }

    }

    private void openAppActivity() {
        Intent intent = new Intent(this,App.class);
        intent.putExtra(DataTransferBetweenActivities.PLAYER_NAME, userName.getText().toString());
        intent.putExtra(DataTransferBetweenActivities.COLLECTION,PLAYER_STATS);
        intent.putExtra(DataTransferBetweenActivities.DOCUMENT,userName.getText().toString());


        startActivity(intent);

    }


}


