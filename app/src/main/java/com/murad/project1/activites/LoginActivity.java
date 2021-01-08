package com.murad.project1.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.activites.student_activites.studentGate;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.PerformanceModels;
import com.murad.project1.supportClasses.Role;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    TextView register,forgetPass;
    EditText email,password;
    Button lgnBtn;
    private LocationCallback locationCallback;
    double lat1;
    double lat2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private FirebaseAuth firebaseAuth;
    String roleOfUser;
    String mailFromIntent,passFromIntent;
    SweetAlertDialog pd;
    private String locationUpdatPhpFile;

    private boolean isApprovedTrainer=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        getSupportActionBar().hide();

        //declaring the components
        register=findViewById(R.id.regist);
        email=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        forgetPass=findViewById(R.id.forget);
        lgnBtn=findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);



        getDeviceLocation();
        //checking if the gps is enable or not(for getting the current location)
        checkGps();
        //getting values from register activity
        if(Flags.Sent_from_register) {
            if(getIntent().getExtras() != null) {
                mailFromIntent = getIntent().getExtras().getString("email");
                passFromIntent = getIntent().getExtras().getString("password");
                email.setText(mailFromIntent);
                password.setText(passFromIntent);
            }
        }

       //handling login button on click
        lgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmail()){
                    if(checkPass()){
                        pd.show();
                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                       //CHECK IF TEACHER OR STUDENT FIRST
                                        checkRole();



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pd.dismissWithAnimation();
                                Toast.makeText(LoginActivity.this,"incorrect email or password", Toast.LENGTH_SHORT).show();
                                email.setError("not valid");
                                password.setError("not valid");

                            }
                        });

                    }else{
                        password.setError("required");
                        pd.dismiss();
                    }
                }else{
                    email.setError("required");
                    pd.dismiss();
                }




            }//the end of firebase auth code

        });//the end of on click block



