package com.murad.project1.lessonsClasses;

import com.murad.project1.UsersClasses.Students;
import com.murad.project1.UsersClasses.User;

public class Lessons extends Students {
    private long session_id;
    private String student_email;
    private String teacher_email;
    private String date;
    private String status;
    private boolean todayLesson;
    private String approved;

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public boolean isTodayLesson() {
            return todayLesson;
        }

        public void setTodayLesson(boolean todayLesson) {
            this.todayLesson = todayLesson;
        }



        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public long getSession_id() {
        return session_id;
    }

    public void setSession_id(long id) {
        this.session_id = id;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
