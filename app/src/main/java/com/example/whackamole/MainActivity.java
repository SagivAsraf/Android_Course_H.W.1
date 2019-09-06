package com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView playButton;
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        playButton = findViewById(R.id.playButton);

        setPlayButtonStyle(playButton);

        userName = findViewById(R.id.nameText);



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
        startActivity(intent);

    }


}
