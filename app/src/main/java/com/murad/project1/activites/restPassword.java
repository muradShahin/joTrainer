package com.murad.project1.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.murad.project1.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class restPassword extends AppCompatActivity {
    private EditText restMail;
    private Button restBtn;
    private FirebaseAuth firebaseAuth;
    private SweetAlertDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);
        restMail=findViewById(R.id.restMail);
        restBtn=findViewById(R.id.restBtn);
        firebaseAuth=FirebaseAuth.getInstance();

        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);

        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(restMail.getText().toString())) {
                    restMail.setError("required");

                } else {


                    FirebaseAuth.getInstance().sendPasswordResetEmail(restMail.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.show();
                                    Toast.makeText(restPassword.this, "password has been sent to your email", Toast.LENGTH_SHORT).show();
                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           finish();

                                       }
                                   }, 1000);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(restPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        }

}
