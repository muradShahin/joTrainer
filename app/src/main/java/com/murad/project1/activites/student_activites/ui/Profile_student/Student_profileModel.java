package com.murad.project1.activites.student_activites.ui.Profile_student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Student_profileModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Student_profileModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}