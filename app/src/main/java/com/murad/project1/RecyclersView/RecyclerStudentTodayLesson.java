package com.murad.project1.RecyclersView;
/*

public class RecyclerStudentTodayLesson {
}
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.murad.project1.R;
import com.murad.project1.lessonsClasses.Lessons;

import java.util.ArrayList;


public class  RecyclerStudentTodayLesson extends    RecyclerView.Adapter<RecyclerStudentTodayLesson.viewitem> {


    ArrayList<Lessons> items;
    Context context;
    String studId, StudFname, StudLname, Studemail, StudCity, StudPhone, Studage, StudProfileImg;
    boolean flag = true;

    public RecyclerStudentTodayLesson(Context c, ArrayList<Lessons> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        TextView  date;




        //initialize
        public viewitem(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);



        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_schedule_today, parent, false);


        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        holder.date.setText(items.get(position).getDate());






    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}