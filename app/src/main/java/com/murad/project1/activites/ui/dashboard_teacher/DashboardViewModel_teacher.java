package com.murad.project1.activites.ui.dashboard_teacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel_teacher extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel_teacher() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}