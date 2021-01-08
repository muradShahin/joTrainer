package com.murad.project1.activites.ui.preDefinedFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerAppointmentsMessage;
import com.murad.project1.RecyclersView.RecyclerCancellationRequests;
import com.murad.project1.RecyclersView.RecyclerViewAllRequests;
import com.murad.project1.activites.Index;
import com.murad.project1.activites.StudentDetails;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.contractClasses.Messages;
import com.murad.project1.contractClasses.Requests;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Flags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class allRequests extends Fragment {

    private ArrayList<Requests> requests=new ArrayList<>();
    private ArrayList<Messages>messagesArray=new ArrayList<>();
    RecyclerView recyclerView,recyclerViewAppoint,recyclerViewCancling;
    TextView studentRequests,messages,cancel;
    SweetAlertDialog pd;
    public allRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_all_requests, container, false);
        studentRequests=v.findViewById(R.id.currLess);
        messages=v.findViewById(R.id.messagesSection);
        recyclerViewAppoint=v.findViewById(R.id.recAppointments);
        cancel=v.findViewById(R.id.canclingSection);
        recyclerView=v.findViewById(R.id.recToday);
        recyclerViewCancling=v.findViewById(R.id.recCanceling);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewAppoint.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewCancling.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewAppoint.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewCancling.setVisibility(View.GONE);

         pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);


        requests.clear();
        getParticipatingRequests();







         //this is code is made to manage on back press
        //to finish the current fragment
        //so the user can't get back to it until he/she press on the notification
        //button once again only
         OnBackPressedCallback callback=new OnBackPressedCallback(true) {
             @Override
             public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStack();


             }
         };
         requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);


