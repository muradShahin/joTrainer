package com.murad.project1.activites.student_activites.ui.StudyFragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerViewSignCatalog;
import com.murad.project1.supportClasses.SignCatalog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignFragment extends Fragment {
    private RecyclerView recyclerView;


    public SignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root= inflater.inflate(R.layout.fragment_sign, container, false);
       recyclerView=root.findViewById(R.id.rec);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
       prepareArray();



    return root;
    }

    private void prepareArray() {
        ArrayList<SignCatalog> ar=new ArrayList<>();
        SignCatalog sc=new SignCatalog();
        sc.setTitle("Warning signs");
        sc.setImage("https://firebasestorage.googleapis.com/v0/b/project1-62ba3.appspot.com/o/imgTrafficsigns%2Fwarning.png?alt=media&token=f820335f-f34b-4b73-bcd0-ddc49521b52d");
        ar.add(sc);

        sc=new SignCatalog();
        sc.setTitle("Indicative signs");
        sc.setImage("https://firebasestorage.googleapis.com/v0/b/project1-62ba3.appspot.com/o/imgTrafficsigns%2Findicative.png?alt=media&token=7c53579a-6ef8-402a-8a87-fa54e3287115");
        ar.add(sc);

        sc=new SignCatalog();
        sc.setTitle("Priority signs");
        sc.setImage("https://firebasestorage.googleapis.com/v0/b/project1-62ba3.appspot.com/o/imgTrafficsigns%2F13.jpg?alt=media&token=41bdb064-b477-41cc-9267-7103b36aecca");
        ar.add(sc);

        sc=new SignCatalog();
        sc.setImage("https://firebasestorage.googleapis.com/v0/b/project1-62ba3.appspot.com/o/imgTrafficsigns%2F35.jpg?alt=media&token=4db4ee43-149c-4508-8a2d-bd2265be0459");
        sc.setTitle("Compulsory signs");
        ar.add(sc);

        sc=new SignCatalog();
        sc.setImage("https://firebasestorage.googleapis.com/v0/b/project1-62ba3.appspot.com/o/imgTrafficsigns%2Fprevention.png?alt=media&token=31f99f15-544a-44bc-b1e4-e573438404c0");
        sc.setTitle("Prevention signs");
        ar.add(sc);

        RecyclerViewSignCatalog recyclerViewSignCatalog=new RecyclerViewSignCatalog(getActivity(),ar);
        recyclerView.setAdapter(recyclerViewSignCatalog);

    }

}
