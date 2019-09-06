package com.example.whackamole;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Data.DataCols;
import Data.DataManagement;

public class MainActivity extends AppCompatActivity {

    CardView playButton;
    EditText userName;
    Button hallOfFameButton;
    private final static String PLAYER_STATS = "Players_Stats";

    private DataManagement dataManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dataManagement = DataManagement.createSingletonDM();

        playButton = findViewById(R.id.playButton);

        setPlayButtonStyle(playButton);

        userName = findViewById(R.id.nameText);


        setPlayClickListener(playButton);

        setHallOfFameClickListener(hallOfFameButton);




    }

    private void setHallOfFameClickListener(Button hallOfFameButton) {
        hallOfFameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHallOfFameActivity();
            }});
        }
    private void openHallOfFameActivity() {
        Intent intent = new Intent(this,App.class);
        intent.putExtra(DataTransferBetweenActivities.PLAYER_NAME, userName.getText().toString());
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


