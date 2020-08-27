package com.murad.project1.contractClasses;


import com.murad.project1.UsersClasses.Students;
import com.murad.project1.UsersClasses.User;

public class Requests extends Students {
    private String reqId;
    private String studentEmail;
    private String teacherEmail;
    private String suggestedTime;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentName) {
        this.studentEmail = studentName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherName) {
        this.teacherEmail= teacherName;
    }

    public String getSuggestedTime() {
        return suggestedTime;
    }

    public void setSuggestedTime(String suggestedTime) {
        this.suggestedTime = suggestedTime;
    }
}
