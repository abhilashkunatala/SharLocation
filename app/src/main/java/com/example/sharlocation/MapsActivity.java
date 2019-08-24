package com.example.sharlocation;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final long MIN_TIME=1000;// time interval to update the location it is milliseconds
    private final long MIN_DIST= 10;// distance after update the location
    private LatLng  latLng;// latitude and longitude
    public Button button;
    String message1 = "";
  //  private LatLng latLng1;
    TextView textView;
   // public String phoneNumber;
    //int reuestCode = 10000;

    SearchView searchView;
    SupportMapFragment mapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // asking the user to grant the permission to grant the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED );
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED );


            searchView =(SearchView)findViewById(R.id.sv_location);
            mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            textView = (TextView)findViewById(R.id.textview);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String location = searchView.getQuery().toString();
                    List<Address> addressList = null;
                    if(location != null || !location.equals("")){
                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        try{
                            addressList = geocoder.getFromLocationName(location,1);
                        }

                            catch (IOException e){
                                    e.printStackTrace();
                            }

                        Address address= addressList.get(0);
                        LatLng latLng =new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10  ));


                     }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });


            mapFragment.getMapAsync(this);





        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hello();


            }
        });

    }

/*
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
      //  LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try{
                        latLng= new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("MY Postion "));// going to to new lat and longi that is moving camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    String phoneNumber =message1;
                   
                    String  myLatitude= String.valueOf(location.getLatitude());
                    String myLongitude = String.valueOf(location.getLongitude());
                    String message = " Latitude = "+ myLatitude+" "+"longitude="+myLongitude+ "This is Abhilash Kuntala";
                    SmsManager smsManager =  SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null, message,null,null);



                }
                catch (Exception e){
                    e.printStackTrace();
                }



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DIST,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DIST,locationListener);


        }

        catch(SecurityException e){
            e.printStackTrace();
        }

    }

    public  void hello()
    {
        Intent intent = new Intent(this,Message1.class);
        startActivityForResult(intent ,5);

      // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==5){
            message1 = data.getStringExtra("key");
            textView.setText(message1);
//            phoneNumber.matches(message);


            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        }




    }
}
