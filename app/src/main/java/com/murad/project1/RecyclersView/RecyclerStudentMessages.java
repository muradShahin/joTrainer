package com.murad.project1.RecyclersView;

/*
public class RecyclerStudentMessages {
}
*/
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  RecyclerStudentMessages extends    RecyclerView.Adapter< RecyclerStudentMessages.viewitem> {


    ArrayList<Messages> items;
    Context context;
    ProgressDialog pd;
    private String status;


    public RecyclerStudentMessages(Context c, ArrayList<Messages> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        TextView post,resultText;
        ImageView senderImage;



        //initialize
        public viewitem(View itemView) {
            super(itemView);

            post=itemView.findViewById(R.id.posttxt);
            resultText=itemView.findViewById(R.id.result);
            senderImage=itemView.findViewById(R.id.senderImg);



        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_messages, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        holder.post.setText(items.get(position).getPost());
        holder.resultText.setText("Request has been "+items.get(position).getStatus());
        Glide.with(context).load(items.get(position).getImage()).into(holder.senderImage);


        if(items.get(position).getStatus().equalsIgnoreCase("message")){
            holder.resultText.setVisibility(View.GONE);
        }

        if(items.get(position).getStatus().equalsIgnoreCase("accepted")){

            holder.resultText.setTextColor(context.getResources().getColor(R.color.quantum_vanillagreen600));
        }else{
            holder.resultText.setTextColor(context.getResources().getColor(R.color.quantum_googred600));

        }



    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    private void removeAt(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos,items.size());
    }


}


