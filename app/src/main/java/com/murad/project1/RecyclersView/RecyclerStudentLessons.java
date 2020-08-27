package com.murad.project1.RecyclersView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.murad.project1.R;
import com.murad.project1.lessonsClasses.Lessons;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentLessInfo;
import com.murad.project1.supportClasses.PerformanceModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class  RecyclerStudentLessons extends    RecyclerView.Adapter<RecyclerStudentLessons.viewitem> {



    ArrayList<Lessons> items;
    Context context;


    public  RecyclerStudentLessons(Context c, ArrayList<Lessons> item)
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
        TextView date;
        Button moreDetails;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
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
                .inflate(R.layout.row_schedule_stud, parent, false);


        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        holder.date.setText(items.get(position).getDate());
        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  CurrentLessInfo.id=items.get(position).getSession_id();
                  CurrentLessInfo.status=items.get(position).getStatus();
                  CurrentLessInfo.teacherEmail=items.get(position).getTeacher_email();
                  CurrentLessInfo.current_date=items.get(position).getDate();
                  getArchiveLessonInfo(items.get(position).getSession_id(),v);

            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    private void getArchiveLessonInfo(long id, View v){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "getArchivedLessInfo.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

               // Toast.makeText(context,s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int controllingScore=x.getInt("controling");
                        int shiftingScore=x.getInt("shifting");
                        int parkingScore=x.getInt("parking");
                        int monitoringScore=x.getInt("monitoring");
                        int prioritiesScore=x.getInt("priorities");
                        String notes=x.getString("notes");
                    CurrentLessInfo.controling=controllingScore;
                    CurrentLessInfo.monitoring=monitoringScore;
                    CurrentLessInfo.shifting=shiftingScore;
                    CurrentLessInfo.parking=parkingScore;
                    CurrentLessInfo.priorites=prioritiesScore;
                    CurrentLessInfo.notes=notes;

                    }
                    Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_lessonInformation);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //   progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //    progressBar.setVisibility(View.GONE);

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
                param.put("id", String.valueOf(id));




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