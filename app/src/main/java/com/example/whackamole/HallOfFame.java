package com.example.whackamole;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Data.DataCols;
import Data.DataManagement;
import Data.FireStoreCallback;

public class HallOfFame extends AppCompatActivity {

    private String collectionName;
    private String documentName;
    private DataManagement dataManagement;
    private List<Map<String, Object>> playersList;
    private List<Map<String, Object>> sortedPlayerList;
    private CardView mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_of_fame);


        getMainActivityData();

        dataManagement = DataManagement.createSingletonDM();

        CardView backButton = findViewById(R.id.returnButton);
        mapButton = findViewById(R.id.mapButton);
        mapButton.setEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapPlayersLocation(playersList);
            }
        });

        addRowsToTable();


    }

    private void addRowsToTable() {

        playersList = new LinkedList<>();

        /*FirseStore loads data asynchronously*/
        dataManagement.readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> returnedPlayersList) {
                sortListByPoints(returnedPlayersList);
                sortedPlayerList = returnedPlayersList;
                fillTableWithData(returnedPlayersList);
                mapButton.setEnabled(true);
            }
        }, collectionName, playersList);


    }


    private void fillTableWithData(List<Map<String, Object>> returnedPlayersList) {
        TableLayout table = findViewById(R.id.playersTable);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 1, 1, 2);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        addHeaderRow(lp,table,params);

        for (int i = 0; i < 10 && i < returnedPlayersList.size(); i++) {
            addPlayerToTable(lp,table,params,returnedPlayersList,i);
        }
    }

    private void addPlayerToTable(TableRow.LayoutParams lp, TableLayout table, TableRow.LayoutParams params, List<Map<String, Object>> returnedPlayersList, int index) {

        TableRow row = new TableRow(this);

        row.setLayoutParams(lp);


        TextView tv1 = new TextView(this);
        tv1.setTextSize(1, 25);
        tv1.setText(returnedPlayersList.get(index).get(DataCols.NAME).toString());
        tv1.setGravity(Gravity.CENTER);
        tv1.setLayoutParams(params);
        tv1.setBackgroundColor(Color.WHITE);


        TextView tv2 = new TextView(this);
        tv2.setTextSize(1, 25);
        tv2.setText(returnedPlayersList.get(index).get(DataCols.POINTS).toString());
        tv2.setGravity(Gravity.CENTER);
        tv2.setLayoutParams(params);

        tv2.setBackgroundColor(Color.WHITE);

        TextView tv3 = new TextView(this);
        tv3.setTextSize(1, 25);
        tv3.setText(returnedPlayersList.get(index).get(DataCols.MISSES).toString());
        tv3.setGravity(Gravity.CENTER);
        tv3.setLayoutParams(params);
        tv3.setBackgroundColor(Color.WHITE);

        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);

        table.addView(row, index + 1);


    }

    private void addHeaderRow(TableRow.LayoutParams lp, TableLayout table, TableRow.LayoutParams params) {
        TableRow headerRow = new TableRow(this);

        params.weight = 1.0f;
        headerRow.setLayoutParams(lp);

        TextView tv1 = new TextView(this);
        tv1.setTextSize(1, 30);
        tv1.setText(DataCols.NAME);
        tv1.setLayoutParams(params);
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundColor(Color.rgb(229,229,229));

        tv1.setTypeface(null, Typeface.BOLD);

        TextView tv2 = new TextView(this);
        tv2.setTextSize(1, 30);
        tv2.setText(DataCols.POINTS);
        tv2.setLayoutParams(params);
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundColor(Color.rgb(229,229,229));
        tv2.setTypeface(null, Typeface.BOLD);


        TextView tv3 = new TextView(this);
        tv3.setTextSize(1, 30);
        tv3.setText(DataCols.MISSES);
        tv3.setGravity(Gravity.CENTER);

        tv3.setLayoutParams(params);
        tv3.setBackgroundColor(Color.rgb(229,229,229));

        tv3.setTypeface(null, Typeface.BOLD);

        headerRow.addView(tv1);
        headerRow.addView(tv2);
        headerRow.addView(tv3);

        table.addView(headerRow, 0);
    }


    /*Sort list by points because we want to be sure the 10 players that will be displayed they are the players
    * with the highest scores*/
    private void sortListByPoints(List<Map<String, Object>> returnedPlayersList) {

        Collections.sort(returnedPlayersList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return new Long((Long) o2.get("Points")).compareTo(new Long((Long)o1.get("Points")));
            }
        });

    }

    private void openMapPlayersLocation(List<Map<String,Object>> playersResults) {
        Intent intent = new Intent(this,MapsActivity.class);

        intent.putExtra("List", (Serializable) playersResults);


        startActivity(intent);

    }


    private void getMainActivityData() {

        collectionName = getIntent().getStringExtra(DataTransferBetweenActivities.COLLECTION);
        documentName = getIntent().getStringExtra(DataTransferBetweenActivities.DOCUMENT);

    }
}
