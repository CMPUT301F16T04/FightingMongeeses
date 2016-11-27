package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * This view shows all requests by default, and then upon user input shows the user all requests
 * centered around the input in a 2km radius
 */
public class RideView extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, ACallback {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Marker> markers;
    private String user;
    private LatLng lastKnownPlace;
    private LatLng restrictToPlace;
    private RideController rides;
    private String rideID;
    private boolean firstLoad;
    private boolean test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firstLoad = false;
        rides = new RideController(this);
        if (mGoogleApiClient == null && !test) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        markers = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if(extra != null){
            user = extra.getString("username");
            rideID = extra.getString("rideID");
        }
        rideID = "513eb9ed-9c45-4468-97ae-73cdcfe5619a";
    }

    protected void onStart() {
        if(!test){
            mGoogleApiClient.connect();
        }

        super.onStart();
    }
    protected void onResume(){

        super.onResume();
        if(!test) {
            mGoogleApiClient.reconnect();
        }
    }

    protected void onPause(){
        if(!test) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    protected void onStop() {
        if(!test) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    // Need this for ConnectionsCallback, doesn't need to do anything AFAIK
    // If a map view does live tracking it might be more useful
    public void onConnectionSuspended(int i){

    }
    @Override
    //On connected listener, required to be able to zoom to users location at login
    public void onConnected(Bundle connectionHint){
        if(rideID != null) {
            rides.findRide(rideID);
        }
    }

    // This should eventually be updated to quit the app or go back to a view that doesn't require geolocation
    // Currently this shows an alert notifying the user that the connection failed
    public void onConnectionFailed(ConnectionResult result) {
        new AlertDialog.Builder(this)
                .setTitle("Connection Failure")
                .setMessage(result.getErrorMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    // Basic thing to do when the map is setup
    // Callback for when the googleMap object is set up
    public void onMapReady(GoogleMap googleMap) {
        // Make our map a map
        map = googleMap;

        Calendar current = Calendar.getInstance();

        // Add night view for nice viewing when it's dark out
        // Dark styling is easier on the eyes
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        if (nightTime(time.format(current.getTime()))) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_night_style);
            map.setMapStyle(style);
        }

        // Let's listen for clicks on our markers to display information
//        map.setOnMarkerClickListener(showInfoWindow);

//        map.setInfoWindowAdapter(displayRequest);

    }


    OnMarkerClickListener showInfoWindow = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return false;
        }
    };

    /**
     * Allows us to display arbitrary data in the info window of a google marker
     */
//    GoogleMap.InfoWindowAdapter displayRequest = new GoogleMap.InfoWindowAdapter() {
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return null;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            Request currentRequest = (Request) marker.getTag();
//            View infoView = getLayoutInflater().inflate(R.layout.info_window_fragment, null);
//            TextView pickUpView = (TextView) infoView.findViewById(R.id.pickup);
//            TextView dropoffView = (TextView) infoView.findViewById(R.id.dropoff);
//            TextView fareView = (TextView) infoView.findViewById(R.id.amount);
//            TextView pickupTIme = (TextView) infoView.findViewById(R.id.pickupTime);
//
//            SimpleDateFormat date = new SimpleDateFormat("hh:mm on DD/MM/yyyy");
//            pickUpView.setText("Pickup: " + currentRequest.getPickup());
//            dropoffView.setText("Drop-off: " + currentRequest.getDropoff());
//            pickupTIme.setText("Pickup time: " + date.format(currentRequest.getDate()));
//            fareView.setText("Amount: $" + currentRequest.getFare());
//
//            return infoView;
//        }
//    };

    /**
     * Callback for an outside Class to get the view to check for new data
     * This interface is used when a controller updates it's data
     * It will call this callback on whoever instantiated that controller
     */
    public void update(){
        try {
            Ride ride = rides.getRide(rideID);
            showRide(ride);
        } catch (Exception e){
            Log.i("Update failed", String.valueOf(e));
        }
    }

    public void showRide(Ride ride){
        Marker pickup = map.addMarker(new MarkerOptions().position(ride.getPickupCoords()));
        pickup.setTitle(ride.getPickupAddress());
        markers.add(pickup);

        Marker dropoff = map.addMarker(new MarkerOptions().position(ride.getDropOffCoords()));
        pickup.setTitle(ride.getDropOffAddress());
        markers.add(dropoff);

    }

    /**
     * A function to check if it's night time or not
     * Plus this is easier on the eyes in night
     * @param time the time
     * @return boolean
     */
    private boolean nightTime(String time){
        try {
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(time);
            Date nightTime = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
            Date earlyMorning = new SimpleDateFormat("HH:mm:ss").parse("6:00:00");
            return currentTime.getTime() > nightTime.getTime() || currentTime.getTime() < earlyMorning.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public int countMarkers(){
        if(markers != null){
            return markers.size();
        }
        return 0;
    }
}


// Example of how to remove a marker using this as a hack for onLongClick
//        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                marker.remove();
//            }
//        });

// This method is not needed for loading requests, but demonstrates how to drop a marker
// map.setOnMapLongClickListener(addMarker);

//    // OnLongClickListener for a marker
//    GoogleMap.OnMapLongClickListener addMarker = new GoogleMap.OnMapLongClickListener(){
//        @Override
//        public void onMapLongClick(LatLng pos){
//            if(map != null) {
//                // Move camera to long click position
//                map.moveCamera(CameraUpdateFactory.newLatLng(pos));
//
//                // Drop marker at location
//                map.addMarker(new MarkerOptions().position(pos));
//            }
//        }
//    };