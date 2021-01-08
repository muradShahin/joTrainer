package com.murad.project1.RecyclersView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.murad.project1.activites.StudentDetails;
import com.murad.project1.activites.ui.preDefinedFragments.allRequests;
import com.murad.project1.contractClasses.Requests;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Flags;
import com.murad.project1.supportClasses.currentStudentDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class  RecyclerViewAllRequests extends    RecyclerView.Adapter< RecyclerViewAllRequests.viewitem> {



    ArrayList<Requests> items;
    Context context;
    String studId,StudFname,StudLname,Studemail,StudCity,StudPhone,Studage,StudProfileImg;
    allRequests fragment;


    public  RecyclerViewAllRequests(Context c, ArrayList<Requests> item, allRequests fragment)
    {
        items=item;
        context=c;
        this.fragment=fragment;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {

        //Declare
        ImageView studentImage;
        TextView name,city,date;
        Button moreDetails,declineRequest;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            studentImage=itemView.findViewById(R.id.studImg);
            name=itemView.findViewById(R.id.name);
            city=itemView.findViewById(R.id.city);
            date=itemView.findViewById(R.id.date);
            moreDetails=itemView.findViewById(R.id.details);
            declineRequest=itemView.findViewById(R.id.decline);



        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_request, parent, false);


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
        holder.city.setText("City : "+items.get(position).getCity());
        holder.date.setText("suggested date : "+items.get(position).getSuggestedTime());

        holder.moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStudentDetails.fname=items.get(position).getFname();
                currentStudentDetails.lname=items.get(position).getLname();
                currentStudentDetails.age=items.get(position).getAge();
                currentStudentDetails.city=items.get(position).getCity();
                currentStudentDetails.phone=items.get(position).getPhone();
                currentStudentDetails.studentEmail=items.get(position).getStudentEmail();
                currentStudentDetails.profileImg=items.get(position).getProfileImg();
                currentStudentDetails.lat=items.get(position).getLat();
                currentStudentDetails.lng=items.get(position).getLng();
                currentStudentDetails.suggestedTime=items.get(position).getSuggestedTime();
                Flags.Go_as_ACCEPTED_STUDENTS=false;
                fragment.goToStudentDetails();



            }
        });

        holder.declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteStudentFromRequestList(items.get(position).getStudentEmail(),position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


    }
    public void deleteStudentFromRequestList(String email,int pos){
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
