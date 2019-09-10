package com.example.whackamole;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Data.DataManagement;
import Data.FireStoreCallback;

public class HallOfFame extends AppCompatActivity {

    private String collectionName;
    private String documentName;
    private DataManagement dataManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_of_fame);

        getMainActivityData();

        dataManagement = DataManagement.createSingletonDM();


        addRowsToTable();


    }

    private void addRowsToTable() {

        List<Map<String, Object>> playersList = new LinkedList<>();

        /*FirseStore loads data asynchronously*/
        dataManagement.readData(new FireStoreCallback() {
            @Override
            public void onCallback(List<Map<String, Object>> returnedPlayersList) {
                fillTableWithData(returnedPlayersList);
            }
        }, collectionName, playersList);


    }

    private void fillTableWithData(List<Map<String, Object>> returnedPlayersList) {


        sortListByPoints(returnedPlayersList);

        TableLayout table = findViewById(R.id.playersTable);

        for (int i = 0; i < 10; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(80, 0, 0, 0);

            TextView tv1 = new TextView(this);
            tv1.setTextSize(1, 30);
            tv1.setText(returnedPlayersList.get(i).get("Name").toString());
            tv1.setLayoutParams(params);

            TextView tv2 = new TextView(this);
            tv2.setTextSize(1, 30);
            tv2.setText(returnedPlayersList.get(i).get("Points").toString());
            tv2.setLayoutParams(params);


            TextView tv3 = new TextView(this);
            tv3.setTextSize(1, 30);
            tv3.setText(returnedPlayersList.get(i).get("Misses").toString());
            tv3.setLayoutParams(params);

            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);

            table.addView(row, i+1);
        }
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


    private void getMainActivityData() {
        collectionName = getIntent().getStringExtra(DataTransferBetweenActivities.COLLECTION);
        documentName = getIntent().getStringExtra(DataTransferBetweenActivities.DOCUMENT);
    }
}
