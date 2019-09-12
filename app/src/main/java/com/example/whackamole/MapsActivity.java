package com.example.whackamole;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Map;

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

       // getPlayersData();

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

        // Add a marker in Sydney and move the camera
        LatLng israel = new LatLng(LAT_ISRAEL, LNG_ISRAEL);
        //LatLng SAGIV = new LatLng(32.2344278, 34.7740178);
        mMap.addMarker(new MarkerOptions().position(israel).title("Marker in Israel").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(israel, ZOOM_IN_OUT));
       // mMap.addMarker(new MarkerOptions().position(SAGIV).title("Sagiv").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(SAGIV));
    }
}
