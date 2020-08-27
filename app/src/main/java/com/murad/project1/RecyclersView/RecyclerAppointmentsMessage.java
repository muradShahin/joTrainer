package com.murad.project1.RecyclersView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.murad.project1.contractClasses.Messages;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Current_Teacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  RecyclerAppointmentsMessage extends    RecyclerView.Adapter< RecyclerAppointmentsMessage.viewitem> {


    ArrayList<Messages> items;
    Context context;
    ProgressDialog pd;
    private String status;


    public RecyclerAppointmentsMessage(Context c, ArrayList<Messages> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        ImageView student_Img;
        TextView post;
        Button accept,decline;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
            student_Img=itemView.findViewById(R.id.studImage);
            post=itemView.findViewById(R.id.posttxt);
            accept=itemView.findViewById(R.id.acceptBtn);
            decline=itemView.findViewById(R.id.declineBtn);


        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_appointments_req, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImage()).into(holder.student_Img);
        holder.post.setText(items.get(position).getPost());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                status="accepted";
                                updateLessonDate(items.get(position).getLessId(),items.get(position).getDate(),items.get(position).getId(),position);


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure about accepting this request?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();






            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                status="declined";
                                updateLessonDate(items.get(position).getLessId(),items.get(position).getDate(),items.get(position).getId(),position);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure about refusing this request?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();




            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    private void updateLessonDate(String lessId, String date, int id, int position){
        pd=new ProgressDialog(context);
        pd.setMessage("Processing...");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "updateLessonDate.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        updateAppointmentRequestStatus(id,position);

                    }

                    else
                    {
                        Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();}
                    pd.dismiss();

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
                param.put("id",lessId);
                param.put("date",date);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }

    private void updateAppointmentRequestStatus(int id,int pos){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url =   Config.url + "updateAppointmentStatus.php";


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(context, "Request approved !", Toast.LENGTH_SHORT).show();
                        removeAt(pos);

                    }

                    else
                    {
                        Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();

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
                param.put("id", String.valueOf(id));
                param.put("status","accepted");
                param.put("img", Current_Teacher.profileImg);




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


}


