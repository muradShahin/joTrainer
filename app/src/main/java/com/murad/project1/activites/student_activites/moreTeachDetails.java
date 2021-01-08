package com.murad.project1.activites.student_activites;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.murad.project1.R;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentDetailsOfTeacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class moreTeachDetails extends Fragment {
    private static final int REQUEST_CALL =1 ;
    ImageView profileImage,carImg;
    TextView name,carType,experience,phone,Email,address,age;
    private ImageView Star1,Star2,Star3,Star4,Star5,phoneIcon;
    Button sendReq,cancelBtn;
    ProgressBar pr;
    String sessionDate;

    public moreTeachDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_more_teach_details, container, false);
        profileImage=v.findViewById(R.id.teachImg);
        name=v.findViewById(R.id.teachname);
        experience=v.findViewById(R.id.teachexp);
        phone=v.findViewById(R.id.teachphone);
        Email=v.findViewById(R.id.teachmail);
        address=v.findViewById(R.id.teachaddress);
        carImg=v.findViewById(R.id.teachcarImg);
        carType=v.findViewById(R.id.teachcarType);
        age=v.findViewById(R.id.teachage);
        Star1=v.findViewById(R.id.teachstar1);
        Star2=v.findViewById(R.id.teachstar2);
        Star3=v.findViewById(R.id.teachstar3);
        Star4=v.findViewById(R.id.teachstar4);
        Star5=v.findViewById(R.id.teachstar5);
        sendReq=v.findViewById(R.id.request);
        cancelBtn=v.findViewById(R.id.cancel);
        phoneIcon=v.findViewById(R.id.phoneImage);
        pr=v.findViewById(R.id.progressBarReq);
        pr.setVisibility(View.GONE);


        init();


        //changing the layout upon the status of the trainee
        //if the trainee is enrolled then send Request btn will be removed
        //and End Subscription  will appear
        checkIfEnrolledOrNot();



        //handling send request on click
        sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                sendDropCourseRequest();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you really wish to send quit request ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });

        phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", CurrentDetailsOfTeacher.email, null));
                intent.putExtra("android.intent.extra.SUBJECT", "Enter Subject Here");
                startActivity(Intent.createChooser(intent, "Select an email client"));
            }
        });

    return v;
    }



    private void checkIfEnrolledOrNot() {
     if(CurrentDetailsOfTeacher.ENROLLED_STUDENT) {
         sendReq.setVisibility(View.GONE);
         cancelBtn.setVisibility(View.VISIBLE);
     }
     else {
         sendReq.setVisibility(View.VISIBLE);
         cancelBtn.setVisibility(View.GONE);

     }
    }

    private void init(){
        Glide.with(getActivity()).load(CurrentDetailsOfTeacher.profileImg).into(profileImage);
        Glide.with(getActivity()).load(CurrentDetailsOfTeacher.carImg).into(carImg);
        name.setText(CurrentDetailsOfTeacher.fname+" "+CurrentDetailsOfTeacher.lname);
        experience.setText("Experience : "+CurrentDetailsOfTeacher.exp);
        phone.setText("0"+CurrentDetailsOfTeacher.phone);
        Email.setText(CurrentDetailsOfTeacher.email);
        address.setText(CurrentDetailsOfTeacher.city);
        carType.setText(CurrentDetailsOfTeacher.carType);
        age.setText("Date Of Birth : "+CurrentDetailsOfTeacher.age);

        switch (CurrentDetailsOfTeacher.rate){
            case "1":
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                Star3.setVisibility(View.GONE);
                Star2.setVisibility(View.GONE);
                break;
            case "2":
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                Star3.setVisibility(View.GONE);
                break;
            case "3":
                Star5.setVisibility(View.GONE);
                Star4.setVisibility(View.GONE);
                break;
            case "4":
                Star5.setVisibility(View.GONE);
                break;

        }


    }
    private void requestTeacher(){
        pr.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "setRequest.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        pr.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "your request has been sent successfully", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();}
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               pr.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pr.setVisibility(View.GONE);
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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("student_email", Currrent_Student.email);
                param.put("teacher_email",CurrentDetailsOfTeacher.email);
                param.put("date",sessionDate);
                param.put("studId",String.valueOf(Currrent_Student.id));
                param.put("teacherId",String.valueOf(CurrentDetailsOfTeacher.id));


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    public void showDate(){
        new SlyCalendarDialog()
                .setSingle(true).setCallback(new SlyCalendarDialog.Callback() {
            @Override
            public void onCancelled() {

            }

            @SuppressLint("WrongConstant")
            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                firstDate.set(Calendar.HOUR_OF_DAY, hours);
                firstDate.set(Calendar.MINUTE, minutes);
                sessionDate=new SimpleDateFormat(getString(R.string.timeFormat),Locale.getDefault()).format(firstDate.getTime());
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                 //requestTeacher();
                                checkIfAvailable();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to set request on"+" "+sessionDate+" ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



            }
        }).setBackgroundColor(Color.parseColor("#ffffff")).setHeaderColor(Color.parseColor("#f47b00"))
                .setSelectedTextColor(Color.parseColor("#f47b00"))
                .setSelectedColor(Color.parseColor("#ffffff")).show(getActivity().getSupportFragmentManager(),"date");

    }
    private void checkIfAvailable(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "checkIfNoTeacher.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {

                        Toast.makeText(getActivity(), "You already have a teacher!", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        requestTeacher();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pr.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pr.setVisibility(View.GONE);
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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("Semail", Currrent_Student.email);
                param.put("Temail",CurrentDetailsOfTeacher.email);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    private void sendDropCourseRequest() {
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "SendMessage.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

              //  Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");

                    if(data.equals("1"))
                    {
                        Toast.makeText(getActivity(), " request has been sent", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                    }
                    pr.setVisibility(View.GONE);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                pr.setVisibility(View.GONE);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pr.setVisibility(View.GONE);
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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                String currentDate = new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(new Date());
                String studentName= Currrent_Student.fname+" "+ Currrent_Student.lname;
                String postContent=studentName+" wants to drop the current course !";
                param.put("id", String.valueOf(Currrent_Student.id));
                param.put("teacherId", String.valueOf(CurrentDetailsOfTeacher.id));
                param.put("student_email", Currrent_Student.email);
                param.put("teacher_email",CurrentDetailsOfTeacher.email);
                param.put("date",currentDate);
                param.put("post",postContent);
                param.put("status","pending/drop");
                param.put("lessonId","1999");
                param.put("name",studentName);
                param.put("img", Currrent_Student.profileImg);


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                call();

            }else{
                Toast.makeText(getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void call() {

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dia="tel:"+phone.getText().toString();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dia)));

        }

    }
}
