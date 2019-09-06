package com.example.whackamole;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CountDown {

    public CountDown() {

    }


    public void startGameTimer(final App app, int gameTime, final TextView timeView) {

        new CountDownTimer(gameTime * 1000, 1000) {

            @Override
            public void onTick(final long l) {
                if (!app.getIsGameOver()) {
                    timeView.setText("" + l / 1000);
                }
            }

            @Override
            public void onFinish() {
                if (!app.getIsGameOver()) {
                    if (Integer.parseInt(app.points.getText().toString()) <= app.getMissesAmout()) {
                        app.savePlayerStatsOnDB(Integer.parseInt(app.points.getText().toString()),app.getMissesAmout());
                        app.setIsWinner(false);
                    }
                    app.setGameOverAndShowPopUp();
                }
            }
        }.start();
    }


}
