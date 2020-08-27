package com.murad.project1.RecyclersView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.murad.project1.QuestionClasses.WrongAnswer;
import com.murad.project1.R;

import java.util.ArrayList;


public class  RecyclerWrongAnswers  extends    RecyclerView.Adapter<RecyclerWrongAnswers  .viewitem> {


    ArrayList<WrongAnswer> items;
    Context context;



    public RecyclerWrongAnswers  (Context c, ArrayList<WrongAnswer> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        TextView question,wrongAns,rightAns;
        ImageView imageView;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
           question=itemView.findViewById(R.id.quesTxt);
           imageView=itemView.findViewById(R.id.questionImg2);
           wrongAns=itemView.findViewById(R.id.wrongAnswerTxt);
           rightAns=itemView.findViewById(R.id.rightAnswerTxt);





        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_wrong_ans, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImg()).into(holder.imageView);
        holder.question.setText(items.get(position).getQuestion());
        holder.rightAns.setText(items.get(position).getRight_Ans());
        holder.wrongAns.setText(items.get(position).getSelected_Ans());

        if(!TextUtils.isEmpty(items.get(position).getImg())){
            LinearLayout.LayoutParams LL=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500);
            holder.imageView.setLayoutParams(LL);
        }else{
            LinearLayout.LayoutParams LL=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.imageView.setLayoutParams(LL);
        }





    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
