package com.murad.project1.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.murad.project1.R;
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.PerformanceModels;
import com.murad.project1.supportClasses.currentStudentDetails;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import devlight.io.library.ArcProgressStackView;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class studentApplicationActivity extends AppCompatActivity {
TextView pickDate,NumberOfLessons,NumberOfLessonsLeft;
Button submitSession,edit,submitNewLessonsNumber,hideBtn;
LinearLayout editLessonsLayout;
NumberPicker lessonsNewNumber;
ProgressBar pd;
    String sessionId;
    String sessionDateWithTime;
    int lessonsNumberAfterUpdate,lessonsNumberLeftAfterUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application);
        getSupportActionBar().hide();
        pickDate=findViewById(R.id.date);
        submitSession=findViewById(R.id.submitSee);
        edit=findViewById(R.id.editLes);
        submitNewLessonsNumber=findViewById(R.id.submitLesNum);
        editLessonsLayout=findViewById(R.id.editLessons);
        NumberOfLessons=findViewById(R.id.numberLess);
        NumberOfLessonsLeft=findViewById(R.id.numberLeft);
        lessonsNewNumber=findViewById(R.id.number_picker);
        lessonsNewNumber.setMax(20);
        pd=findViewById(R.id.progressBar);
        hideBtn=findViewById(R.id.hide);


        pd.setVisibility(View.GONE);
        //student behavior diagram
        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("Controlling ", PerformanceModels.controling, Color.parseColor("#FFEEEEEE"), Color.parseColor("#ba4c00")));
        models.add(new ArcProgressStackView.Model("Monitoring ", PerformanceModels.monitoring, Color.parseColor("#FFEEEEEE"), Color.parseColor("#6abf69")));
        models.add(new ArcProgressStackView.Model("Shifting", PerformanceModels.shifting, Color.parseColor("#FFEEEEEE"), Color.parseColor("#5eb8ff")));
        models.add(new ArcProgressStackView.Model("Parking", PerformanceModels.parking,Color.parseColor("#FFEEEEEE"), Color.parseColor("#fbc02d")));
        models.add(new ArcProgressStackView.Model("Priorities", PerformanceModels.priorites,Color.parseColor("#FFEEEEEE"), Color.parseColor("#b71c1c")));

        final ArcProgressStackView arcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        arcProgressStackView.setModels(models);



        //hiding edit lessons layout
        editLessonsLayout.setVisibility(View.GONE);





        //handling pick date text on click
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();

            }
        });

        //this button is responsible about making the layout (editLessonsLayout) visible
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLessonsLayout.setVisibility(View.VISIBLE);

            }
        });

        //hiding the edit lessons layout
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLessonsLayout.setVisibility(View.GONE);

            }
        });

        //handling on click submit new lessons number
        submitNewLessonsNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonsNumberAfterUpdate=Integer.parseInt(currentStudentDetails.lessons)+lessonsNewNumber.getValue();
                lessonsNumberLeftAfterUpdate=currentStudentDetails.lessons_left+lessonsNewNumber.getValue();
              pd.setVisibility(View.VISIBLE);
              updateLessonsNumber();
            }
        });

        //getting lessons numbers
        setLessonsNumberInfo();


        //handling submit session button on click
        submitSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setVisibility(View.VISIBLE);
                sessionId=String.valueOf((int)(Math.random()*100000));
                addSession();
            }
        });


    }

    private void addSession() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "InsertSession.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                         addPerformaceOfLesson();

                    }

                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
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
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();

                param.put("id",sessionId);
                param.put("student_email",currentStudentDetails.studentEmail);
                param.put("teacher_email", Current_Teacher.email);
                param.put("date",sessionDateWithTime);
                param.put("status","waiting");
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

    private void addPerformaceOfLesson() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "setPerformance.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(studentApplicationActivity.this, "lesson have been submitted", Toast.LENGTH_SHORT).show();
                        pd.setVisibility(View.GONE);
                    }

                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
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
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();

                param.put("sessionId",sessionId);
                param.put("studentEmail",currentStudentDetails.studentEmail);
                param.put("controling","0");
                param.put("shifting","0");
                param.put("parking","0");
                param.put("monitoring","0");
                param.put("priorities","0");
                param.put("notes","");


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    private void updateLessonsNumber() {
        RequestQueue queue = Volley.newRequestQueue(this);
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
                        pd.setVisibility(View.GONE);
                        currentStudentDetails.lessons=String.valueOf(lessonsNumberAfterUpdate);
                        currentStudentDetails.lessons_left=lessonsNumberLeftAfterUpdate;
                        NumberOfLessons.setText(currentStudentDetails.lessons);
                        NumberOfLessonsLeft.setText(String.valueOf(currentStudentDetails.lessons_left));

                        Toast.makeText(studentApplicationActivity.this, "student lessons updated successfully", Toast.LENGTH_SHORT).show();

                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
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
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("newLessNumber",String.valueOf(lessonsNumberAfterUpdate));
                param.put("email",currentStudentDetails.studentEmail);
                param.put("lessNleft",String.valueOf(lessonsNumberLeftAfterUpdate));


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    public void setLessonsNumberInfo(){
        int numberOfLessons= Integer.parseInt(currentStudentDetails.lessons);
        NumberOfLessons.setText(String.valueOf(numberOfLessons));
        NumberOfLessonsLeft.setText(String.valueOf(currentStudentDetails.lessons_left));

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
                pickDate.setText(  new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime()));
                sessionDateWithTime=new SimpleDateFormat(getString(R.string.timeFormat),Locale.getDefault()).format(firstDate.getTime());
                Toast.makeText(studentApplicationActivity.this, sessionDateWithTime, Toast.LENGTH_SHORT).show();

            }
        }).setBackgroundColor(Color.parseColor("#ffffff")).setHeaderColor(Color.parseColor("#f47b00"))
                .setSelectedTextColor(Color.parseColor("#f47b00"))
                .setSelectedColor(Color.parseColor("#ffffff")).show(getSupportFragmentManager(),"date");

    }
}
