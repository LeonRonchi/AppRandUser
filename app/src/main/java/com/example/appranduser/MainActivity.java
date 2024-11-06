package com.example.appranduser;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ImageButton btUserFragment, btMapViewFragment;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private static UserFragment userFragment = new UserFragment();
    private static MapViewFragment mapViewFragment = new MapViewFragment();
    private LocationManager locationManager;
    private LocationListener locationListener;

    private PermissionsMarshmallow permissionsMashmallow = new PermissionsMarshmallow(this);

    private String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };


    public static double
            latitudeUser = 0, longitudeUser = 0,
            latitudeDevice = 0, longitudeDevice = 0;

    public static Result user = null;
    public static boolean[] selectedContriesPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frameLayout = findViewById(R.id.frameLayout);
        btUserFragment = findViewById(R.id.btUserFragment);
        btMapViewFragment = findViewById(R.id.btMapViewFragment);
        fragmentManager = getSupportFragmentManager();

        btUserFragment.setOnClickListener(e -> {
            trocarFragment(userFragment);
            //btUserFragment.setBackgroundColor(808080);
        });

        btMapViewFragment.setOnClickListener(e -> {
            trocarFragment(mapViewFragment);
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitudeDevice = location.getLatitude();
                longitudeDevice = location.getLongitude();
            }
        };
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameLayout.getId(), userFragment);
        fragmentTransaction.commit();
        checkPermissionGranted();
        iniciarListenerLocalizacao();
    }

    private void iniciarListenerLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);
    }

    private void trocarFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


    private void checkPermissionGranted() {
        if (permissionsMashmallow.hasPermissions(PERMISSIONS)) {
        } else {
            permissionsMashmallow.requestPermissions(PERMISSIONS, 2);
        }
    }
}