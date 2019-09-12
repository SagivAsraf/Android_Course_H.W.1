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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);



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


