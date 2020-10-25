package com.murad.project1.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.currentStudentDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StudentDetails extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    ImageView profileImage;
    TextView studName,studCity,studAge;
    Button call,message,accept,userApplication;
    String phoneNumber;
    private int REQUEST_CALL=1;
    ProgressDialog pd;
    private ImageView Star1,Star2,Star3,Star4,Star5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);

        getSupportActionBar().hide();
        profileImage=findViewById(R.id.image);
        studName=findViewById(R.id.name);
        studCity=findViewById(R.id.city);
        studAge=findViewById(R.id.age);
        call=findViewById(R.id.btnCall);
        message=findViewById(R.id.messageBtn);
        accept=findViewById(R.id.acceptBtn);
        userApplication=findViewById(R.id.userApplication);
        Star1=findViewById(R.id.star1);
        Star2=findViewById(R.id.star2);
        Star3=findViewById(R.id.star3);
        Star4=findViewById(R.id.star4);
        Star5=findViewById(R.id.star5);



        initComponents();

        //handling call button
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();

            }
        });

        //handling accept button
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                 checkIfThereIsSpace();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetails.this);
                builder.setMessage("Are you sure about accepting "+currentStudentDetails.fname).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



            }
        });


        //handling student Edit application button
        userApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StudentDetails.this,studentApplicationActivity.class);
                startActivity(i);

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(StudentDetails.this);
        /*        new SweetAlertDialog(StudentDetails.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("write your message")
                        .setConfirmText("send")
                        .setCustomView(editText)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sendMessageToStudent(editText.getText().toString());
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();*/

              senMessageChooser();

            }
        });





        final SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        //this code to remove the accept button if the student was enrolled with the teacher
        if(Flags.Go_as_ACCEPTED_STUDENTS)
            accept.setVisibility(View.GONE);

    }

    private void checkIfThereIsSpace() {
        pd =new ProgressDialog(StudentDetails.this);
        pd.setMessage("Loading....");
        pd.show();

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(StudentDetails.this);
        String url =   Config.url + "getTeacherStudents.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                        if(arr.length()==14){
                            Toast.makeText(StudentDetails.this, "You already have 14 students you can't accept more", Toast.LENGTH_SHORT).show();

                        }else{

                            acceptStudent();
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
                Toast.makeText(StudentDetails.this, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email", Current_Teacher.email);



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
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;


        //marker
       double lat=Double.parseDouble(currentStudentDetails.lat);
       double lng=Double.parseDouble(currentStudentDetails.lng);
        LatLng sydney = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(sydney).title(currentStudentDetails.fname+"s"+" "+"location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f));
    }

    private void initComponents() {
        Glide.with(this).load(currentStudentDetails.profileImg).into(profileImage);
        studName.setText(currentStudentDetails.fname+" "+currentStudentDetails.lname);
        studCity.setText(currentStudentDetails.city);
        studAge.setText(currentStudentDetails.age);
        phoneNumber=currentStudentDetails.phone;
        getRate();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                call();

            }else{
                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void call() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dia="tel:"+phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dia)));

        }

    }


    private void acceptStudent() {
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Config.url + "acceptStudent.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        sendApprovementMessage();

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
               param.put("studentEmail",currentStudentDetails.studentEmail);
               param.put("teacherEmail", Current_Teacher.email);
               param.put("studentName",studName.getText().toString());
               param.put("teacherName", Current_Teacher.fname+" "+ Current_Teacher.lname);
               param.put("date",currentStudentDetails.suggestedTime);
               param.put("nLessons","0");
               param.put("nLessonsLeft","0");
               param.put("studentId", String.valueOf(Currrent_Student.id));
               param.put("teacherId", String.valueOf(Current_Teacher.id));

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);






    }
    private void sendApprovementMessage(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "SendMessage.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                 //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        deleteStudentFromRequestList();


                    }

                    else
                    {
                        Toast.makeText(StudentDetails.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();

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
                Toast.makeText(StudentDetails.this, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                String currentDate = new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(new Date());
                String instructorName=Current_Teacher.fname+" "+Current_Teacher.lname;
                param.put("id", String.valueOf(Currrent_Student.id));
                param.put("teacherId", String.valueOf(Current_Teacher.id));
                param.put("student_email", currentStudentDetails.studentEmail);
                param.put("teacher_email",Current_Teacher.email);
                param.put("date",currentDate);
                param.put("lessonId","1999");
                param.put("name",instructorName);
                param.put("status","accepted");
                param.put("img",Current_Teacher.profileImg);
                param.put("post",instructorName +"has accepted your request , your training will start as soon as possible");

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }



    public void deleteStudentFromRequestList(){

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Config.url + "deleteRequest.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(StudentDetails.this, "the student has been accepted !", Toast.LENGTH_SHORT).show();
                         Flags.REFRESH=true;
                         finish();

                    }
                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
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
                param.put("email",currentStudentDetails.studentEmail);

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    private void sendMessageToStudent(String post){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "SendMessage.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(StudentDetails.this, "Sent", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(StudentDetails.this,"Failed",Toast.LENGTH_LONG).show();
                    }


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
                Toast.makeText(StudentDetails.this, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                String currentDate = new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(new Date());
                String instructorName=Current_Teacher.fname+" "+Current_Teacher.lname;
                param.put("id", String.valueOf(Currrent_Student.id));
                param.put("student_email", currentStudentDetails.studentEmail);
                param.put("teacher_email",Current_Teacher.email);
                param.put("date",currentDate);
                param.put("lessonId","1999");
                param.put("name",instructorName);
                param.put("status","message");
                param.put("img",Current_Teacher.profileImg);
                param.put("post",instructorName +": "+post);
                param.put("teacherId", String.valueOf(Current_Teacher.id));

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    private void getRate() {
        switch (currentStudentDetails.rate){
            case 1:
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                Star3.setVisibility(View.GONE);
                Star2.setVisibility(View.GONE);
                break;
            case 2:
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                Star3.setVisibility(View.GONE);
                break;
            case 3:
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                break;
            case 4:
                Star5.setVisibility(View.GONE);
                break;


        }

    }

    private void senMessageChooser(){

        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.message_chooser);
        LinearLayout sendMessageWithApp=(LinearLayout)dialog.findViewById(R.id.linearLayout5);
        LinearLayout sendMessageWhats=(LinearLayout) dialog.findViewById(R.id.linearLayout4);

        sendMessageWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri = Uri.parse("smsto:" + currentStudentDetails.phone);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));


            }
        });


        sendMessageWithApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = new EditText(StudentDetails.this);
                new SweetAlertDialog(StudentDetails.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("write your message")
                        .setConfirmText("send")
                        .setCustomView(editText)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sendMessageToStudent(editText.getText().toString());
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


            }
        });

      //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


    }



}
