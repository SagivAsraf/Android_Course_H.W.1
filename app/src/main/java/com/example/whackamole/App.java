package com.example.whackamole;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class App extends AppCompatActivity {

    final static int GAME_TIME = 30;
    final static int BOARD_SIZE = 9;
    final static int NUM_OF_LIVES = 3;
    final static int MAX_ALLOW_MIESSES = 3;
    final static int MAX_POINTS = 30;

    TextView timeLeftText;
    TextView points;

    ImageView[] lifeImagesArray = new ImageView[NUM_OF_LIVES];
    ImageView[] imageViewsArray = new ImageView[BOARD_SIZE];
    ImageView[] plus1Array = new ImageView[BOARD_SIZE];

    int[] imagesId = new int[4];
    private int missesAmout;

    private String userName;

    private boolean isGameOver = false;
    private boolean isWinner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        setImageLivesArray(lifeImagesArray);

        setPlus1ImagesArray(plus1Array);

        userName = getIntent().getStringExtra(DataTransferBetweenActivities.PLAYER_NAME);

        timeLeftText = findViewById(R.id.timeLeftText);
        points = findViewById(R.id.pointsLabeValue);

        getImageViewsAndIdsIntoArrays(imageViewsArray, imagesId);
        setClickListenerOnImages(imageViewsArray);


        startGame();
    }

    private void setPlus1ImagesArray(ImageView[] plus1Array) {

        plus1Array[0] = findViewById(R.id.plus1image00);
        plus1Array[1] = findViewById(R.id.plus1image01);
        plus1Array[2] = findViewById(R.id.plus1image02);
        plus1Array[3] = findViewById(R.id.plus1image10);
        plus1Array[4] = findViewById(R.id.plus1image11);
        plus1Array[5] = findViewById(R.id.plus1image12);
        plus1Array[6] = findViewById(R.id.plus1image20);
        plus1Array[7] = findViewById(R.id.plus1image21);
        plus1Array[8] = findViewById(R.id.plus1image22);

    }

    private void setImageLivesArray(ImageView[] lifeImagesArray) {
        lifeImagesArray[0] = findViewById(R.id.life1);
        lifeImagesArray[1] = findViewById(R.id.life2);
        lifeImagesArray[2] = findViewById(R.id.life3);
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

                if (imageView.getDrawable() == null) {
                    mpMiss.start();
                    setMissesAmout(getMissesAmout() + 1);
                    setLifesImages();
                    if (getMissesAmout() >= MAX_ALLOW_MIESSES) {
                        if (!getIsGameOver()) {
                            setIsWinner(false);
                            setGameOverAndShowPopUp();
                        }

                    }


                } else {
                    mpHit.start();
                    pointsAmount++;
                    animatePlus1(view.getId());
                    if (pointsAmount >= MAX_POINTS) {
                        if (!getIsGameOver()) {
                            setIsWinner(true);
                            setGameOverAndShowPopUp();
                        }
                    }
                    points.setTypeface(points.getTypeface(), Typeface.BOLD_ITALIC);
                    //points.setTextColor(Color.parseColor("#008000"));


                }
                points.setText(pointsAmount + "");
            }
        });
    }

    private void setLifesImages() {

        if (getMissesAmout() != 0) {
            lifeImagesArray[lifeImagesArray.length - getMissesAmout()].setBackgroundResource(0);
        }

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

    private void animatePlus1(int imageViewId) {
        int clickedImageView = findTheClickedImageView(imageViewId);
        plus1Array[clickedImageView].setImageResource(R.drawable.plus1);
        AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(600);
        animation1.setFillAfter(true);
        plus1Array[clickedImageView].startAnimation(animation1);
    }

    private int findTheClickedImageView(int imageViewId) {

        switch (imageViewId) {
            case R.id.image00:
                return 0;
            case R.id.image01:
                return 1;
            case R.id.image02:
                return 2;
            case R.id.image10:
                return 3;
            case R.id.image11:
                return 4;
            case R.id.image12:
                return 5;
            case R.id.image20:
                return 6;
            case R.id.image21:
                return 7;
            case R.id.image22:
                return 8;
        }
        return 0;
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
        imageViewsIds[1] = R.drawable.applogoandroid;
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

    public int getMissesAmout() {
        return missesAmout;
    }

    public void setMissesAmout(int missesAmout) {
        this.missesAmout = missesAmout;
    }
}