//        forgetPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog dialog=new Dialog(LoginActivity.this);
//                dialog.setContentView(R.layout.forgetpass);
//                EditText email=dialog.findViewById(R.id.email);
//                Button btn=dialog.findViewById(R.id.button5);
//
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Log.d("test forget pass", "Email sent.");
//                                            Toast.makeText(LoginActivity.this,"Email has been sent",Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss();
//
//                                        }
//                                    }
//                                });
//                    }
//                });
//
//
//                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//
//                dialog.show();
//
//
//
//            }
//        });
//


        //register on click handling
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, catalog_signUp.class);
                startActivity(i);
            }
        });

    forgetPass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(LoginActivity.this,restPassword.class);
            startActivity(i);

        }
    });

    }

    private void checkRole() {

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "checkRole.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                Log.i("testLogin",s);




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                        if(data.equals("1")) {
                            //Role.role="student";
                            locationUpdatPhpFile="updateStudentLocation.php";
                            insertCurrentLocationToDb("student");



                                }

                            else{
                           // Role.role="teacher";
                            locationUpdatPhpFile="updateLocation.php";
                            insertCurrentLocationToDb("teacher");


                        }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",firebaseAuth.getCurrentUser().getEmail());



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);





    }

    private void getStudentData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "getStudentData.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id = x.getInt("id");
                        String email = x.getString("email");
                        String fname = x.getString("fname");
                        String lname = x.getString("lname");
                        String password = x.getString("password");
                        String phone = x.getString("phone");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String profileImg = x.getString("profileImg");
                        String rate = x.getString("rate");


                            Currrent_Student.id=id;
                            Currrent_Student.email = email;
                            Currrent_Student.fname = fname;
                            Currrent_Student.lname = lname;
                            Currrent_Student.password = password;
                            Currrent_Student.phone = phone;
                            Currrent_Student.age = age;
                            Currrent_Student.profileImg = profileImg;
                            Currrent_Student.city = city;
                            Currrent_Student.rate=rate;
                            Currrent_Student.role="student";
                            roleOfUser="student";
                            Role.role="student";
                            Flags.Go_as_Student = true;
                            Toast.makeText(LoginActivity.this, Currrent_Student.fname, Toast.LENGTH_SHORT).show();





                    }
                        getPerformance();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",firebaseAuth.getCurrentUser().getEmail());



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }

    private void getUserData() {



        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "getUserData.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id = x.getInt("id");
                        String email = x.getString("email");
                        String fname = x.getString("fname");
                        String lname = x.getString("lname");
                        String password = x.getString("password");
                        String phone = x.getString("phone");
                        String role = x.getString("role");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String exp = x.getString("exp");
                        String office = x.getString("office");
                        String carType = x.getString("carType");
                        String carImg = x.getString("carImg");
                        String profileImg = x.getString("profileImg");
                        String rate = x.getString("rate");
                        String isApproved=x.getString("isApproved");

                        if(isApproved.equalsIgnoreCase("true"))
                            isApprovedTrainer=true;
                        else
                            isApprovedTrainer=false;


                            Current_Teacher.id=id;
                            Current_Teacher.email = email;
                            Current_Teacher.fname = fname;
                            Current_Teacher.lname = lname;
                            Current_Teacher.password = password;
                            Current_Teacher.phone = phone;
                            Current_Teacher.age = age;
                            Current_Teacher.profileImg = profileImg;
                            Current_Teacher.carImg = carImg;
                            Current_Teacher.carType = carType;
                            Current_Teacher.exp = exp;
                            Current_Teacher.office=office;
                            Current_Teacher.city = city;
                            Current_Teacher.rate=rate;
                            Current_Teacher.role="teacher";
                            roleOfUser="teacher";
                            Role.role="teacher";

                        Flags.Go_as_Teacher = true;





                    }

                    if(isApprovedTrainer) {
                        Intent i = new Intent(LoginActivity.this, Index.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"you are not approved as a trainer yet!",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",firebaseAuth.getCurrentUser().getEmail());



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);




    }
    public boolean checkEmail(){
        boolean flag=true;
        if(TextUtils.isEmpty(email.getText().toString()))
            flag=false;
        return flag;
    }
    public boolean checkPass(){
        boolean flag=true;
        if(TextUtils.isEmpty(password.getText().toString()))
            flag=false;
        return flag;
    }

    public void checkGps(){


        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient= LocationServices.getSettingsClient(LoginActivity.this);
        Task<LocationSettingsResponse>task=settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(LoginActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();

            }
        });

        task.addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                    try {
                        resolvableApiException.startResolutionForResult(LoginActivity.this,51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    lastKnownLocation=task.getResult();
                    if(lastKnownLocation!=null){
                        lat1=lastKnownLocation.getLatitude();
                        lat2=lastKnownLocation.getLongitude();

                    }else{
                        final LocationRequest locationRequest=LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback =new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if(locationResult==null) {
                                    return;
                                }
                                lastKnownLocation=locationResult.getLastLocation();
                                lat1=lastKnownLocation.getLatitude();
                                lat2=lastKnownLocation.getLongitude();

                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);


                    }
                } else{

                    Toast.makeText(LoginActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void insertCurrentLocationToDb(String role){


        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + locationUpdatPhpFile;


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        if(role.equalsIgnoreCase("student"))
                            getStudentData();
                        else
                            getUserData();

                    }

                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();

                    param.put("lat",lat1+"");
                    param.put("lng",lat2+"");
                    param.put("email",firebaseAuth.getCurrentUser().getEmail());


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void getPerformance(){


        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url =   Config.url + "getPerformance.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

               //  Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");
                    int numberOfRows=arr.length();
                    int sumCont=0;
                    int sumShifting=0;
                    int sumPrio=0;
                    int sumPark=0;
                    int sumMoni=0;


                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int controllingD= x.getInt("controling");
                        int shiftingD=x.getInt("shifting");
                        int prioritesD=x.getInt("priorities");
                        int parkingD=x.getInt("parking");
                        int monitoringD=x.getInt("monitoring");
                        sumCont+=controllingD;
                        sumShifting+=shiftingD;
                        sumPrio+=prioritesD;
                        sumPark+=parkingD;
                        sumMoni+=monitoringD;

                    }

                    if(numberOfRows>0) {
                        PerformanceModels.controling = sumCont / numberOfRows;
                        PerformanceModels.shifting = sumShifting / numberOfRows;
                        PerformanceModels.monitoring = sumMoni / numberOfRows;
                        PerformanceModels.parking = sumPark / numberOfRows;
                        PerformanceModels.priorites = sumPrio / numberOfRows;
                    }
                    Intent i = new Intent(LoginActivity.this, studentGate.class);
                    startActivity(i);
                    finish();







                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(LoginActivity.this, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email", Currrent_Student.email);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() !=null){
            pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
            pd.setTitleText("Loading");
            pd.setCancelable(false);
            pd.show();
            checkRole();
        }

    }
}


