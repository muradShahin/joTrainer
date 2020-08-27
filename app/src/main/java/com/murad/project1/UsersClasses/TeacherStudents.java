package com.murad.project1.UsersClasses;

//this class is made to be used in teacher profile
//to get all the students whose enrolled with the student

public class TeacherStudents {
    private int studentId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    private String email;
    private String fname;
    private String lname;
    private String profileImg;
    private String age;
    private String lat;
    private String lng;
    private String phone;
    private String city;
    private String Date;
    private String lessons;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    private int rate;
    private int lessons_left;

    public int getLessons_left() {
        return lessons_left;
    }

    public void setLessons_left(int lessons_left) {
        this.lessons_left = lessons_left;
    }

    private String TeacherName;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
