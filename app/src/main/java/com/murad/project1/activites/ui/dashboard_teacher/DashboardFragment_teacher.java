package com.murad.project1.activites.ui.dashboard_teacher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.murad.project1.R;
import com.murad.project1.RecyclersView.DatesAdapter;
import com.murad.project1.RecyclersView.RecyclerStudentLessons;
import com.murad.project1.RecyclersView.RecyclerTeacherLessons;
import com.murad.project1.RecyclersView.RecyclerViewTeacherStudents;
import com.murad.project1.RecyclersView.StudentsFilterAdapter;
import com.murad.project1.UsersClasses.TeacherStudents;
import com.murad.project1.activites.studentApplicationActivity;
import com.murad.project1.lessonsClasses.Lessons;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentLocationNew;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.LocationService;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import guy4444.smartrate.SmartRate;
import io.paperdb.Paper;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;


public class DashboardFragment_teacher extends Fragment {

    private DashboardViewModel_teacher dashboardViewModelTeacher;
    private TextView currentLesson,scheduleSection;
    private boolean UPCOMING_ARCHIVE;
    private ImageView studentImg;
    private TextView studentName;
    private EditText controlling,shifting,priorities,monitoring,parking,notes;
    private ArrayList<Lessons> lessonInformation=new ArrayList<>();
    private ProgressBar pr;
    private long lesson_ID;
    private Button submit;
    private String studentEmail;
    private int previousRates=0;
    private int numberOfLessons;
    private int numberOfLessonsLeft;
    private LinearLayout schedule,currentLessonLayout;
    private TextView upComing,archive;
    private RecyclerView recyclerView;
    private TextView searchByDate;
    private  SweetAlertDialog pd;
    private Dialog dialog;
    private TextView noSessionText;

    private DashboardFragment_teacher instance;

    ArrayList<Lessons> lessonsArrayList=new ArrayList<>();
    ArrayList<TeacherStudents> studentsArray=new ArrayList<>();


