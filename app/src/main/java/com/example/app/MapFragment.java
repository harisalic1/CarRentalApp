package com.example.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapFragment extends Fragment { // This fragment is the simplest way to place a map in an application.

   FusedLocationProviderClient fusedLocationProviderClient; //FusedLocationProviderClient means combining gps and google-translating-received-wlan-cellphone-tower-signals.
   Button btnLocation;
   TextView latitude, longitude;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false); //inflater draws our layout and specify which layout to use
        btnLocation = view.findViewById(R.id.btn_location); //findViewById is the method that finds the View by the ID it is given
        latitude = view.findViewById(R.id.latitude); //same as above just another method in this case latitude
        longitude = view.findViewById(R.id.longitude); // same as above except  method is longitude
        //Initialize location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());//Returns a single location fix representing the best estimate of the current location of the device.
        btnLocation.setOnClickListener(new View.OnClickListener() { // OnClickListener() interface has an onClick(View v) method that is called when the view (component) is clicked
            @Override
            public void onClick(View view) {
                //check the condition
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        //when permission is granted
                        //call method
                        getCurentLocation();
                }else
                {
                    //when permission is not granted, request permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
                }

            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && (grantResults.length > 0)&& (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED))
        {
            getCurentLocation();
        }else{
            //kada je permisija odbijena
            Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurentLocation() {

        //initialize lokacijski menadzer

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled
            //Get last location

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initilaize location

                    Location location = task.getResult();
                    if (location!=null){
                        //when location is not null, set latitude
                        latitude.setText(String.valueOf(location.getLatitude()));
                        //set longitude
                        longitude.setText(String.valueOf(location.getLongitude()));
                    }else{
                        //when location is null, initialize location request
                        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                                .setPriority(LocationRequest.QUALITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000)
                                .setNumUpdates(1);

                        //Initialize location call back

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //initilaize location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                latitude.setText(String.valueOf(location1.getLatitude()));
                                //set longitude
                                longitude.setText(String.valueOf(location1.getLongitude()));
                            }
                        };
                        //Request location updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            //when location service is not enabled
            //Open location settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
