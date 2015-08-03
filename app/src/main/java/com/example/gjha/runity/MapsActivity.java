package com.example.gjha.runity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
   // TextView txtLat;
    String lat;
    String provider;
    LatLng startlocation;
    LatLng endlocation;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    ImageButton runbutton;
    Integer imageres=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        runbutton = (ImageButton)findViewById(R.id.run_button);
        runbutton.setOnClickListener(runButttonHandler);
        setUpMapIfNeeded();
        imageres = 1;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    View.OnClickListener runButttonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            switch (imageres) {
                case 1:
                    runbutton.setImageResource(R.drawable.pause);
                    imageres = 0;
                    break;
                case 0:
                    runbutton.setImageResource(R.drawable.play);
                    imageres = 1;
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng runlocation = new LatLng(12.9182596,77.6695326 );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(runlocation, 19));
        mMap.addMarker(new MarkerOptions().position(runlocation).title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng runlocation = new LatLng(location.getLatitude(),location.getLongitude() );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(runlocation, 19));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
