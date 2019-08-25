package com.example.whackamole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class App extends AppCompatActivity {

    final static int GAME_TIME = 30;
    final static int BOARD_SIZE = 9;
    final static int MAX_ALLOW_MIESSES = 3;
    final static int MAX_POINTS = 30;
    TextView timeLeftText;
    TextView points;
    TextView misses;
    ImageView[] imageViewsArray = new ImageView[BOARD_SIZE];
    int[] imagesId = new int[4];
    private String userName;
    private boolean isGameOver = false;
    private boolean isWinner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        userName = getIntent().getStringExtra(DataTransferBetweenActivities.PLAYER_NAME);

        timeLeftText = findViewById(R.id.timeLeftText);
        points = findViewById(R.id.pointsLabeValue);
        misses = findViewById(R.id.missesText);

        getImageViewsAndIdsIntoArrays(imageViewsArray, imagesId);
        setClickListenerOnImages(imageViewsArray);
        startGame();
    }

    private void setClickListenerOnImages(final ImageView[] imageView) {
        for (int i = 0; i < imageView.length; i++) {
            setClickListenerOnImage(imageViewsArray[i]);
        }

    }

    private void setClickListenerOnImage(final ImageView imageView) {

        final MediaPlayer mpHit = MediaPlayer.create(this, R.raw.success);
        final MediaPlayer mpMiss = MediaPlayer.create(this, R.raw.miss);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pointsAmount = Integer.parseInt(points.getText().toString());
                int missesAmount = Integer.parseInt(misses.getText().toString());

                if (imageView.getDrawable() == null) {
                    mpMiss.start();
                    missesAmount++;
                    if (missesAmount >= MAX_ALLOW_MIESSES) {
                        if(!getIsGameOver()){
                            setIsWinner(false);
                            setGameOverAndShowPopUp();
                        }

                    }
                    misses.setTypeface(misses.getTypeface(), Typeface.BOLD_ITALIC);
                    points.setTypeface(null);
                    misses.setTextColor(Color.parseColor("#FF0000"));


                } else {
                    mpHit.start();
                    pointsAmount++;
                    if (pointsAmount >= MAX_POINTS) {
                        if(!getIsGameOver()){
                            setIsWinner(true);
                            setGameOverAndShowPopUp();
                        }
                    }
                    points.setTypeface(points.getTypeface(), Typeface.BOLD_ITALIC);
                    misses.setTypeface(null);
                    points.setTextColor(Color.parseColor("#008000"));


                }
                points.setText(pointsAmount + "");
                misses.setText(missesAmount + "");
            }
        });
    }


    public void setGameOverAndShowPopUp() {
        disableListeners();
        setGameOver(true);
        showGameOverPopUp();
    }

    private void startGame() {
        startGameTimer();

        final Random rnd = new Random();
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {

            public void run() {

                int imagesViewsRndVal = rnd.nextInt(imageViewsArray.length);
                int imageIdRnd = rnd.nextInt(imagesId.length);

                if (shouldClearBoard()) {
                    clearBoard();
                } else {
                    if (imageViewsArray[imagesViewsRndVal].getDrawable() == null) {
                        imageViewsArray[imagesViewsRndVal].setImageResource(imagesId[imageIdRnd]);
                        animateMolesImages(imageViewsArray[imagesViewsRndVal]);
                    }
                }
                if (getIsGameOver()) {
                    handler.removeCallbacksAndMessages(null);
                    clearBoard();
                } else {
                    handler.postDelayed(this, 700);
                }
            }


            /*Make the game more interesting, clear the board in various cases*/
            private boolean shouldClearBoard() {
                return rnd.nextInt(3) == 2 || isMoreThanMoles(2) || (rnd.nextBoolean() && isMoreThanMoles(1));
            }

            /*Check if there are more than numOfMoles displaying on the board*/
            private boolean isMoreThanMoles(int numOfMoles) {
                int count = 0;
                for (ImageView imageView : imageViewsArray) {
                    if (imageView.getDrawable() != null) {
                        count++;
                    }
                }
                return count > numOfMoles;
            }


            private void clearBoard() {
                for (ImageView imageView : imageViewsArray) {
                    imageView.setImageResource(0);
                }
            }
        };
        if (!getIsGameOver()) {
            handler.postDelayed(r, 1000);
        }


    }


    private void animateMolesImages(ImageView image) {
        AlphaAnimation animation1 = new AlphaAnimation(0.1f, 1.0f);
        animation1.setDuration(400);
        animation1.setFillAfter(true);
        image.startAnimation(animation1);
    }

    private void startGameTimer() {
        new CountDown().startGameTimer(this, GAME_TIME, timeLeftText);
    }

    private void showGameOverPopUp() {

        final AlertDialog gameOverDialog;

        final AlertDialog.Builder gameOver = new AlertDialog.Builder(this);

        gameOver.setTitle("Game Over!!!");

        gameOver.setMessage(userName);

        gameOver.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        final View gameOverView = inflater.inflate(R.layout.game_over, null);

        gameOver.setView(gameOverView);

        gameOverDialog = gameOver.create();

        CardView startNewGameButton = gameOverView.findViewById(R.id.Start_New_Game);

        TextView winnerOrLoserText = gameOverView.findViewById(R.id.winnerOrLoserText);

        ImageView winnerOrLoserImage = gameOverView.findViewById(R.id.winnerOrLoser);


        if (getIsWinner()) {
            winnerOrLoserImage.setImageResource(R.drawable.winner);
            winnerOrLoserText.setText("You Won!!!!");
        } else {
            winnerOrLoserImage.setImageResource(R.drawable.loser);
            winnerOrLoserText.setText("You Lose!!!!");
        }

        startNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                gameOverDialog.dismiss();
            }
        });
        gameOverDialog.show();


    }

    private void disableListeners() {

        for (int i = 0; i < imageViewsArray.length; i++) {
            imageViewsArray[i].setOnClickListener(null);
        }

    }

    private void getImageViewsAndIdsIntoArrays(ImageView[] imageViewsArray, int[] imageViewsIds) {
        imageViewsArray[0] = findViewById(R.id.image00);
        imageViewsArray[1] = findViewById(R.id.image01);
        imageViewsArray[2] = findViewById(R.id.image02);
        imageViewsArray[3] = findViewById(R.id.image10);
        imageViewsArray[4] = findViewById(R.id.image11);
        imageViewsArray[5] = findViewById(R.id.image12);
        imageViewsArray[6] = findViewById(R.id.image20);
        imageViewsArray[7] = findViewById(R.id.image21);
        imageViewsArray[8] = findViewById(R.id.image22);

        imageViewsIds[0] = R.drawable.mole1;
        imageViewsIds[1] = R.drawable.mole2;
        imageViewsIds[2] = R.drawable.mole3;
        imageViewsIds[3] = R.drawable.mole4;


    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(boolean winner) {
        isWinner = winner;
    }
}
