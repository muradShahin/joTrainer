package com.murad.project1.activites.student_activites.ui.Profile_student;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerStudentLessons;
import com.murad.project1.RecyclersView.RecyclerStudentTodayLesson;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.lessonsClasses.Lessons;

import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentDetailsOfTeacher;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.PerformanceModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import devlight.io.library.ArcProgressStackView;

public class Student_profile extends Fragment {

      private Student_profileModel galleryViewModel;
      private ImageView profImg,teacherImage;
      private TextView StudentName,details,schedual,TeacherName,lessons,lessonsLeft;
      LinearLayout detailsLayout,ScheduleLay;
      ProgressBar progressBar;

      private ImageView Star1,Star2,Star3,Star4,Star5;
      ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
      ArrayList<Lessons> arrayOfLessonsComing=new ArrayList<>();
      ArrayList<Lessons> todayLessons=new ArrayList<>();



      int[] performance = new int[5];
      boolean flag=false;
      //schedule inside nav
      private TextView todays,upComing,archiveLess;
      private  RecyclerView recyclerViewTodays,recyclerViewComing,recyclerViewArchive;
      private Button moreButton;


    ArcProgressStackView arcProgressStackView;
      int controling,shifting,parking,monitoring,priorites;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(Student_profileModel.class);
        View root = inflater.inflate(R.layout.fragment_profile_student, container, false);
       profImg=root.findViewById(R.id.profileImg);
       StudentName=root.findViewById(R.id.name);
       details=root.findViewById(R.id.details);
       schedual=root.findViewById(R.id.schedual);
       detailsLayout=root.findViewById(R.id.detailsLay);
       ScheduleLay=root.findViewById(R.id.scheduleLay);
       progressBar=root.findViewById(R.id.progressBar);
       TeacherName=root.findViewById(R.id.teacherName);
       teacherImage=root.findViewById(R.id.teacherImg);
       moreButton=root.findViewById(R.id.moreBtn);
       Star1=root.findViewById(R.id.star1);
       Star2=root.findViewById(R.id.star2);
       Star3=root.findViewById(R.id.star3);
       Star4=root.findViewById(R.id.star4);
       Star5=root.findViewById(R.id.star5);
       lessons=root.findViewById(R.id.n_lessons);
       lessonsLeft=root.findViewById(R.id.n_lessonsLeft);
       arcProgressStackView = root.findViewById(R.id.apsv);
       todays=root.findViewById(R.id.todaysTextBtn);
       upComing=root.findViewById(R.id.comingTextBtn);
       archiveLess=root.findViewById(R.id.archiveTextBtn);
       recyclerViewTodays=root.findViewById(R.id.recToday);
       recyclerViewComing=root.findViewById(R.id.recUpComing);
       recyclerViewArchive=root.findViewById(R.id.recArchive);


       models.clear();







