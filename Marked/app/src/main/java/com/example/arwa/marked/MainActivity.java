package com.example.arwa.marked;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (googleServiceAvailable()) {
            initMap();
        } else {
            // no google maps
        }

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    //check if service is available
    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();

        } else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
//        mGoogleMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);

        if(mGoogleMap !=null){
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

               @Override
                public View getInfoWindow(Marker marker){
                   return null;
                }

                @Override
                public View getInfoContents(Marker marker){
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    ImageView img = (ImageView) v.findViewById(R.id.tv_img);
                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitude: " + ll.latitude);
                    tvLng.setText("Longitude: " + ll.longitude);
                  //  if(marker.getTitle() == "Dopravo") {
                        img.setImageResource(R.mipmap.marked_icon);
                    //}

                    return v;
                }




            });
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    Intent intent = new Intent(MainActivity.this, EditEventActivity.class);
                    startActivity(intent);

                }
            });
        }

        setMarker("Dopravo", 24.7188572, 46.6704415);
        setMarker("Royal Mall", 24.7235064, 46.6646761);

        goToLocationZoom(24.7188572, 46.6704415, 15);



//     zoom in to current location:

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        mGoogleApiClient.connect();

    }



    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);



    }
//    public void getCurrentLatLng() {
//
//        GPSTracker gps = new GPSTracker(this);
//        // check if GPS enabled
//        if (gps.isGPSEnabled) {
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//            LatLng currentLatLng = new LatLng(latitude, longitude);
//            System.out.println(currentLatLng);
//        } else {
//            gps.showSettingsAlert();
//        }
//    }
    Marker marker;


    public void geoLocate(View view) throws IOException {

        EditText et = (EditText) findViewById(R.id.editText2);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lat, lng, 15);

        setMarker(locality, lat, lng);

    }
    Marker marker1;
    Marker marker2;
    private void setMarker(String locality, double lat, double lng) {
       // if(marker !=null){
           // marker.remove();
       // }
        //Add marker
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(new LatLng(lat, lng));

        marker =   mGoogleMap.addMarker(options);

        if(marker1 == null){
            marker1 = mGoogleMap.addMarker(options);
        }else if(marker2 == null){
            marker2 = mGoogleMap.addMarker(options);
        }
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connection Established", Toast.LENGTH_LONG).show();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            mGoogleMap.animateCamera(update);
        }
    }

//    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, "Info window clicked",
//                Toast.LENGTH_SHORT).show();
//    }
}

