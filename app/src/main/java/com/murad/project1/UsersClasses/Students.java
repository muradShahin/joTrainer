package com.murad.project1.UsersClasses;

public class Students extends User {
    private int numberOfLessons;

    public int getNumberOfLessons() {
        return numberOfLessons;
    }

    public void setNumberOfLessons(int numberOfLessons) {
        this.numberOfLessons = numberOfLessons;
    }

    public int getNumberOfLessonsLeft() {
        return numberOfLessonsLeft;
    }

    public void setNumberOfLessonsLeft(int numberOfLessonsLeft) {
        this.numberOfLessonsLeft = numberOfLessonsLeft;
    }

    private int numberOfLessonsLeft;

}
