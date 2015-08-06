package com.example.gjha.runity;

import android.content.Context;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    GPSTracker gps;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    Location startloc;
    LatLng startlatlng;
    LatLng destlatlng;
    LatLng midlatlng;
    Timer t;
    int TimeCounter = 0;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    ImageButton runbutton;
    Integer imageres=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_maps);
        runbutton = (ImageButton) findViewById(R.id.run_button);
        runbutton.setOnClickListener(runButttonHandler);

        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        imageres = 1;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            handleNewLocation(location);
        }
       // locationManager.requestLocationUpdates(bestProvider, 20000, 0, PendingIntent.getService());

        //    startlocation = locationManager.getLastKnownLocation(provider);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

      //  startlatlng = new LatLng(startlocation.getLatitude(),startlocation.getLongitude());

    }

    View.OnClickListener runButttonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            switch (imageres) {
                case 1:
                    runbutton.setImageResource(R.drawable.stopn);
                    imageres = 0;
                    t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    TextView textview = (TextView)findViewById(R.id.texttimerun);
                                    textview.setText(String.valueOf(TimeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
                                    TimeCounter++;
                                }
                            });

                        }
                    }, 1000, 1000);

                    gps = new GPSTracker(MapsActivity.this);
                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        startlatlng = new LatLng(latitude,longitude );
                        startloc = new Location("");
                        startloc.setLatitude(latitude);
                        startloc.setLongitude(longitude);

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(startlatlng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                        mMap.addMarker(new MarkerOptions().position(startlatlng).title("I am here!"));
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                        // \n is for new line
                       // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                    break;
                case 0:
                    runbutton.setImageResource(R.drawable.playn);
                    imageres = 1;
                    gps = new GPSTracker(MapsActivity.this);
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    destlatlng = new LatLng(latitude,longitude);
                    t.cancel();//stopping the timer when ready to stop.
                    Toast.makeText(MapsActivity.this, "The time you have run for" + String.valueOf(TimeCounter) + "s", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
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
            mMap.setMyLocationEnabled(true);
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
       /* if (startlatlng == null){
            startlatlng = new LatLng(12.9182596,77.6695326 );
        }*/
       startlatlng = new LatLng(12.9182596,77.6695326 );

        gps = new GPSTracker(MapsActivity.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        startlatlng = new LatLng(latitude,longitude);
        startlatlng = new LatLng(12.9182596,77.6695326 );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(startlatlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
        mMap.addMarker(new MarkerOptions().position(startlatlng).title("I am here!"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        gps = new GPSTracker(MapsActivity.this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        midlatlng = new LatLng(latitude,longitude);
        Location loc = new Location("");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);

        float distanceInMeters = loc.distanceTo(startloc);
        float distanceInKm = (distanceInMeters / 1000) ;

        TextView textview = (TextView)findViewById(R.id.textdistancerun);
        textview.setText((int) distanceInKm + "Km");

        //double distance = computeDistanceBetween(midlatlng, startlatlng);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(midlatlng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(midlatlng, 19));


    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }


    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
}
