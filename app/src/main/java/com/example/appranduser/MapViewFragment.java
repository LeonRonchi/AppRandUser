package com.example.appranduser;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private TextView distanciaKm, latLong;
    private static final String MAP_VIEW_BUNDLE_KEY = "MAP_VIEW_BUNDLE_KEY";

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        mapView = view.findViewById(R.id.mapView);
        distanciaKm = view.findViewById(R.id.distanciaKm);
        latLong = view.findViewById(R.id.tvLatLong);
        distanciaKm.setText("0.0 KM");

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private double calculaDistanciaEmKM(double latOrigem, double lonOrigem, double latDestino, double lonDestino) {
        Location start = new Location("Start Point");
        start.setLatitude(latOrigem);
        start.setLongitude(lonOrigem);

        Location finish = new Location("Finish Point");
        finish.setLatitude(latDestino);
        finish.setLongitude(lonDestino);

        return start.distanceTo(finish) / 1000;  // Distance in kilometers
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setIndoorEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        LatLng userLatLng = new LatLng(MainActivity.latitudeUser, MainActivity.longitudeUser);
        double distanceKm = calculaDistanciaEmKM(
                MainActivity.latitudeDevice, MainActivity.longitudeDevice,
                MainActivity.latitudeUser, MainActivity.longitudeUser
        );
        distanciaKm.setText(String.format("%.2f KM", distanceKm));
        latLong.setText("Latitude: "+MainActivity.latitudeUser + " - Longitude: " +MainActivity.longitudeUser);
        googleMap.addMarker(new MarkerOptions().position(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12.0f));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
