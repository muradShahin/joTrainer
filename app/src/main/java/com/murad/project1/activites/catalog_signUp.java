package com.murad.project1.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.murad.project1.R;
import com.skyfishjy.library.RippleBackground;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class catalog_signUp extends AppCompatActivity {
    CardView student,teacher;
    RippleBackground reipple1,reipple2;
    SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_sign_up);
        getSupportActionBar().hide();

        student=findViewById(R.id.studentCv);
        teacher=findViewById(R.id.teacherCv);
        reipple1=findViewById(R.id.ripple);
        reipple2=findViewById(R.id.ripple2);
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);

        //handling card view on click
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       pd.dismiss();
                       Intent i=new Intent(catalog_signUp.this,register.class);
                       i.putExtra("role","student");
                       startActivity(i);
                       finish();
                   }
               }, 1000);




            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              pd.show();
                new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       pd.dismiss();
                       Intent i=new Intent(catalog_signUp.this,register.class);
                       i.putExtra("role","teacher");
                       startActivity(i);
                   }
               }, 1000);


            }
        });

    }
}
