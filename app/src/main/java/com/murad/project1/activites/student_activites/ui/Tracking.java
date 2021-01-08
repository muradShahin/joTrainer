package com.murad.project1.activites.student_activites.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.currentStudentDetails;

public class Tracking extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    private LatLng teacherLocation;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ImageView driverImage;
    private TextView driverName,carType;
    private CardView callCard,whatsAppCard;

    private static final int REQUEST_CALL=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        getSupportActionBar().hide();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Locations");
        driverImage=findViewById(R.id.driverImage);
        driverName=findViewById(R.id.driverName);
        carType=findViewById(R.id.carType);
        callCard=findViewById(R.id.callDriver);
        whatsAppCard=findViewById(R.id.whatsCard);
       // updateLocation();
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
    }

    private void init() {

        if(Current_Teacher.profileImg !=null){
            Glide.with(this)
                    .load(Current_Teacher.profileImg)
                    .into(driverImage);
        }

        if(Current_Teacher.carType !=null){
            carType.setText(Current_Teacher.carType);
        }

        if(Current_Teacher.lname !=null && Current_Teacher.fname !=null){
            driverName.setText(Current_Teacher.fname +" "+Current_Teacher.lname);
        }


        callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        whatsAppCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendViaWhatsApp();
            }
        });


    }

    private void updateLocation() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){

                    if(key.child("email").getValue().toString().equals(Current_Teacher.email)){

                        Log.i("test","reached");
                        double lat=Double.parseDouble(key.child("lat").getValue().toString());
                        double lng=Double.parseDouble(key.child("lng").getValue().toString());

                        Current_Teacher.lat=lat+"";
                        Current_Teacher.lng=lng+"";

                        teacherLocation=new LatLng(lat,lng);


                      LatLng location=new LatLng(lat,lng);
                        map.clear();
                        MarkerOptions markerOptions=new MarkerOptions();
                        markerOptions.position(location);

                        map.addMarker(markerOptions);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17.0f));

                        break;


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    /*    if(getIntent() !=null){
            if(getIntent().getParcelableExtra("teacherLocation") !=null){
                teacherLocation=getIntent().getParcelableExtra("teacherLocation");
            }else{
                teacherLocation=new LatLng(-34.0,151.0);
            }
        }*/

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){

                    if(key.child("email").getValue() !=null) {
                        if (key.child("email").getValue().toString().equals(Current_Teacher.email)) {

                            Log.i("test", "reached");
                            double lat = Double.parseDouble(key.child("lat").getValue().toString());
                            double lng = Double.parseDouble(key.child("lng").getValue().toString());

                            Current_Teacher.lat = lat + "";
                            Current_Teacher.lng = lng + "";

                            //  teacherLocation=new LatLng(lat,lng);


                            if (lat != 31.9558133 && lng != 35.9118567) {
                                LatLng location = new LatLng(lat, lng);
                                map.clear();
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car_marker);
                                map.addMarker(new MarkerOptions().position(location).title("current location").icon(icon));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14.0f));
                            }

                            break;


                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void call() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dia="tel:"+Current_Teacher.phone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dia)));

        }

    }

    private void sendViaWhatsApp(){
        Uri uri = Uri.parse("smsto:" + Current_Teacher.phone);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }
}