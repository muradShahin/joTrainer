package com.murad.project1.RecyclersView;

/*
public class RecyclerViewSignCatalog {
}
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.murad.project1.R;
import com.murad.project1.supportClasses.CurrentCatalog;
import com.murad.project1.supportClasses.SignCatalog;

import java.util.ArrayList;

public class  RecyclerViewSignCatalog  extends    RecyclerView.Adapter<RecyclerViewSignCatalog .viewitem> {


    ArrayList<SignCatalog> items;
    Context context;


    public RecyclerViewSignCatalog (Context c, ArrayList<SignCatalog> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
         CardView catalogCard;
         ImageView sign;
         TextView title;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            catalogCard=itemView.findViewById(R.id.cv);
            sign=itemView.findViewById(R.id.signImage);
            title=itemView.findViewById(R.id.signTitle);



        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_catalog, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImage()).into(holder.sign);
        holder.title.setText(items.get(position).getTitle());
       switch (items.get(position).getTitle()){
            case "Warning signs":holder.catalogCard.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_yellow400));
            break;

           case "Priority signs":holder.catalogCard.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_vanillablueA400));
            break;

           case "Indicative signs":holder.catalogCard.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_googgreen500));
           break;

           case "Compulsory signs":holder.catalogCard.setCardBackgroundColor(context.getResources().getColor(R.color.myOrange));
           break;

           case "Prevention signs":holder.catalogCard.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_googred900));
             break;
        }
        holder.catalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentCatalog.title=items.get(position).getTitle();
                Navigation.findNavController(v).navigate(R.id.action_signFragment_to_insideSignFragment);
            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}

