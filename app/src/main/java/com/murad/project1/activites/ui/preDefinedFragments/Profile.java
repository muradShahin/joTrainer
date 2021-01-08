package com.murad.project1.activites.ui.preDefinedFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerAppointmentsMessage;
import com.murad.project1.RecyclersView.RecyclerCancellationRequests;
import com.murad.project1.RecyclersView.RecyclerTodaysLessons;
import com.murad.project1.RecyclersView.RecyclerViewTeacherStudents;
import com.murad.project1.contractClasses.Messages;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.UsersClasses.TeacherStudents;
import com.murad.project1.activites.LoginActivity;

import com.murad.project1.lessonsClasses.Lessons;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.DriverClass;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    ImageView profileImage,carImg,logOut;
    TextView name,carType,rating,experience,info,schedual,students,phone,Email,address,notifications;
    RecyclerView recSchedule,recStudents;
    ScrollView recInfo;
    RelativeLayout requests;
    ArrayList<TeacherStudents> studentsArray=new ArrayList<>();
    ArrayList<Lessons>Arraylessons=new ArrayList<>();
    int numberOfNotification=0;
    RelativeLayout notificationLayout;
    SweetAlertDialog pd;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private BubbleShowCase bubbleShowCase;
    int countCase=1;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage=v.findViewById(R.id.profileImg);
        name=v.findViewById(R.id.name);
        rating=v.findViewById(R.id.rate);
        experience=v.findViewById(R.id.exp);
        info=v.findViewById(R.id.personalinfobtn);
        schedual=v.findViewById(R.id.schedual);
        students=v.findViewById(R.id.myStuds);
        recInfo=v.findViewById(R.id.recInfo);
        recSchedule=v.findViewById(R.id.recSched);
        recStudents=v.findViewById(R.id.recStudents);
        requests=v.findViewById(R.id.requestLay);
        phone=v.findViewById(R.id.phone);
        Email=v.findViewById(R.id.mail);
        address=v.findViewById(R.id.address);
        carImg=v.findViewById(R.id.carImg);
        logOut=v.findViewById(R.id.logOff);
        notificationLayout=v.findViewById(R.id.notfiLayout);
        notifications=v.findViewById(R.id.notif);
        notificationLayout.setVisibility(View.GONE);

        //init firebase
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Locations");

        addUserLocationTofireBase();


        pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);


        initData();

        //recycler view settings
        recStudents.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        recSchedule.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));


        //CONSTRAINTS ON RECYCLER VIEWS....RECYCLER VIEW OF INFORMATION MUST BE VISIBLE AT THE FIRST
        recInfo.setVisibility(View.VISIBLE);
        info.setTextColor(getResources().getColor(R.color.myOrange));
        recSchedule.setVisibility(View.GONE);
        recStudents.setVisibility(View.GONE);

        //handling info , schedual,students buttons
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setTextColor(getResources().getColor(R.color.myOrange));
                schedual.setTextColor(getResources().getColor(R.color.quantum_grey700));
                students.setTextColor(getResources().getColor(R.color.quantum_grey700));
                recInfo.setVisibility(View.VISIBLE);
                recSchedule.setVisibility(View.GONE);
                recStudents.setVisibility(View.GONE);
                studentsArray.clear();
                Arraylessons.clear();
            }
        });
        schedual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setTextColor(getResources().getColor(R.color.quantum_grey700));
                schedual.setTextColor(getResources().getColor(R.color.myOrange));
                students.setTextColor(getResources().getColor(R.color.quantum_grey700));
                recInfo.setVisibility(View.GONE);
                recSchedule.setVisibility(View.VISIBLE);
                recStudents.setVisibility(View.GONE);
                Arraylessons.clear();
                studentsArray.clear();
                getTodaysLessons();



            }
        });

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setTextColor(getResources().getColor(R.color.quantum_grey700));
                schedual.setTextColor(getResources().getColor(R.color.quantum_grey700));
                students.setTextColor(getResources().getColor(R.color.myOrange));
                recInfo.setVisibility(View.GONE);
                recSchedule.setVisibility(View.GONE);
                recStudents.setVisibility(View.VISIBLE);
                Arraylessons.clear();
                studentsArray.clear();
                getTeacherStudents();


            }
        });


        //handling requests relative layout on click
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profile_to_allRequests);
            }
        });

        //handling on log out button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                pd= new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                                pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
                                pd.setTitleText("logging out...");
                                pd.setCancelable(false);
                                pd.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.dismiss();
                                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                                        firebaseAuth.signOut();
                                        Intent i=new Intent(getActivity(), LoginActivity.class);
                                        startActivity(i);
                                        getActivity().finish();

                                    }
                                }, 2000);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("are you sure you want to quit ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });




       return v;
    }

    private void addUserLocationTofireBase() {
        DriverClass driverClass=new DriverClass();

        if(Current_Teacher.email !=null)
           driverClass.setEmail(Current_Teacher.email);


        if(Current_Teacher.lat !=null)
            driverClass.setLat(Current_Teacher.lat);
        else
            driverClass.setLat("31.9564013");

        if(Current_Teacher.lng!=null)
            driverClass.setLat(Current_Teacher.lng);
        else
            driverClass.setLng("35.8453127");

   databaseReference.child("email");
   databaseReference.orderByChild("email").equalTo(Current_Teacher.email);
   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if(dataSnapshot.exists())
               Log.i("existsTest","exist");
           else{
               databaseReference.child(Current_Teacher.lname+Current_Teacher.fname+Current_Teacher.phone).setValue(driverClass)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Log.i("testFirebase","done successfully");
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.i("testFirebase","fail");

                   }
               });
           }

       }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

       }
   });



    }

    public void initData(){
        Glide.with(this).load(Current_Teacher.profileImg).into(profileImage);
        name.setText(Current_Teacher.fname+ Current_Teacher.lname);
        experience.setText(Current_Teacher.exp);
        rating.setText(Current_Teacher.rate+" / 5");
        phone.setText("0"+Current_Teacher.phone);
        Email.setText(Current_Teacher.email);
        address.setText(Current_Teacher.city);
        Glide.with(getActivity()).load(Current_Teacher.carImg).into(carImg);
        getParticipatingRequests();

        showUseCase(1);


    }

    private void showUseCase(int type) {

        View requiredView=null;
        String message="";
        if(type ==1){
            requiredView=schedual;
            message="to see today session , click here";
        }else if(type==2){
            requiredView=students;
            message="to see your students , click here";

        }else if(type==3){
            requiredView=requests;
            message="to check your notifications and messages , click here";

        }

        if(requiredView !=null) {
            new BubbleShowCaseBuilder(requireActivity())
                    .title(message)
                    .backgroundColor(ContextCompat.getColor(requireActivity(), R.color.usecase))
                    /*.showOnce("BUBBLE_SHOW_CASE_ID1")*/
                    .image(ContextCompat.getDrawable(requireActivity(), R.mipmap.logo_icon_app))
                    .targetView(requiredView)

                    .listener(new BubbleShowCaseListener() {
                        @Override
                        public void onTargetClick(@NotNull BubbleShowCase bubbleShowCase) {

                        }

                        @Override
                        public void onCloseActionImageClick(@NotNull BubbleShowCase bubbleShowCase) {

                            if (countCase != 3) {
                                countCase++;
                                showUseCase(countCase);
                            }
                        }

                        @Override
                        public void onBackgroundDimClick(@NotNull BubbleShowCase bubbleShowCase) {

                        }

                        @Override
                        public void onBubbleClick(@NotNull BubbleShowCase bubbleShowCase) {

                        }
                    }).show();
        }

    }

    public void getTeacherStudents(){

        pd.show();

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherStudents.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        String id = x.getString("contract_id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("date");
                        String student_name = x.getString("student_name");
                        String teacher_name= x.getString("teacher_name");
                        int n_lessons= x.getInt("n_lessons");
                        int n_left=x.getInt("n_lessonsLeft");
                        getStudentsInfo(student_email,teacher_email,date,teacher_name,n_lessons,n_left);





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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
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

    public void getStudentsInfo(String student_email, String teacher_email, String date, String teacher_name, int n_lessons,int n_left){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        String phone = x.getString("phone");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String lat = x.getString("lat");
                        String lng = x.getString("lng");
                        int rate=x.getInt("rate");
                        String profileImg = x.getString("profileImg");

                        TeacherStudents ts=new TeacherStudents();
                        ts.setStudentId(id);
                        ts.setFname(fname);
                        ts.setEmail(email);
                        ts.setLname(lname);
                        ts.setLat(lat);
                        ts.setLng(lng);
                        ts.setAge(age);
                        ts.setCity(city);
                        ts.setPhone(phone);
                        ts.setProfileImg(profileImg);
                        ts.setTeacherName(teacher_name);
                        ts.setDate(date);
                        ts.setLessons(String.valueOf(n_lessons));
                        ts.setLessons_left(n_left);
                        ts.setRate(rate);
                        studentsArray.add(ts);









                    }
                    prepareArray(studentsArray);



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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",student_email);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    private void prepareArray(ArrayList<TeacherStudents> studentsArray) {

            RecyclerViewTeacherStudents recyclerViewTeacherStudents=new RecyclerViewTeacherStudents(getActivity(),studentsArray);
            recStudents.setAdapter(recyclerViewTeacherStudents);



    }

    private void getTodaysLessons(){
        pd.show();

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTodaysLessons.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        String id = x.getString("id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status= x.getString("status");
                        String approved=x.getString("approved");
                        getStudentData(student_email,date,id,teacher_email,status,approved);




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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                String currentDate = new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(new Date());
                param.put("email", Current_Teacher.email);
                param.put("date",currentDate);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    public void getStudentData(String emailStudent,String date,String id,String teach_mail,String status,String approved){
        long session_id=Long.parseLong(id);

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        String email = x.getString("email");
                        String fname = x.getString("fname");
                        String lname = x.getString("lname");
                        String phone = x.getString("phone");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String lat = x.getString("lat");
                        String lng = x.getString("lng");
                        String rate=x.getString("rate");
                        String profileImg = x.getString("profileImg");

                        Lessons today_lessons=new Lessons();
                        today_lessons.setSession_id(session_id);
                        today_lessons.setFname(fname);
                        today_lessons.setLname(lname);
                        today_lessons.setPhone(phone);
                        today_lessons.setProfileImg(profileImg);
                        today_lessons.setStudent_email(emailStudent);
                        today_lessons.setLat(lat);
                        today_lessons.setLng(lng);
                        today_lessons.setCity(city);
                        today_lessons.setAge(age);
                        today_lessons.setDate(date);
                        today_lessons.setTeacher_email(teach_mail);
                        today_lessons.setRate(rate);
                        today_lessons.setStatus(status);
                        today_lessons.setApproved(approved);


                        Arraylessons.add(today_lessons);


                    }
                     RecyclerTodaysLessons recyclerTodaysLessons=new RecyclerTodaysLessons(getActivity(),Arraylessons);
                     recSchedule.setAdapter(recyclerTodaysLessons);



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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",emailStudent);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    public void getParticipatingRequests(){
        numberOfNotification=0;

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentsRequest.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

              //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                      numberOfNotification+=arr.length();
                      getAppointmentsRequests();




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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
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

    public void getAppointmentsRequests(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");
                    numberOfNotification+=arr.length();

                    getCancellationRequests();





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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email", Current_Teacher.email);
                param.put("status", "pending/appoint");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void getCancellationRequests() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //  Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");
                    numberOfNotification+=arr.length();

                    if(numberOfNotification>0){
                        notificationLayout.setVisibility(View.VISIBLE);
                        notifications.setText(numberOfNotification+"");
                    }
                   // Toast.makeText(getActivity(),numberOfNotification+"", Toast.LENGTH_SHORT).show();





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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email", Current_Teacher.email);
                param.put("status", "pending/drop");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);




    }


}
