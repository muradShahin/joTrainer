package com.murad.project1.activites.student_activites;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.murad.project1.UsersClasses.TeacherStudents;
import com.murad.project1.supportClasses.CurrentDetailsOfTeacher;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentLessInfo;
import com.murad.project1.supportClasses.Flags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import devlight.io.library.ArcProgressStackView;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonInformation extends Fragment {

    private ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    private ArcProgressStackView arcProgressStackView;
    private TextView lessonNotes,date,studentName;
    private LinearLayout finishedLessonsLayout,waitingLayout;
    private Button submit;
    private ProgressBar pd,pd2;
    private RatingBar ratingBar;
    private int previousRates=0;
    private CardView cardViewRating,studentDetails;
    private ImageView studentImage;
    public LessonInformation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_lesson_information, container, false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if(CurrentLessInfo.teacher){
             //       Navigation.findNavController(v).navigate(R.id.action_lessonInformation2_to_navigation_dashboard);
                    Flags.ReturnFromLessInfo = true;
                    CurrentLessInfo.teacher=false;


                }else {
  //                  Navigation.findNavController(getView()).navigate(R.id.action_lessonInformation_to_nav_profile);
                    Flags.ReturnFromLessInfo = true;

                }
                getActivity().getSupportFragmentManager().popBackStack();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        lessonNotes=v.findViewById(R.id.notes);
        finishedLessonsLayout=v.findViewById(R.id.finishedLay);
        date=v.findViewById(R.id.dateTxt);
        waitingLayout=v.findViewById(R.id.waitingLess);
        submit=v.findViewById(R.id.submit);
        cardViewRating=v.findViewById(R.id.cvRating);
        ratingBar=v.findViewById(R.id.ratingBar);
        studentName=v.findViewById(R.id.studentName);
        studentImage=v.findViewById(R.id.studentImg);
        studentDetails=v.findViewById(R.id.studentCvDetails);
        studentDetails.setVisibility(View.GONE);
        if (CurrentLessInfo.teacher){
            ratingBar.setVisibility(View.GONE);
            cardViewRating.setVisibility(View.GONE);
            studentDetails.setVisibility(View.VISIBLE);
        }else{
            studentDetails.setVisibility(View.GONE);
        }
        //pd is for up coming lessons
        //pd2 is for rating in archive lessons
        pd=v.findViewById(R.id.progressBarSubmit);
        pd2=v.findViewById(R.id.progressBarRate);
        pd.setVisibility(View.GONE);
        pd2.setVisibility(View.GONE);
        arcProgressStackView = (ArcProgressStackView)v.findViewById(R.id.apsv2);
        models.add(new ArcProgressStackView.Model("Controlling ", CurrentLessInfo.controling, Color.parseColor("#FFEEEEEE"), Color.parseColor("#ba4c00")));
        models.add(new ArcProgressStackView.Model("Monitoring ", CurrentLessInfo.monitoring, Color.parseColor("#FFEEEEEE"), Color.parseColor("#6abf69")));
        models.add(new ArcProgressStackView.Model("Shifting", CurrentLessInfo.shifting, Color.parseColor("#FFEEEEEE"), Color.parseColor("#5eb8ff")));
        models.add(new ArcProgressStackView.Model("Parking", CurrentLessInfo.parking,Color.parseColor("#FFEEEEEE"), Color.parseColor("#fbc02d")));
        models.add(new ArcProgressStackView.Model("Priorities", CurrentLessInfo.priorites,Color.parseColor("#FFEEEEEE"), Color.parseColor("#b71c1c")));
        arcProgressStackView.setModels(models);
        studentInformation();
        lessonNotes.setText(CurrentLessInfo.notes);


        organizeLayouts();


        //handling on text field click
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        // handling submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAppointmentRequest();

            }
        });



        //handling the ratingBar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                getInstructorsRate((int) rating);

            }
        });
    return v;
    }




    private void organizeLayouts() {
      if(CurrentLessInfo.status.equals("waiting")) {
          finishedLessonsLayout.setVisibility(View.GONE);
          waitingLayout.setVisibility(View.VISIBLE);
      }else {
          finishedLessonsLayout.setVisibility(View.VISIBLE);
          waitingLayout.setVisibility(View.GONE);
      }
    }
    private void showDate(){
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
                date.setText(  new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault()).format(firstDate.getTime()));

            }
        }).setBackgroundColor(Color.parseColor("#ffffff")).setHeaderColor(Color.parseColor("#f47b00"))
                .setSelectedTextColor(Color.parseColor("#f47b00"))
                .setSelectedColor(Color.parseColor("#ffffff")).show(getActivity().getSupportFragmentManager(),"date");

    }
    private void sendAppointmentRequest(){
        pd.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "SendMessage.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(getActivity(), "Appointment update request has been sent", Toast.LENGTH_SHORT).show();
                        Flags.ReturnFromLessInfo=true;
                        Navigation.findNavController(getView()).navigate(R.id.action_lessonInformation_to_nav_profile);

                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                    }
                    pd.setVisibility(View.GONE);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                pd.setVisibility(View.GONE);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.setVisibility(View.GONE);
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
               String postContent=studentName+" asked for rescheduling the lesson appointment from: "+CurrentLessInfo.current_date+" to :"+date.getText().toString();
                param.put("id", String.valueOf(Currrent_Student.id));
                param.put("student_email", Currrent_Student.email);
                param.put("teacher_email",CurrentLessInfo.teacherEmail);
                param.put("date",date.getText().toString());
                param.put("post",postContent);
                param.put("status","pending/appoint");
                param.put("lessonId", String.valueOf(CurrentLessInfo.id));
                param.put("name", Currrent_Student.fname+" "+ Currrent_Student.lname);
                param.put("img", Currrent_Student.profileImg);
                param.put("teacherId", String.valueOf(CurrentDetailsOfTeacher.id));




                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void updateInstructorRating(int rating) {
        pd2.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "setRateTeacher.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(getActivity(), "We thank you for making this better", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
                    }
                    pd2.setVisibility(View.GONE);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                pd2.setVisibility(View.GONE);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd2.setVisibility(View.GONE);
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
                param.put("email",CurrentLessInfo.teacherEmail);
                param.put("rate",String.valueOf(rating));


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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getLessonsTeacher.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    if(arr.length()>0) {
                        int sum = (previousRates * arr.length()) + rate;
                        int avg = sum / arr.length();
                        if (avg > 5)
                            avg = 5;
                        updateInstructorRating(avg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd2.setVisibility(View.GONE);
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
                param.put("email",CurrentLessInfo.teacherEmail);
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
    private void getInstructorsRate(int rating) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        previousRates=x.getInt("rate");



                    }
                    calculateAvgOfRate(rating);




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd2.setVisibility(View.GONE);
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
                param.put("email",CurrentLessInfo.teacherEmail);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }
    private void studentInformation(){
       SweetAlertDialog pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentData.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

              // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




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
                        String profileImg = x.getString("profileImg");


                        Glide.with(getActivity()).load(profileImg).into(studentImage);
                        studentName.setText(fname+" "+lname);


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
                param.put("email",Currrent_Student.email);



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
