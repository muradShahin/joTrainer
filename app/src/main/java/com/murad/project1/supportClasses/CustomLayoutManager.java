package com.murad.project1.supportClasses;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLayoutManager extends LinearLayoutManager {
    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
