package com.murad.project1.UsersClasses;

public class Teacher extends User {
    private  String exp;
    private  String carType;
    private  String carImg;
    private  String office;

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
