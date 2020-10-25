package com.murad.project1.activites.student_activites.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerAllTeachers;
import com.murad.project1.RecyclersView.RecyclerStudentMessages;
import com.murad.project1.UsersClasses.Teacher;

import com.murad.project1.activites.student_activites.ui.Tracking;
import com.murad.project1.contractClasses.Messages;
import com.murad.project1.lessonsClasses.Lessons;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.FilterClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;


public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    FloatingSearchView mSearchView;
    private String searchKey;
    String fname;
    String lname;
    private ImageView notification;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<Teacher>teachersArrayList=new ArrayList<>();
    private ArrayList<Teacher>teachersArrayListByOrder=new ArrayList<>();
    private RelativeLayout notificationLayout;
    private TextView n_notifications;

    // current lesson layout
    private ImageView driverImage,trackingBtn;
    private TextView driverName,date;
    private ConstraintLayout currentLesson;
    private LatLng teacherLocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mSearchView=root.findViewById(R.id.floating_search_view);
        recyclerView=root.findViewById(R.id.rec);
        progressBar=root.findViewById(R.id.progressBarSearch);
        notification=root.findViewById(R.id.notiBtn);
        teachersArrayList.clear();
        teachersArrayListByOrder.clear();

        notificationLayout=root.findViewById(R.id.notfiLayout);
        n_notifications=root.findViewById(R.id.notif);

        notificationLayout.setVisibility(View.GONE);

        //current lesson init
        trackingBtn=root.findViewById(R.id.trackingImage);
        driverName=root.findViewById(R.id.driverName);
        date=root.findViewById(R.id.sessionDate);
        driverImage=root.findViewById(R.id.trainerImage);
        currentLesson=root.findViewById(R.id.constraintLayout);
        currentLesson.setVisibility(View.GONE);

        getAllStudentMessages();
        getAllTeachers();
        getLiveLesson();

        //handling on search bar menu click
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if(item.getItemId()==R.id.filter){
                    showFilter();


                }

            }


        });
       //end of code


        // on current lesson click
        currentLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getActivity(), Tracking.class);
                i.putExtra("teacherLocation",teacherLocation);
                startActivity(i);
            }
        });



        //this code is for taking the final keyword from search box
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                fname="";
                lname="";
                //get suggestions based on newQuery
                searchKey=newQuery;

                for (int i=0;i<searchKey.length()-1;i++){
                    if (searchKey.charAt(i)==' ') {
                        fname = "";
                        fname = searchKey.substring(0, i);
                        lname = searchKey.substring(i+1);
                    }
                }
             if(fname.equals("")){
                fname=searchKey;
            }
             teachersArrayList.clear();
              getTeacherByName();

                //pass them on to the search view
              //  mSearchView.swapSuggestions();
            }
        });

        mSearchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {

                    @Override
                    public void onHomeClicked() {
                        teachersArrayList.clear();
                        teachersArrayListByOrder.clear();
                        getAllTeachers();
                    }
                });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_studentMessages);
            }
        });



        return root;
    }
    //this method return an array list of the filters which have been add to filter dialog
    private ArrayList prepareSearchArray(){
        ArrayList<FilterClass> arrayList=new ArrayList<>();
        arrayList.add(new FilterClass("highest rate"));
        arrayList.add(new FilterClass("lowest rate"));



        return arrayList;
    }


     //this method is responsible on making the filter dialog active
    private void showFilter(){
        new SimpleSearchDialogCompat(getActivity(), "Search...",
                "What are you looking for...?", null, prepareSearchArray(),
                new SearchResultListener<FilterClass>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog,
                                           FilterClass item, int position) {

                      if(item.getTitle().equals("highest rate")) {
                          searchKey="desc";

                      }
                      else if(item.getTitle().equals("lowest rate")) {
                          searchKey = "asc";

                      }
                      teachersArrayListByOrder.clear();
                        teachersArrayList.clear();
                        getTeachersByRate();

                        dialog.dismiss();

                    }

                }).show();



    }
    private void getAllTeachers(){
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getAllUsers.php";





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
                        Teacher teacher =new Teacher();
                        teacher.setId(id);
                        teacher.setEmail(email);
                        teacher.setFname(fname);
                        teacher.setLname(lname);
                        teacher.setAge(age);
                        teacher.setCarImg(carImg);
                        teacher.setPhone(phone);
                        teacher.setCity(city);
                        teacher.setExp(exp);
                        teacher.setProfileImg(profileImg);
                        teacher.setCarType(carType);
                        teacher.setRate(rate);
                        teachersArrayList.add(teacher);




                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    RecyclerAllTeachers recyclerAllTeachers=new RecyclerAllTeachers(getActivity(),teachersArrayList);
                    recyclerView.setAdapter(recyclerAllTeachers);




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
                param.put("role", "teacher");



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);





    }
    private void getTeachersByRate(){
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getAllUserByOrder.php";





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
                        Teacher teacher =new Teacher();
                        teacher.setId(id);
                        teacher.setEmail(email);
                        teacher.setFname(fname);
                        teacher.setLname(lname);
                        teacher.setAge(age);
                        teacher.setCarImg(carImg);
                        teacher.setPhone(phone);
                        teacher.setCity(city);
                        teacher.setExp(exp);
                        teacher.setProfileImg(profileImg);
                        teacher.setCarType(carType);
                        teacher.setRate(rate);
                        teachersArrayListByOrder.add(teacher);




                    }
                    RecyclerAllTeachers recyclerAllTeachers=new RecyclerAllTeachers(getActivity(),teachersArrayListByOrder);
                    recyclerView.setAdapter(recyclerAllTeachers);




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
                param.put("role", "teacher");
                param.put("orderState",searchKey);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);



    }
    private void getTeacherByName(){

        progressBar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getAllTeacherByName.php";





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
                        Teacher teacher =new Teacher();
                        teacher.setId(id);
                        teacher.setEmail(email);
                        teacher.setFname(fname);
                        teacher.setLname(lname);
                        teacher.setAge(age);
                        teacher.setCarImg(carImg);
                        teacher.setPhone(phone);
                        teacher.setCity(city);
                        teacher.setExp(exp);
                        teacher.setProfileImg(profileImg);
                        teacher.setCarType(carType);
                        teacher.setRate(rate);
                        teachersArrayList.add(teacher);




                    }

                    RecyclerAllTeachers recyclerAllTeachers=new RecyclerAllTeachers(getActivity(),teachersArrayList);
                    recyclerView.setAdapter(recyclerAllTeachers);




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
                param.put("role", "teacher");
                param.put("fname",fname);
                param.put("lname",lname);




                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void getAllStudentMessages(){



        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentsMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                      if(arr.length()>0){
                          notificationLayout.setVisibility(View.VISIBLE);
                          n_notifications.setText(arr.length()+"");

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

    private void getLiveLesson(){



        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentCurrentLesson.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");


                    Log.i("test1",arr.toString());

                        JSONObject jsonObject=arr.getJSONObject(0);
                        Lessons lesson=new Lessons();
                        lesson.setDate(jsonObject.getString("session_date"));
                        lesson.setStudent_email(jsonObject.getString("student_email"));
                        lesson.setTeacher_email(jsonObject.getString("teacher_email"));
                        lesson.setStatus(jsonObject.getString("status"));

                        getDriverAllInfo(lesson);







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

    private void getDriverAllInfo(Lessons lesson) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getUserData.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    Log.i("test2",arr.toString());




                    JSONObject jsonObject=arr.getJSONObject(0);
                    Teacher teacher=new Teacher();
                    teacher.setCarImg(jsonObject.getString("carImg"));
                    teacher.setCarType(jsonObject.getString("carType"));
                    teacher.setFname(jsonObject.getString("fname"));
                    teacher.setLname(jsonObject.getString("lname"));
                    teacher.setPhone(jsonObject.getString("phone"));
                    teacher.setAge(jsonObject.getString("age"));
                    teacher.setExp(jsonObject.getString("exp"));
                    teacher.setProfileImg(jsonObject.getString("profileImg"));
                    teacher.setRate(jsonObject.getString("rate"));
                    teacher.setEmail(jsonObject.getString("email"));
                    teacher.setLat(jsonObject.getString("lat"));
                    teacher.setLng(jsonObject.getString("lng"));

                    Current_Teacher.phone=teacher.getPhone();
                    Current_Teacher.fname=teacher.getFname();
                    Current_Teacher.lname=teacher.getLname();
                    Current_Teacher.email=teacher.getEmail();
                    Current_Teacher.exp=teacher.getExp();
                    Current_Teacher.age=teacher.getAge();
                    Current_Teacher.profileImg=teacher.getProfileImg();
                    Current_Teacher.rate=teacher.getRate();
                    Current_Teacher.carImg=teacher.getCarImg();
                    Current_Teacher.carType=teacher.getCarType();



                    setCurrentLessonLayot(lesson,teacher);






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
                param.put("email",lesson.getTeacher_email());



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    private void setCurrentLessonLayot(Lessons lesson, Teacher teacher) {

        double lat=Double.parseDouble(teacher.getLat());
        double lng=Double.parseDouble(teacher.getLng());

        teacherLocation=new LatLng(lat,lng);


        currentLesson.setVisibility(View.VISIBLE);
        driverName.setText(teacher.getFname() +" "+teacher.getLname());
        date.setText(lesson.getDate());
        Glide.with(this)
                .load(teacher.getProfileImg())
                .into(driverImage);


    }


}