//handling my custom bar
        messages.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
        messages.setTextColor(getResources().getColor(R.color.quantum_grey800));
        messages.setTextSize(10);

        cancel.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
        cancel.setTextColor(getResources().getColor(R.color.quantum_grey800));
        cancel.setTextSize(10);

        studentRequests.setBackgroundResource(R.drawable.btn_login_shape);
        studentRequests.setPadding(0,30,0,30);
        studentRequests.setTextColor(getResources().getColor(R.color.quantum_white_100));
        studentRequests.setTextSize(12);


        studentRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                messages.setTextColor(getResources().getColor(R.color.quantum_grey800));
                messages.setTextSize(10);

                cancel.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                cancel.setTextColor(getResources().getColor(R.color.quantum_grey800));
                cancel.setTextSize(10);


                studentRequests.setBackgroundResource(R.drawable.btn_login_shape);
                studentRequests.setTextColor(getResources().getColor(R.color.quantum_white_100));
                studentRequests.setTextSize(12);


                requests.clear();
                getParticipatingRequests();
                recyclerViewAppoint.setVisibility(View.GONE);
                recyclerViewCancling.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);





            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentRequests.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                studentRequests.setTextColor(getResources().getColor(R.color.quantum_grey800));
                studentRequests.setTextSize(10);

                cancel.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                cancel.setTextColor(getResources().getColor(R.color.quantum_grey800));
                cancel.setTextSize(10);

                messages.setBackgroundResource(R.drawable.btn_login_shape);
                messages.setTextColor(getResources().getColor(R.color.quantum_white_100));
                messages.setTextSize(12);


                messagesArray.clear();
                getAppointmentsRequests();
                recyclerView.setVisibility(View.GONE);
                recyclerViewCancling.setVisibility(View.GONE);

                recyclerViewAppoint.setVisibility(View.VISIBLE);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentRequests.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                studentRequests.setTextColor(getResources().getColor(R.color.quantum_grey800));
                studentRequests.setTextSize(10);

                messages.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
                messages.setTextColor(getResources().getColor(R.color.quantum_grey800));
                messages.setTextSize(10);

                cancel.setBackgroundResource(R.drawable.btn_login_shape);
                cancel.setTextColor(getResources().getColor(R.color.quantum_white_100));
                cancel.setTextSize(12);



                messagesArray.clear();
                getCancellationRequests();
                recyclerView.setVisibility(View.GONE);
                recyclerViewAppoint.setVisibility(View.GONE);
                recyclerViewCancling.setVisibility(View.VISIBLE);


            }
        });




        return v;
    }


    public void getParticipatingRequests(){

        pd.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentsRequest.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

               // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        String id = x.getString("req_id");
                        String student_email = x.getString("student_email");
                        String teacher_email = x.getString("teacher_email");
                        String date = x.getString("suggested_time");
                        getStudentData(student_email,date,id);



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











    public void getStudentData(String emailStudent,String date,String id){

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
                        int  id = x.getInt("id");
                        String email = x.getString("email");
                        String fname = x.getString("fname");
                        String lname = x.getString("lname");
                        String phone = x.getString("phone");
                        String city = x.getString("city");
                        String age = x.getString("age");
                        String lat = x.getString("lat");
                        String lng = x.getString("lng");
                        String profileImg = x.getString("profileImg");


                        Requests request=new Requests();
                        Currrent_Student.id=id;
                        request.setSuggestedTime(date);
                        request.setStudentEmail(email);
                        request.setFname(fname);
                        request.setLname(lname);
                        request.setCity(city);
                        request.setProfileImg(profileImg);
                        request.setPhone(phone);
                        request.setAge(age);
                        request.setLat(lat);
                        request.setLng(lng);
                        requests.add(request);







                    }
                    prepareArray(requests);



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
    public void prepareArray(ArrayList<Requests> req){
       RecyclerViewAllRequests recycler=new RecyclerViewAllRequests(getActivity(),req,this);
       recyclerView.setAdapter(recycler);


    }




    public void getAppointmentsRequests(){

       pd.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id=x.getInt("id");
                        String studentEmail=x.getString("student_email");
                        String teacher_email=x.getString("teacher_email");
                        String date=x.getString("date");
                        String post=x.getString("post");
                        String studentId=x.getString("studentId");
                        String teacherId=x.getString("teacherId");
                        String lessonsId=x.getString("lessonsId");
                        String status=x.getString("status");
                        String name=x.getString("name");
                        String img=x.getString("img");

                        Messages am=new Messages();
                        am.setStudentEmail(studentEmail);
                        am.setTeacherEmail(teacher_email);
                        am.setDate(date);
                        am.setId(id);
                        am.setLessId(lessonsId);
                        am.setStudentEmail(studentId);
                        am.setTeacherEmail(teacher_email);
                        am.setPost(post);
                        am.setStatus(status);
                        am.setName(name);
                        am.setImage(img);
                        messagesArray.add(am);



                    }
                    RecyclerAppointmentsMessage recApp=new RecyclerAppointmentsMessage(getActivity(),messagesArray);
                    recyclerViewAppoint.setAdapter(recApp);





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

     pd.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getTeacherMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

               //  Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id=x.getInt("id");
                        String studentEmail=x.getString("student_email");
                        String teacher_email=x.getString("teacher_email");
                        String date=x.getString("date");
                        String post=x.getString("post");
                        String studentId=x.getString("studentId");
                        String teacherId=x.getString("teacherId");
                        String lessonsId=x.getString("lessonsId");
                        String status=x.getString("status");
                        String name=x.getString("name");
                        String img=x.getString("img");

                        Messages am=new Messages();
                        am.setStudentEmail(studentEmail);
                        am.setTeacherEmail(teacher_email);
                        am.setDate(date);
                        am.setId(id);
                        am.setLessId(lessonsId);
                        am.setTeacherEmail(teacher_email);
                        am.setPost(post);
                        am.setStatus(status);
                        am.setName(name);
                        am.setImage(img);
                        messagesArray.add(am);



                    }
                    RecyclerCancellationRequests recCancel=new RecyclerCancellationRequests(getActivity(),messagesArray);
                    recyclerViewCancling.setAdapter(recCancel);






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

    public void goToStudentDetails(){

        Intent i=new Intent(requireActivity(), StudentDetails.class);
        requireActivity().startActivity(i);
    }



    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(Flags.REFRESH){
            requests.clear();
             pd.show();

            getParticipatingRequests();
            Flags.REFRESH=false;

        }


    }

}
