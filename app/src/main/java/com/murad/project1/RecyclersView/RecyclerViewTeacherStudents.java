package com.murad.project1.RecyclersView;

/*
public class RecyclerViewTeacherStudents {
}
*/

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.murad.project1.UsersClasses.TeacherStudents;
import com.murad.project1.activites.StudentDetails;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Currrent_Student;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.PerformanceModels;
import com.murad.project1.supportClasses.currentStudentDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class  RecyclerViewTeacherStudents extends    RecyclerView.Adapter< RecyclerViewTeacherStudents.viewitem> {



    ArrayList<TeacherStudents> items;
    Context context;


    public  RecyclerViewTeacherStudents(Context c, ArrayList<TeacherStudents> item)
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
        Button moreDetails;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            studentImage=itemView.findViewById(R.id.profileImg);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            moreDetails=itemView.findViewById(R.id.moreBtn);




        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_accepted_students, parent, false);


        //this on click is for the row
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });






        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getProfileImg()).into(holder.studentImage);
        holder.name.setText(items.get(position).getFname()+" "+items.get(position).getLname());
        holder.date.setText(items.get(position).getDate());

        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStudentDetails.fname=items.get(position).getFname();
                currentStudentDetails.lname=items.get(position).getLname();
                currentStudentDetails.age=items.get(position).getAge();
                currentStudentDetails.city=items.get(position).getCity();
                currentStudentDetails.phone=items.get(position).getPhone();
                currentStudentDetails.studentEmail=items.get(position).getEmail();
                currentStudentDetails.profileImg=items.get(position).getProfileImg();
                currentStudentDetails.lat=items.get(position).getLat();
                currentStudentDetails.lng=items.get(position).getLng();
                currentStudentDetails.suggestedTime=items.get(position).getDate();
                currentStudentDetails.teacherName=items.get(position).getTeacherName();
                currentStudentDetails.lessons=items.get(position).getLessons();
                currentStudentDetails.lessons_left=items.get(position).getLessons_left();
                currentStudentDetails.rate=items.get(position).getRate();
                Currrent_Student.id=items.get(position).getStudentId();
                Flags.Go_as_ACCEPTED_STUDENTS=true;
                getPerformance(items.get(position).getEmail());

            }
        });




    }
    /*public void deleteStudentFromRequestList(String email,int pos){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.show();

        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Config.url + "deleteRequest.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(context, "the request has been deleted !", Toast.LENGTH_SHORT).show();
                        removeAt(pos);


                    }
                    else
                    {Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();}
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
                Toast.makeText(context, errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email",email);

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

    private void removeAt(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos,items.size());
    }*/


    @Override
    public int getItemCount() {
        return items.size();
    }

    private void getPerformance(String student_email){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "getPerformance.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //  Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");
                    int numberOfRows=arr.length();
                    int sumCont=0;
                    int sumShifting=0;
                    int sumPrio=0;
                    int sumPark=0;
                    int sumMoni=0;


                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int controllingD= x.getInt("controling");
                        int shiftingD=x.getInt("shifting");
                        int prioritesD=x.getInt("priorities");
                        int parkingD=x.getInt("parking");
                        int monitoringD=x.getInt("monitoring");
                        sumCont+=controllingD;
                        sumShifting+=shiftingD;
                        sumPrio+=prioritesD;
                        sumPark+=parkingD;
                        sumMoni+=monitoringD;

                    }

                    if(numberOfRows>0) {
                        PerformanceModels.controling = sumCont / numberOfRows;
                        PerformanceModels.shifting = sumShifting / numberOfRows;
                        PerformanceModels.monitoring = sumMoni / numberOfRows;
                        PerformanceModels.parking = sumPark / numberOfRows;
                        PerformanceModels.priorites = sumPrio / numberOfRows;
                    }
                    Intent i=new Intent(context, StudentDetails.class);
                    context.startActivity(i);







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
                param.put("email", student_email);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);




    }


    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}