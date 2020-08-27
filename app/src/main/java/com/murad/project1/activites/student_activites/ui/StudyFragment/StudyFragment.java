package com.murad.project1.activites.student_activites.ui.StudyFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

//import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.PDFView;
import com.murad.project1.R;


public class StudyFragment extends Fragment {
    private StudyViewModel StudyViewModel;
    private CardView book,signs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StudyViewModel =
                ViewModelProviders.of(this).get(StudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_study, container, false);
        book=root.findViewById(R.id.cardBook);
        signs=root.findViewById(R.id.cardSigns);
        //Navigation.findNavController(root).navigate(R.id.action_nav_tools_to_bookFragment);


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_tools_to_bookFragment);
            }
        });

        signs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_tools_to_signFragment);

            }
        });

        return root;
    }
}