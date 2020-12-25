package com.murad.project1.activites.student_activites;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.activites.LoginActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class studentGate extends AppCompatActivity {

    private ImageView profImage;
    private TextView studName;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_gate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //init the nav drawer profile image and student name
        initDrawer(navigationView);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    private void initDrawer(NavigationView navigationView) {
        View headerVew=navigationView.getHeaderView(0);
        studName=headerVew.findViewById(R.id.studentName);
        profImage=headerVew.findViewById(R.id.profileImg);


        assignDataToComponent();

    }

    private void assignDataToComponent() {
     studName.setText(Currrent_Student.fname+" "+ Currrent_Student.lname);
        Glide.with(this).load(Currrent_Student.profileImg).into(profImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_gate, menu);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==R.id.action_settings){
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                 SweetAlertDialog pd = new SweetAlertDialog(studentGate.this, SweetAlertDialog.PROGRESS_TYPE);
                                pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
                                pd.setTitleText("Logging out...");
                                pd.setCancelable(false);
                                pd.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                                        firebaseAuth.signOut();
                                        Intent i=new Intent(studentGate.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();

                                    }
                                }, 2000);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(studentGate.this);
                builder.setMessage("are you sure you want to quit ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            return true;
        }






        return super.onOptionsItemSelected(item);
    }
}
