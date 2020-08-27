package com.murad.project1.supportClasses;

import ir.mirrajabi.searchdialog.core.Searchable;

public class FilterClass implements Searchable {
    private String title;

    public FilterClass(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
    public FilterClass setTitle(String title) {
       this.title = title;
        return this;
    }
}
