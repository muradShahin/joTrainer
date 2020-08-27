package com.murad.project1.RecyclersView;

/*
public class RecyclerAllTeachers {
}
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.murad.project1.R;
import com.murad.project1.UsersClasses.Teacher;
import com.murad.project1.supportClasses.CurrentDetailsOfTeacher;

import java.util.ArrayList;


public class  RecyclerAllTeachers extends    RecyclerView.Adapter< RecyclerAllTeachers.viewitem> {


    ArrayList<Teacher> items;
    Context context;


    public RecyclerAllTeachers(Context c, ArrayList<Teacher> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        TextView name;
        ImageView profileImg;
        Button showButton;
        ImageView starOne,starTwo,starThree,starFour,starFive;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            profileImg=itemView.findViewById(R.id.profileImg);
            showButton=itemView.findViewById(R.id.moreDetails);
            starOne=itemView.findViewById(R.id.star1);
            starTwo=itemView.findViewById(R.id.star2);
            starThree=itemView.findViewById(R.id.star3);
            starFour=itemView.findViewById(R.id.star4);
            starFive=itemView.findViewById(R.id.star5);


        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teachers, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        holder.name.setText(items.get(position).getFname()+" "+items.get(position).getLname());
        Glide.with(context).load(items.get(position).getProfileImg()).into(holder.profileImg);

        switch (items.get(position).getRate()){
            case "1":
                holder.starFive.setVisibility(View.GONE);
                holder.starFour.setVisibility(View.GONE);
                holder.starThree.setVisibility(View.GONE);
                holder.starTwo.setVisibility(View.GONE);
                break;
            case "2":
                holder.starFive.setVisibility(View.GONE);
                holder.starFour.setVisibility(View.GONE);
                holder.starThree.setVisibility(View.GONE);
                break;

            case "3" :
                holder.starFive.setVisibility(View.GONE);
                holder.starFour.setVisibility(View.GONE);
                break;
            case "4":
                holder.starFive.setVisibility(View.GONE);
                break;
        }
        holder.showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentDetailsOfTeacher.id=items.get(position).getId();
                CurrentDetailsOfTeacher.fname=items.get(position).getFname();
                CurrentDetailsOfTeacher.lname=items.get(position).getLname();
                CurrentDetailsOfTeacher.rate=items.get(position).getRate();
                CurrentDetailsOfTeacher.email=items.get(position).getEmail();
                CurrentDetailsOfTeacher.city=items.get(position).getCity();
                CurrentDetailsOfTeacher.profileImg=items.get(position).getProfileImg();
                CurrentDetailsOfTeacher.carImg=items.get(position).getCarImg();
                CurrentDetailsOfTeacher.carType=items.get(position).getCarType();
                CurrentDetailsOfTeacher.exp=items.get(position).getExp();
                CurrentDetailsOfTeacher.age=items.get(position).getAge();
                CurrentDetailsOfTeacher.phone=items.get(position).getPhone();
                CurrentDetailsOfTeacher.ENROLLED_STUDENT=false;

                 items.clear();
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_moreTeachDetails);
            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}


