package com.murad.project1.activites.student_activites.ui.Exams;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExamsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExamsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}