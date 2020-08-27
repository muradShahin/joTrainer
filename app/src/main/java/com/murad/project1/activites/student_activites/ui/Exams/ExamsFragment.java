package com.murad.project1.activites.student_activites.ui.Exams;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.murad.project1.R;

public class ExamsFragment extends Fragment {

    private ExamsViewModel examsViewModel;
    private CardView cardBtn;
    private TextView cardTxt;
    private LinearLayout cardLinLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        examsViewModel =
                ViewModelProviders.of(this).get(ExamsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_exams, container, false);
        cardBtn=root.findViewById(R.id.cardBtn);
        cardTxt=root.findViewById(R.id.cardBtnTxt);
        cardLinLayout=root.findViewById(R.id.cardLinLay);


        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardTxt.setTextColor(getResources().getColor(R.color.myOrange));
                cardLinLayout.setBackgroundColor(getResources().getColor(R.color.quantum_white_100));
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     cardTxt.setTextColor(getResources().getColor(R.color.quantum_white_100));
                     cardLinLayout.setBackgroundColor(getResources().getColor(R.color.myOrange));
                 }
             }, 200);

                new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {

                      Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_questionsFragment);

                  }
              }, 300);
            }
        });


        return root;
    }
}