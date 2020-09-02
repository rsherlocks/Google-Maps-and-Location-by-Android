package com.example.androishaper.location_get;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener, EasyPermissions.PermissionCallbacks, View.OnClickListener {
    LocationManager locationManager;
    String provider;
    Button button;
    LocationGps obj=new LocationGps();
    //String lat,lan;
    Location locationC;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        textView=findViewById(R.id.text);
        button.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//            EasyPermissions.requestPermissions(this, "Please Allow Permission location", 1, perms);
//
//
//            return;
//        }
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this,perms))
        {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationNetwork!=null) {

                onPermissionCheck(locationNetwork);
            }
            else if (locationGps!=null) {

            onPermissionCheck(locationGps);
        }
            else {
                textView.setText("Network & Gps is Null");
            }

        }
        else{
            EasyPermissions.requestPermissions(this,"Please Allow Permission Camera and Storage",1,perms);
        }





    }

    private void onPermissionCheck(Location location) {
        if (location == null) {
            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();

        } else {
            onLocationChanged(location);


        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);

    }


    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        String latt = Double.toString(lat);
        String lang = Double.toString(lng);
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(lat,lng,1);
            if(addressList!=null && addressList.size()>0)
            {
                String addressHolder = "";
                String addresslocality="";
                String addressPostal="";
                //
                addressPostal=addressList.get(0).getPostalCode();
                //

                addresslocality=addressList.get(0).getAddressLine(0);

                //
                addressHolder+=addressList.get(0).getFeatureName().toString()+", ";
                addressHolder+=addressList.get(0).getSubAdminArea().toString()+", ";
                addressHolder+=addressList.get(0).getAdminArea().toString()+".";

//             for (int i=0; i<addressList.get(0).getMaxAddressLineIndex(); i++)
//             {
//                 addressHolder=addressList.get(0).getAddressLine(i);
//             }

//                Toast.makeText(this, addressList.get(0).toString(), Toast.LENGTH_SHORT).show();
//                textView.setText(addressList.get(0).toString());
                if (addressPostal==null)
                {
                    textView.setText(addressHolder);
                }
                else {
                    textView.setText(addresslocality+".");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, latt + lang, Toast.LENGTH_SHORT).show();

//       obj.setLat(latt);
//       obj.setLang(lang);
//       this.lat=latt;
//       this.lan=lang;
       this.locationC=location;


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (locationNetwork!=null) {

            onPermissionCheck(locationNetwork);
        }
        else if (locationGps!=null) {

            onPermissionCheck(locationGps);
        }
        else {
            textView.setText("Network & Gps is Null");
        }




    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms))
        {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button)
        {
//            String lat=obj.getLat();
//            String lan=obj.getLang();
            Double lat = locationC.getLatitude();
            Double lng = locationC.getLongitude();
            String latt = Double.toString(lat);
            String lang = Double.toString(lng);
            Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addressList=geocoder.getFromLocation(lat,lng,1);
                if(addressList!=null && addressList.size()>0)
                {
                    Toast.makeText(this, addressList.get(0).toString(), Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(this, latt + lang, Toast.LENGTH_SHORT).show();
           Intent next=new Intent(this,MapsActivity.class);
           next.putExtra("lat",latt);
            next.putExtra("lan",lang);
            //startActivity(next);


        }

    }

}
