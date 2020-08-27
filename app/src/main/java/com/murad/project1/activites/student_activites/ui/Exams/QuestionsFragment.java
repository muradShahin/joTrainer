package com.murad.project1.activites.student_activites.ui.Exams;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.murad.project1.QuestionClasses.Question;
import com.murad.project1.QuestionClasses.WrongAnswer;
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerQuestions;
import com.murad.project1.RecyclersView.RecyclerWrongAnswers;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentAnswer;
import com.murad.project1.supportClasses.CustomLayoutManager;
import com.murad.project1.supportClasses.Score;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment {
    ProgressBar pr;
    RecyclerView recyclerView,recyclerViewWrongAns;
    ArrayList<Question> Questions=new ArrayList<>();
    ArrayList<Question> Questions_20=new ArrayList<>();
    ArrayList<WrongAnswer> wrongAnswersArray=new ArrayList<>();
        Button next,finish;
        int recyclerPos;
        CardView cardViewScore;
        TextView finalScore;
        LinearLayout ScoreLayout,QuestionsLay;



    public QuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_questions, container, false);
        pr=v.findViewById(R.id.progressBarQues);
        pr.setVisibility(GONE);

        recyclerView=v.findViewById(R.id.recQues);
        recyclerViewWrongAns=v.findViewById(R.id.rec2);
        next=v.findViewById(R.id.nextBtn);

        cardViewScore=v.findViewById(R.id.finalCard);
        ScoreLayout=v.findViewById(R.id.scoreLay);
        QuestionsLay=v.findViewById(R.id.questionLay);
        ScoreLayout.setVisibility(GONE);


        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewWrongAns.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        getAllQuestions();

        Score.finalScore=0;


        finalScore=v.findViewById(R.id.Score);
        finish=v.findViewById(R.id.finishBtn);

       recyclerPos=1;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(Score.userAns)) {
                    Toast.makeText(getActivity(), "answer the question please !", Toast.LENGTH_SHORT).show();
                }else {
                    if (Score.userAns.equals(Score.rightAns)) {
                        Score.finalScore += 1;
                    }else{
                        WrongAnswer wa=new WrongAnswer();
                        wa.setQuestion(CurrentAnswer.question);
                        wa.setImg(CurrentAnswer.image);
                        wa.setRight_Ans(CurrentAnswer.right_Ans);
                        wa.setSelected_Ans(CurrentAnswer.selected_Ans);
                        wrongAnswersArray.add(wa);

                    }

                    recyclerView.scrollToPosition(recyclerPos);

                    if (recyclerPos > Questions_20.size() - 1) {
                        RecyclerWrongAnswers recyclerWrongAnswers=new RecyclerWrongAnswers(getActivity(),wrongAnswersArray);
                        recyclerViewWrongAns.setAdapter(recyclerWrongAnswers);
                        QuestionsLay.setVisibility(GONE);
                        ScoreLayout.setVisibility(View.VISIBLE);
                        if (Score.finalScore > 30)
                            Score.finalScore = 30;

                        finalScore.setText(Score.finalScore + " / " + Questions_20.size());
                    }
                    recyclerPos++;
                    Score.userAns="";
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_questionsFragment_to_nav_slideshow);
                Score.finalScore=0;
                Questions_20.clear();

            }
        });



    return v;
    }
    private void getAllQuestions(){
        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getAllQuestions.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

             //    Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        String ques=x.getString("question");
                        String ans1=x.getString("ans1");
                        String ans2=x.getString("ans2");
                        String ans3=x.getString("ans3");
                        String ans4=x.getString("ans4");
                        String image=x.getString("image");
                        String answer=x.getString("answer");

                        Question q=new Question();
                        q.setQuestion(ques);
                        q.setAns1(ans1);
                        q.setAns2(ans2);
                        q.setAns3(ans3);
                        q.setAns4(ans4);
                        q.setImg(image);
                        q.setAnswer(answer);
                        Questions.add(q);


                    }
                  ArrayList<Integer> numbers = new ArrayList<Integer>();
                    Random randomGenerator = new Random();
                    while (numbers.size() < 30) {

                        int random = (int)(Math.random()*Questions.size());
                        if (!numbers.contains(random)) {
                            numbers.add(random);
                        }
                    }
                    for (int i=0;i<numbers.size();i++){
                        /*Question q=new Question();
                        q.setQuestion(Questions.get(i).getQuestion());
                        q.setAns1(Questions.get(i).getAns1());
                        q.setAns2(Questions.get(i).getAns2());
                        q.setAns3(Questions.get(i).getAns3());
                        q.setAns4(Questions.get(i).getAns4());
                        q.setImg(Questions.get(i).getImg());
                        q.setAnswer(Questions.get(i).getAnswer());
                        Questions_20.add(q);*/
                        Questions_20.add(Questions.get(numbers.get(i)));
                    }
                    pr.setVisibility(GONE);
                    RecyclerQuestions recyclerQuestions=new RecyclerQuestions(getActivity(),Questions_20);
                    recyclerView.setAdapter(recyclerQuestions);




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pr.setVisibility(GONE);
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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();




                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }



}
