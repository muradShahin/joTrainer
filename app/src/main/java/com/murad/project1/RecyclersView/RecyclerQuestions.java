package com.murad.project1.RecyclersView;



import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.murad.project1.QuestionClasses.Question;
import com.murad.project1.R;
import com.murad.project1.supportClasses.CurrentAnswer;
import com.murad.project1.supportClasses.Score;

import java.util.ArrayList;


public class  RecyclerQuestions extends    RecyclerView.Adapter<RecyclerQuestions .viewitem> {


    ArrayList<Question> items;
    Context context;



    public RecyclerQuestions (Context c, ArrayList<Question> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {

        //Declare
        TextView Qnumber,QuestionContent;
        ImageView quesImg;
        RadioButton answer1,answer2,answer3,answer4,defult;
        RadioGroup radioGroup;
        String studentAnswer;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            Qnumber=itemView.findViewById(R.id.number);
            QuestionContent=itemView.findViewById(R.id.quesTxt);
            answer1=itemView.findViewById(R.id.ans1);
            answer2=itemView.findViewById(R.id.ans2);
            answer3=itemView.findViewById(R.id.ans3);
            answer4=itemView.findViewById(R.id.ans4);
            quesImg=itemView.findViewById(R.id.questionImg);
            radioGroup=itemView.findViewById(R.id.radioGroub);
            defult=itemView.findViewById(R.id.hiddenRadio);





        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_row, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImg()).into(holder.quesImg);
        holder.Qnumber.setText((position+1)+" )");
        holder.QuestionContent.setText(items.get(position).getQuestion());
        holder.answer1.setText(items.get(position).getAns1());
        holder.answer2.setText(items.get(position).getAns2());
        holder.answer3.setText(items.get(position).getAns3());
        holder.answer4.setText(items.get(position).getAns4());
        Score.rightAns=items.get(position).getAnswer();

        holder.answer3.setVisibility(View.VISIBLE);
        holder.answer4.setVisibility(View.VISIBLE);

        holder.answer1.setChecked(false);
        holder.answer2.setChecked(false);
        holder.answer3.setChecked(false);
        holder.answer4.setChecked(false);
        holder.defult.setChecked(true);
        holder.defult.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(items.get(position).getImg())){
            LinearLayout.LayoutParams LL=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500);
            holder.quesImg.setLayoutParams(LL);
        }else{
            LinearLayout.LayoutParams LL=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.quesImg.setLayoutParams(LL);
        }

        if(TextUtils.isEmpty(items.get(position).getAns3())){
            holder.answer3.setVisibility(View.GONE);
            holder.answer4.setVisibility(View.GONE);

        }else if(TextUtils.isEmpty(items.get(position).getAns4())){
            holder.answer4.setVisibility(View.GONE);

        }
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ans1:
                        Score.userAns=holder.answer1.getText().toString();
                        CurrentAnswer.question=items.get(position).getQuestion();
                        CurrentAnswer.image=items.get(position).getImg();
                        CurrentAnswer.right_Ans=items.get(position).getAnswer();
                        CurrentAnswer.selected_Ans=holder.answer1.getText().toString();

                        break;
                    case R.id.ans2:
                        Score.userAns=holder.answer2.getText().toString();
                        CurrentAnswer.question=items.get(position).getQuestion();
                        CurrentAnswer.image=items.get(position).getImg();
                        CurrentAnswer.right_Ans=items.get(position).getAnswer();
                        CurrentAnswer.selected_Ans=holder.answer2.getText().toString();
                        break;
                    case R.id.ans3:
                        Score.userAns=holder.answer3.getText().toString();
                        CurrentAnswer.question=items.get(position).getQuestion();
                        CurrentAnswer.image=items.get(position).getImg();
                        CurrentAnswer.right_Ans=items.get(position).getAnswer();
                        CurrentAnswer.selected_Ans=holder.answer3.getText().toString();
                        break;
                    case R.id.ans4:
                        Score.userAns=holder.answer4.getText().toString();
                        CurrentAnswer.question=items.get(position).getQuestion();
                        CurrentAnswer.image=items.get(position).getImg();
                        CurrentAnswer.right_Ans=items.get(position).getAnswer();
                        CurrentAnswer.selected_Ans=holder.answer4.getText().toString();
                        break;
                }
            }
        });







    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}

/*

        holder. radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ans1:
                        holder.studentAnswer=holder.answer1.getText().toString();
                        if(holder.studentAnswer.equals(items.get(position).getAnswer()))
                            Score.finalScore +=1;
                        break;
                    case R.id.ans2:
                        holder.studentAnswer=holder.answer2.getText().toString();
                        if(holder.studentAnswer.equals(items.get(position).getAnswer()))
                            Score.finalScore+=1;
                        break;
                    case R.id.ans3:
                        holder.studentAnswer=holder.answer3.getText().toString();
                        if(holder.studentAnswer.equals(items.get(position).getAnswer()))
                            Score.finalScore+=1;
                        break;
                    case R.id.ans4:
                        holder.studentAnswer=holder.answer4.getText().toString();
                        if(holder.studentAnswer.equals(items.get(position).getAnswer()))
                            Score.finalScore+=1;
                        break;
                }
            }
        });

 */