package com.murad.project1.RecyclersView;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
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
import com.murad.project1.supportClasses.Current_Teacher;
import com.murad.project1.lessonsClasses.Lessons;
import com.murad.project1.supportClasses.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class  RecyclerTodaysLessons extends    RecyclerView.Adapter<RecyclerTodaysLessons.viewitem> {



    ArrayList<Lessons> items;
    Context context;

    public  RecyclerTodaysLessons(Context c, ArrayList<Lessons> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {

        //Declare
        ImageView studentImage;
        TextView name,date;
        Button startButton;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            studentImage=itemView.findViewById(R.id.profileImg);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            startButton=itemView.findViewById(R.id.startBtn);



        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_schedual, parent, false);


        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getProfileImg()).into(holder.studentImage);
        holder.name.setText(items.get(position).getFname()+" "+items.get(position).getLname());
        holder.date.setText(items.get(position).getDate());
        
        holder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<Lessons>currentLessonInfo=new ArrayList<>();
                Lessons lessons=new Lessons();
                     lessons.setSession_id(items.get(position).getSession_id());
                     lessons.setDate(items.get(position).getDate());
                     lessons.setLat(items.get(position).getLat());
                     lessons.setLng(items.get(position).getLng());
                     lessons.setAge(items.get(position).getAge());
                     lessons.setStudent_email(items.get(position).getStudent_email());
                     lessons.setFname(items.get(position).getFname());
                     lessons.setLname(items.get(position).getLname());
                     lessons.setPhone(items.get(position).getPhone());
                     lessons.setTeacher_email(items.get(position).getTeacher_email());
                     lessons.setCity(items.get(position).getCity());
                     lessons.setProfileImg(items.get(position).getProfileImg());
                     lessons.setRate(items.get(position).getRate());
                     currentLessonInfo.add(lessons);
                     checkIfReadyFound(items.get(position).getSession_id(), v,currentLessonInfo);


            }
        });


    }

    private void updateLessonStatus(long id,View v,ArrayList<Lessons> currentLessonInfo) {
        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "updateLessonStatus.php";




        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1")) {

                        // this code to save the last lesson we started in the Mobile database
                        // when the lesson is finished , it will be removed
                        Paper.init(context);
                        Paper.book().write("currentLesson", currentLessonInfo);
                        Toast.makeText(context, "Lesson in ready mode now", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_profile_to_navigation_dashboard);
                    }
                    else
                    {}
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
                Toast.makeText(context, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("lessId", String.valueOf(id));
                param.put("newStatus","ready");


                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }

    private void checkIfReadyFound(long pos,View v,ArrayList<Lessons> currLess){


        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "checkIfReady.php";




        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1")) {
                        Toast.makeText(context, "there is a running lesson at this time", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        updateLessonStatus(pos,v,currLess);


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
                Toast.makeText(context, errorDescription,Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return items.size();
    }




    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}
