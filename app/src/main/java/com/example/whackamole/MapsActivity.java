package com.example.whackamole;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.provider.ContactsContract;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

import Data.DataCols;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int ZOOM_IN_OUT = 8;
    private final double LAT_ISRAEL = 31.2344278;
    private final double LNG_ISRAEL = 34.7740178;
    private List<Map<String, Object>> playersData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getPlayersData();

    }

    private void getPlayersData() {

        playersData = (List<Map<String, Object>>) getIntent().getSerializableExtra("List");

        System.out.println(playersData);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng israel = new LatLng(LAT_ISRAEL, LNG_ISRAEL);

        addPlayersMarkers(mMap);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel, ZOOM_IN_OUT));


    }

    private void addPlayersMarkers(GoogleMap mMap) {


            for (int i = 0; i < playersData.size() && i < HallOfFame.NUM_OF_HIGLIGHTS_PLAYER; i++) {

                Map<String, Object> player = playersData.get(i);

                LatLng latLng = new LatLng((Double) player.get(DataCols.LAT), (Double) player.get(DataCols.LNG));
                String playerDetails =
                        "Name: " + player.get(DataCols.NAME).toString()
                                + " Points: "
                                + player.get(DataCols.POINTS).toString()
                                + " Misses: "
                                + player.get(DataCols.MISSES).toString();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Player Stats: ").snippet(playerDetails).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }





    }
}
