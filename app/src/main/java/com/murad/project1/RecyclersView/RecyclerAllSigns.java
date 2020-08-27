package com.murad.project1.RecyclersView;

/*
public class RecyclerAllSigns {
}
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Signs;

import java.util.ArrayList;

public class  RecyclerAllSigns  extends    RecyclerView.Adapter<RecyclerAllSigns .viewitem> {


    ArrayList<Signs> items;
    Context context;


    public RecyclerAllSigns (Context c, ArrayList<Signs> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare

        ImageView sign;
        TextView description;

        //initialize
        public viewitem(View itemView) {
            super(itemView);

            sign=itemView.findViewById(R.id.sImg);
            description=itemView.findViewById(R.id.desc);



        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sign, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImage()).into(holder.sign);
        holder.description.setText(items.get(position).getDesc());



    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}