    //firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    //filter dialog
     private ImageView filterBtn;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModelTeacher =
                ViewModelProviders.of(this).get(DashboardViewModel_teacher.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        instance=this;

        currentLesson=root.findViewById(R.id.currLess);
        scheduleSection=root.findViewById(R.id.scheduleSection);
        studentImg=root.findViewById(R.id.studentImage);
        studentName=root.findViewById(R.id.name);
        noSessionText=root.findViewById(R.id.noSessionText);
        controlling=root.findViewById(R.id.controlingPerc);
        shifting=root.findViewById(R.id.shifting);
        priorities=root.findViewById(R.id.priorities);
        monitoring=root.findViewById(R.id.monitoring);
        parking=root.findViewById(R.id.parking);
        notes=root.findViewById(R.id.note);
        pr=root.findViewById(R.id.progressBar);
        submit=root.findViewById(R.id.submitButton);
        currentLessonLayout=root.findViewById(R.id.currentLessLay);
        schedule=root.findViewById(R.id.schedLayOut);
        upComing=root.findViewById(R.id.futureLess);
        archive=root.findViewById(R.id.history);
        searchByDate=root.findViewById(R.id.searchTxt);
        filterBtn=root.findViewById(R.id.filterBtn);
        recyclerView=root.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));

        //firebase init
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Locations").child(Current_Teacher.lname+Current_Teacher.fname+Current_Teacher.phone);

        lessonsArrayList.clear();

        // starting a service to keep the location updated even if the driver closes the app

       /* Intent intent=new Intent(requireActivity(), LocationService.class);
        requireActivity().startService(intent);*/
       getUpdatedDriverLocation();






        //init DATA
       init();

        //making progress dialog invisible
        pr.setVisibility(View.GONE);


        //handling Submit Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                 updatePerformance();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("By submitting , the session will be finished !").setPositiveButton("Ok", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();


            }
        });

        if(Flags.ReturnFromLessInfo){

            currentLesson.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
            currentLesson.setTextColor(getResources().getColor(R.color.quantum_grey800));


            scheduleSection.setBackgroundResource(R.drawable.btn_login_shape);
            scheduleSection.setTextColor(getResources().getColor(R.color.quantum_white_100));


            currentLessonLayout.setVisibility(View.GONE);
            noSessionText.setVisibility(View.VISIBLE);


            archive.setTextColor(getResources().getColor(R.color.quantum_white_100));
            archive.setBackgroundResource(R.drawable.btn_login_shape);
            UPCOMING_ARCHIVE=true;

            getArchiveLessons();





            Flags.ReturnFromLessInfo=false;




        }else {


            scheduleSection.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
            scheduleSection.setTextColor(getResources().getColor(R.color.quantum_grey800));


            currentLesson.setBackgroundResource(R.drawable.btn_login_shape);
            currentLesson.setTextColor(getResources().getColor(R.color.quantum_white_100));


            schedule.setVisibility(View.GONE);
            noSessionText.setVisibility(View.VISIBLE);


            upComing.setTextColor(getResources().getColor(R.color.quantum_white_100));
            upComing.setBackgroundResource(R.drawable.btn_login_shape);
            getUpcomingLessons();
            UPCOMING_ARCHIVE=false;


        }

        currentLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleSection.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                scheduleSection.setTextColor(getResources().getColor(R.color.quantum_grey800));


                currentLesson.setBackgroundResource(R.drawable.btn_login_shape);
                currentLesson.setTextColor(getResources().getColor(R.color.quantum_white_100));


                currentLessonLayout.setVisibility(View.VISIBLE);
                noSessionText.setVisibility(View.VISIBLE);
                schedule.setVisibility(View.GONE);

                init();

            }
        });

        //settings on the inside of schedule section



        upComing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByDate.setText("Search by date");
                upComing.setTextColor(getResources().getColor(R.color.quantum_white_100));
                upComing.setBackgroundResource(R.drawable.btn_login_shape);

                archive.setTextColor(getResources().getColor(R.color.quantum_grey900));
                archive.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                lessonsArrayList.clear();
                getUpcomingLessons();
                UPCOMING_ARCHIVE=false;


            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByDate.setText("Search by date");
                archive.setTextColor(getResources().getColor(R.color.quantum_white_100));
                archive.setBackgroundResource(R.drawable.btn_login_shape);

                upComing.setTextColor(getResources().getColor(R.color.quantum_grey900));
                upComing.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                lessonsArrayList.clear();
                getArchiveLessons();
                UPCOMING_ARCHIVE=true;

            }
        });



        scheduleSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLesson.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                currentLesson.setTextColor(getResources().getColor(R.color.quantum_grey800));

                scheduleSection.setBackgroundResource(R.drawable.btn_login_shape);
                scheduleSection.setTextColor(getResources().getColor(R.color.quantum_white_100));

                currentLessonLayout.setVisibility(View.GONE);
                noSessionText.setVisibility(View.GONE);
                schedule.setVisibility(View.VISIBLE);



            }
        });


        searchByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //filter dialog

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialog=new Dialog(requireContext());
                dialog.setContentView(R.layout.filter_dialog);

                TabLayout tabs=dialog.findViewById(R.id.tabLayout);
                RecyclerView filterRec=dialog.findViewById(R.id.rec_filter);
                filterRec.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

                getDates(filterRec);
                tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        if(tab.getPosition()==0){
                            getDates(filterRec);
                        }else if(tab.getPosition()==1){
                            getTeacherStudents(filterRec);

                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                dialog.show();


            }
        });


        return root;

    }



    private void showDate() {
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
                searchByDate.setText(  new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime()));
                String sessionDate=new SimpleDateFormat(getString(R.string.dateFormat),Locale.getDefault()).format(firstDate.getTime());
                Toast.makeText(getActivity(),sessionDate , Toast.LENGTH_SHORT).show();
               if(UPCOMING_ARCHIVE) {
                   lessonsArrayList.clear();
                   getArchiveLessonsOnDate(sessionDate);
               }else{
                   lessonsArrayList.clear();
                   getUpComingLessonsByDate(sessionDate);
               }
            }
        }).setBackgroundColor(Color.parseColor("#ffffff")).setHeaderColor(Color.parseColor("#f47b00"))
                .setSelectedTextColor(Color.parseColor("#f47b00"))
                .setSelectedColor(Color.parseColor("#ffffff")).show(getActivity().getSupportFragmentManager(),"date");



    }




    private void init() {
            Paper.init(Objects.requireNonNull(getActivity()));
            lessonInformation=Paper.book().read("currentLesson",new ArrayList<>());
            if(lessonInformation.size()==1) {
                Glide.with(getActivity()).load(lessonInformation.get(0).getProfileImg()).into(studentImg);
                studentName.setText(lessonInformation.get(0).getFname()+" "+lessonInformation.get(0).getLname());
                lesson_ID = lessonInformation.get(0).getSession_id();
                studentEmail = lessonInformation.get(0).getStudent_email();
                previousRates+=Integer.parseInt(lessonInformation.get(0).getRate());
                getStudentLessonInfo(studentEmail);

            }else {
                currentLessonLayout.setVisibility(View.GONE);
                noSessionText.setVisibility(View.VISIBLE);

            }
    }
    private void updatePerformance(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "updatePerformance.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        shutLessonDown();
                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();}
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
                param.put("id",String.valueOf(lesson_ID));
                param.put("email",studentEmail);
                param.put("controling",controlling.getText().toString());
                param.put("shifting",shifting.getText().toString());
                param.put("parking",parking.getText().toString());
                param.put("monitoring",monitoring.getText().toString());
                param.put("priorities",priorities.getText().toString());
                param.put("notes",notes.getText().toString());


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void shutLessonDown(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "updateLessonStatus.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        ArrayList<Lessons> newArray=new ArrayList<>();
                        Paper.init(getActivity());
                        Paper.book().write("currentLesson",newArray);
                        SmartRate.Rate(getActivity()
                                , "Rate "+studentName.getText().toString()
                                , "this rate is about the behavior of the student"
                                , "Continue"
                                , ""
                                , ""
                                , ""
                                , ""
                                , Color.parseColor("#f47b00")
                                , -1
                                , new SmartRate.CallBack_UserRating() {
                                    @Override
                                    public void userRating(int rating) {
                                        // Do something
                                        // maybe from now disable this button
                                        calculateAvgOfRate(rating);
                                    }
                                }
                        );





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
                param.put("lessId",String.valueOf(lesson_ID));
                param.put("newStatus","finished");


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }
    private void updateStudentRate(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "setRate.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        updateStudentLessonsNumber();


                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();}
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
                param.put("email",studentEmail);
                param.put("rate", String.valueOf(previousRates));


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    private void calculateAvgOfRate(int rate){
        int rateAvg=0;

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessons.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");
                     int numberOfRows=arr.length();
                     previousRates+=rate;
                     previousRates/=numberOfRows;
                     updateStudentRate();



                } catch (JSONException e) {
                    e.printStackTrace();
                }



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
                param.put("email",studentEmail);
                param.put("newStatus","finished");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }
    private void updateStudentLessonsNumber(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "updateLessonsNumber.php";


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
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_profile);
                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();}
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
                param.put("email",studentEmail);
                param.put("newLessNumber", String.valueOf(numberOfLessons));
                param.put("lessNleft",String.valueOf(numberOfLessonsLeft-1));

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }
    private void getStudentLessonInfo(String studentEmail){
        ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading..");
        pd.show();


        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentLessonNumber.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

              //   Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        numberOfLessons=x.getInt("n_lessons");
                        numberOfLessonsLeft=x.getInt("n_lessonsLeft");

                    }
                    pd.dismiss();



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
                param.put("email", studentEmail);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }
    private void getUpcomingLessons(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherLessons.php";





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
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status=x.getString("status");
                        String approve=x.getString("approved");


                        Log.i("testApprove",approve);
                        Lessons lesson=new Lessons();
                        lesson.setStudent_email(student_email);
                        lesson.setSession_id(id);
                        lesson.setTeacher_email(teacher_email);
                        lesson.setDate(date);
                        lesson.setStatus(status);
                        lesson.setApproved(approve);

                        lessonsArrayList.add(lesson);


                    }
                    RecyclerTeacherLessons recyclerTeacherLessons=new RecyclerTeacherLessons(getActivity(),lessonsArrayList);
                    recyclerView.setAdapter(recyclerTeacherLessons);






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
                param.put("email", Current_Teacher.email);
                param.put("newStatus","waiting");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void getArchiveLessons() {

        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherLessons.php";





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
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status=x.getString("status");

                        Lessons lesson=new Lessons();
                        lesson.setStudent_email(student_email);
                        lesson.setSession_id(id);
                        lesson.setTeacher_email(teacher_email);
                        lesson.setDate(date);
                        lesson.setStatus(status);
                        lessonsArrayList.add(lesson);


                    }
                    RecyclerTeacherLessons recyclerTeacherLessons=new RecyclerTeacherLessons(getActivity(),lessonsArrayList);
                    recyclerView.setAdapter(recyclerTeacherLessons);






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
                param.put("email", Current_Teacher.email);
                param.put("newStatus","finished");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    private void getArchiveLessonsOnDate(String sessionDate) {

        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessonsByDate.php";





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
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status=x.getString("status");

                        Lessons lesson=new Lessons();
                        lesson.setStudent_email(student_email);
                        lesson.setSession_id(id);
                        lesson.setTeacher_email(teacher_email);
                        lesson.setDate(date);
                        lesson.setStatus(status);
                        lessonsArrayList.add(lesson);


                    }
                    RecyclerTeacherLessons recyclerTeacherLessons=new RecyclerTeacherLessons(getActivity(),lessonsArrayList);
                    recyclerView.setAdapter(recyclerTeacherLessons);






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
                param.put("email", Current_Teacher.email);
                param.put("newStatus","finished");
                param.put("time",sessionDate);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }

    public void getUpComingLessonsByDate(String sessionDate){

        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessonsByDate.php";





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
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status=x.getString("status");

                        Lessons lesson=new Lessons();
                        lesson.setStudent_email(student_email);
                        lesson.setSession_id(id);
                        lesson.setTeacher_email(teacher_email);
                        lesson.setDate(date);
                        lesson.setStatus(status);
                        lessonsArrayList.add(lesson);


                    }
                    RecyclerTeacherLessons recyclerTeacherLessons=new RecyclerTeacherLessons(getActivity(),lessonsArrayList);
                    recyclerView.setAdapter(recyclerTeacherLessons);






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
                param.put("email", Current_Teacher.email);
                param.put("newStatus","waiting");
                param.put("time",sessionDate);



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
    public void onResume() {
        super.onResume();
       /* Intent intent=new Intent(requireActivity(), LocationService.class);
        requireActivity().startService(intent);*/
        //Toast.makeText(requireActivity(),"test",Toast.LENGTH_LONG).show();
    }

    public void getUpdatedDriverLocation(){
        CurrentLocationNew current=new CurrentLocationNew.Builder(requireActivity())
                .setIntervalv2(5000)
                .setFastestInterval(20000)
                .setListener(new CurrentLocationNew.EasyLocationCallback() {
                    @Override
                    public void onGoogleAPIClient(@Nullable GoogleApiClient googleApiClient, @Nullable String message) {

                    }

                    @Override
                    public void onLocationUpdated(double latitude, double longitude) throws IOException {
                        Log.i("update", "onLocationUpdated::" + latitude + " : " + longitude);

                        Map<String, Object> hasMap = new HashMap<>();

                        hasMap.put("lat", latitude + "");
                        hasMap.put("lng", longitude + "");

                        // update on firebase


                                databaseReference.updateChildren(hasMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("testUpdate", "success");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("testUpdate", "faild");

                                    }
                                });


                    }

                    @Override
                    public void onLocationUpdateRemoved() {

                    }
                }).build();

        getLifecycle().addObserver(current);

    }

    private void getDates(RecyclerView recDates) {

        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getDates.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();



                ArrayList<String> dates=new ArrayList<>();

                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");


                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);

                        String date= x.getString("session_date");

                        dates.add(date);

                    }

                    for (int i=0;i<dates.size();i++) {
                        if (dates.get(i).contains("dumy")){
                            dates.remove(i);
                        }
                    }
                    DatesAdapter adapter=new DatesAdapter(instance,dates);
                    recDates.setAdapter(adapter);






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



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }

    public void getLessonsOnFilterDate(String Date){
        if(UPCOMING_ARCHIVE) {
            lessonsArrayList.clear();
            getArchiveLessonsOnDate(Date);
        }else{
            lessonsArrayList.clear();
            getUpComingLessonsByDate(Date);
        }
        dialog.dismiss();

    }



    public void getTeacherStudents(RecyclerView recyclerView){

        studentsArray.clear();
        pd= new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("logging out...");
        pd.setCancelable(false);
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
                        getStudentsInfo(student_email,teacher_email,date,teacher_name,n_lessons,n_left,recyclerView);





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

    public void getStudentsInfo(String student_email, String teacher_email, String date, String teacher_name, int n_lessons,int n_left,RecyclerView recyclerView){
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
                    prepareArray(studentsArray,recyclerView);



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

    private void prepareArray(ArrayList<TeacherStudents> studentsArray,RecyclerView recFilter) {

        StudentsFilterAdapter recyclerViewTeacherStudents=new StudentsFilterAdapter(instance,studentsArray);
        recFilter.setAdapter(recyclerViewTeacherStudents);



    }

    public void getLessonsFilterByName(String studentEmail){
        if(UPCOMING_ARCHIVE) {
            lessonsArrayList.clear();
            getStudentLessons(studentEmail,"finished");
        }else{
            lessonsArrayList.clear();
            getStudentLessons(studentEmail,"waiting");
        }
    }

    private void getStudentLessons(String studentEmail,String status){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessons.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");


                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);

                        Lessons lesson=new Lessons();
                        lesson.setSession_id(x.getInt("id"));
                        lesson.setTeacher_email(x.getString("teacher_email"));
                        lesson.setStudent_email(x.getString("student_email"));
                        lesson.setDate(x.getString("session_date"));
                        lesson.setStatus(x.getString("status"));
                        lesson.setApproved(x.getString("approved"));

                        lessonsArrayList.add(lesson);



                    }
                    RecyclerTeacherLessons recyclerTeacherLessons=new RecyclerTeacherLessons(getActivity(),lessonsArrayList);
                    recyclerView.setAdapter(recyclerTeacherLessons);
                    dialog.dismiss();


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
                param.put("email",studentEmail);
                param.put("newStatus",status);



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