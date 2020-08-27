package com.murad.project1.activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.murad.project1.R;

public class PermissionActivity extends AppCompatActivity {
Button grant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        grant=findViewById(R.id.button);



        //checking if the permission is granted successfully
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(PermissionActivity.this,Splash.class));

            finish();
            return;
        }

        //ask for permission to get the current location
        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });
    }

    public void getPermission(){

        Dexter.withActivity(PermissionActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
                        finish();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(PermissionActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is denied. you need to go to settings to allow permission")
                                    .setNegativeButton("cancel",null)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i=new Intent();
                                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            i.setData(Uri.fromParts("package",getPackageName(),null));

                                        }
                                    }).show();
                        }else{
                            Toast.makeText(PermissionActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.cancelPermissionRequest();
                    }
                }).check();

    }
}