        //handling more button on click
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDetailsOfTeacher.ENROLLED_STUDENT=true;
                if(TextUtils.isEmpty(CurrentDetailsOfTeacher.email)){
                    Toast.makeText(getActivity(), "you have no Trainer", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_home);
                }else {
                    Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_moreTeachDetails);
                }
                }
        });






        //controlling the nav (My details,My schedule)
        ScheduleLay.setVisibility(View.GONE);

        //student behavior diagram
        setPerformanceDigram();
        //enabling my details at the first
        details.setBackgroundResource(R.drawable.btn_login_shape);
        details.setPadding(0,30,0,30);
        details.setTextColor(getResources().getColor(R.color.quantum_white_100));
        details.setTextSize(16);

        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight= 1;
        details.setLayoutParams(lp);






        //this piece of code is used to get the user to the section of upComing lessons
        //after he clicked on more button
        if(Flags.ReturnFromLessInfo){
            details.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
            details.setPadding(0,0,0,0);
            details.setTextColor(getResources().getColor(R.color.quantum_grey800));
            details.setTextSize(12);

            schedual.setBackgroundResource(R.drawable.btn_login_shape);
            schedual.setPadding(0,30,0,30);
            schedual.setTextColor(getResources().getColor(R.color.quantum_white_100));
            schedual.setTextSize(16);
            Toast.makeText(getActivity(), "schedule", Toast.LENGTH_SHORT).show();

            LinearLayout.LayoutParams lpp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight= 1;
            schedual.setLayoutParams(lp);

            LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            lp2.weight=2;
            details.setLayoutParams(lp2);
            detailsLayout.setVisibility(View.GONE);
            ScheduleLay.setVisibility(View.VISIBLE);




        }



        //handling my details on click
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // details.setBackgroundColor(getResources().getColor(R.color.myOrange));
                details.setBackgroundResource(R.drawable.btn_login_shape);
                details.setPadding(0,30,0,30);
                details.setTextColor(getResources().getColor(R.color.quantum_white_100));
                details.setTextSize(16);

                schedual.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                schedual.setPadding(0,0,0,0);
                schedual.setTextColor(getResources().getColor(R.color.quantum_grey800));
                schedual.setTextSize(12);

                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp.weight= 1;
                details.setLayoutParams(lp);

                LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp2.weight=2;
                schedual.setLayoutParams(lp2);
                detailsLayout.setVisibility(View.VISIBLE);
                ScheduleLay.setVisibility(View.GONE);

            }
        });

        //handling my schedule on click
        schedual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                details.setPadding(0,0,0,0);
                details.setTextColor(getResources().getColor(R.color.quantum_grey800));
                details.setTextSize(12);

                schedual.setBackgroundResource(R.drawable.btn_login_shape);
                schedual.setPadding(0,30,0,30);
                schedual.setTextColor(getResources().getColor(R.color.quantum_white_100));
                schedual.setTextSize(16);

                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp.weight= 1;
                schedual.setLayoutParams(lp);

                LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp2.weight=2;
                details.setLayoutParams(lp2);
                detailsLayout.setVisibility(View.GONE);
                ScheduleLay.setVisibility(View.VISIBLE);


            }
        });



        //settings on (My schedule layout)
        recyclerViewComing.setVisibility(View.GONE);
        recyclerViewArchive.setVisibility(View.GONE);


         //the first recycler that must be clicked and active when user first enter to the
        //schedule section
        recyclerViewTodays.setVisibility(View.VISIBLE);
        recyclerViewComing.setVisibility(View.GONE);
        recyclerViewArchive.setVisibility(View.GONE);
        todays.setTextColor(getResources().getColor(R.color.myOrange));
        upComing.setTextColor(getResources().getColor(R.color.quantum_grey700));
        archiveLess.setTextColor(getResources().getColor(R.color.quantum_grey700));
        arrayOfLessonsComing.clear();
        todayLessons.clear();
        prepareTodayLessons();


        if(Flags.ReturnFromLessInfo){
            recyclerViewTodays.setVisibility(View.GONE);
            recyclerViewComing.setVisibility(View.VISIBLE);
            recyclerViewArchive.setVisibility(View.GONE);
            arrayOfLessonsComing.clear();
            prepareRecycler();
            todays.setTextColor(getResources().getColor(R.color.quantum_grey700));
            upComing.setTextColor(getResources().getColor(R.color.myOrange));
            archiveLess.setTextColor(getResources().getColor(R.color.quantum_grey700));
            todayLessons.clear();

            Flags.ReturnFromLessInfo=false;
        }


        //handling the click on schedule inside navbar
        todays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewTodays.setVisibility(View.VISIBLE);
                recyclerViewComing.setVisibility(View.GONE);
                recyclerViewArchive.setVisibility(View.GONE);
                todays.setTextColor(getResources().getColor(R.color.myOrange));
                upComing.setTextColor(getResources().getColor(R.color.quantum_grey700));
                archiveLess.setTextColor(getResources().getColor(R.color.quantum_grey700));
                arrayOfLessonsComing.clear();
                todayLessons.clear();
                prepareTodayLessons();



            }
        });

        upComing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewTodays.setVisibility(View.GONE);
                recyclerViewComing.setVisibility(View.VISIBLE);
                recyclerViewArchive.setVisibility(View.GONE);
                arrayOfLessonsComing.clear();
                prepareRecycler();
                todays.setTextColor(getResources().getColor(R.color.quantum_grey700));
                upComing.setTextColor(getResources().getColor(R.color.myOrange));
                archiveLess.setTextColor(getResources().getColor(R.color.quantum_grey700));
                todayLessons.clear();

            }
        });

        archiveLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewTodays.setVisibility(View.GONE);
                recyclerViewComing.setVisibility(View.GONE);
                recyclerViewArchive.setVisibility(View.VISIBLE);
                todays.setTextColor(getResources().getColor(R.color.quantum_grey700));
                upComing.setTextColor(getResources().getColor(R.color.quantum_grey700));
                archiveLess.setTextColor(getResources().getColor(R.color.myOrange));
                arrayOfLessonsComing.clear();
                todayLessons.clear();
                getArchivedLessons();

            }
        });










       //init Data
        initData();
        getStudentTeacher();


        return root;
    }



    private void initData() {
        Glide.with(getActivity()).load(Currrent_Student.profileImg).into(profImg);
        StudentName.setText(Currrent_Student.fname+" "+ Currrent_Student.lname);
        getRate();
        getLessonsInfo();
    }

    private void getRate() {
        switch (Currrent_Student.rate){
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

    //this method for getting the student teacher
    private void getStudentTeacher(){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentTeacher.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

         //    Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id=x.getInt("id");
                        String email = x.getString("email");
                        String fname= x.getString("fname");
                        String lname = x.getString("lname");
                        String phone = x.getString("phone");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String exp = x.getString("exp");
                        String carType = x.getString("carType");
                        String carImg = x.getString("carImg");
                        String profileImg = x.getString("profileImg");
                        String rate = x.getString("rate");

                        CurrentDetailsOfTeacher.id=id;
                        CurrentDetailsOfTeacher.fname=fname;
                        CurrentDetailsOfTeacher.lname=lname;
                        CurrentDetailsOfTeacher.email=email;
                        CurrentDetailsOfTeacher.phone=phone;
                        CurrentDetailsOfTeacher.city=city;
                        CurrentDetailsOfTeacher.exp=exp;
                        CurrentDetailsOfTeacher.age=age;
                        CurrentDetailsOfTeacher.carType=carType;
                        CurrentDetailsOfTeacher.carImg=carImg;
                        CurrentDetailsOfTeacher.rate=rate;
                        CurrentDetailsOfTeacher.profileImg=profileImg;
                        /*Current_Teacher.lname = lname;
                        Current_Teacher.phone = phone;
                        Current_Teacher.age = age;
                        Current_Teacher.profileImg = profileImg;
                        Current_Teacher.carImg = carImg;
                        Current_Teacher.carType = carType;
                        Current_Teacher.exp = exp;
                        Current_Teacher.office=office;
                        Current_Teacher.city = city;
                        Current_Teacher.rate=rate;*/

                        TeacherName.setText(fname+" "+lname);
                        Glide.with(getActivity()).load(profileImg).into(teacherImage);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
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
    private void getLessonsInfo(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessonsInfo.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);

                        String n_lessons= x.getString("n_lessons");
                        String n_lessonsLeft= x.getString("n_lessonsLeft");
                        lessons.setText(n_lessons);
                        lessonsLeft.setText(n_lessonsLeft);
                    }


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


    public void setPerformanceDigram(){

        ArcProgressStackView.Model model1=new ArcProgressStackView.Model("Controlling",PerformanceModels.controling, Color.parseColor("#e0e0e0"), Color.parseColor("#2e7d32"));
        models.add(model1);
        ArcProgressStackView.Model model2=new ArcProgressStackView.Model("Monitoring",PerformanceModels.monitoring, Color.parseColor("#e0e0e0"), Color.parseColor("#fdd835"));
        models.add(model2);
        ArcProgressStackView.Model model3=new ArcProgressStackView.Model("Shifting",PerformanceModels.shifting, Color.parseColor("#e0e0e0"), Color.parseColor("#0d47a1"));
        models.add(model3);
        ArcProgressStackView.Model model4=new ArcProgressStackView.Model("Parking",PerformanceModels.parking, Color.parseColor("#e0e0e0"), Color.parseColor("#8e0000"));
        models.add(model4);
        ArcProgressStackView.Model model5=new ArcProgressStackView.Model("Priorities",PerformanceModels.priorites, Color.parseColor("#e0e0e0"), Color.parseColor("#b53d00"));
        models.add(model5);
        arcProgressStackView.setModels(models);

    }


    private void prepareRecycler() {
        progressBar.setVisibility(View.VISIBLE);
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

                        int id = x.getInt("id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status= x.getString("status");



                        Lessons td=new Lessons();
                        td.setStatus(status);
                        td.setTodayLesson(false);
                        td.setDate(date);
                        td.setSession_id(id);
                        td.setStudent_email(student_email);
                        td.setTeacher_email(teacher_email);
                        arrayOfLessonsComing.add(td);


                    }
                    recyclerViewComing.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));
                    RecyclerStudentLessons recyclerStudentLessons=new RecyclerStudentLessons(getActivity(),arrayOfLessonsComing);
                    recyclerViewComing .setAdapter(recyclerStudentLessons);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);

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
                param.put("email", Currrent_Student.email);
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

    private void prepareTodayLessons(){
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentTodayLess.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //    Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();





                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);

                        int id = x.getInt("id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status= x.getString("status");

                        Lessons lesson=new Lessons();
                        lesson.setDate(date);
                        lesson.setSession_id(id);
                        lesson.setStatus(status);
                        lesson.setTodayLesson(true);
                        lesson.setStudent_email(student_email);
                        lesson.setTeacher_email(teacher_email);
                        todayLessons.add(lesson);



                    }
                    recyclerViewTodays.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    RecyclerStudentTodayLesson recyclerStudentTodayLesson=new RecyclerStudentTodayLesson(getActivity(),todayLessons);
                    recyclerViewTodays.setAdapter(recyclerStudentTodayLesson);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
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
                param.put("email", Currrent_Student.email);
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
    private void getArchivedLessons(){
        progressBar.setVisibility(View.VISIBLE);
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
                        int id = x.getInt("id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date= x.getString("session_date");
                        String status=x.getString("status");

                        lesson.setStatus(status);
                        lesson.setDate(date);
                        lesson.setSession_id(id);
                        lesson.setStudent_email(student_email);
                        lesson.setTeacher_email(teacher_email);
                        lesson.setTodayLesson(false);

                        arrayOfLessonsComing.add(lesson);



                    }
                    recyclerViewArchive.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true));
                    RecyclerStudentLessons recyclerStudentLessons=new RecyclerStudentLessons(getActivity(),arrayOfLessonsComing);
                    recyclerViewArchive .setAdapter(recyclerStudentLessons);





                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);

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
                param.put("email", Currrent_Student.email);
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




    }




