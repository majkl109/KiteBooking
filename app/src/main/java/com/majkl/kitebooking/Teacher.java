package com.majkl.kitebooking;

public class Teacher {

    int id;
    String name;
    int yearsOfExperience;
    boolean certificate;
    String joiningDate;

    public Teacher(int id, String name, int yearsOfExperience, boolean certificate, String joiningDate) {
        this.id = id;
        this.name = name;
        this.yearsOfExperience = yearsOfExperience;
        this.certificate = certificate;
        this.joiningDate = joiningDate;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public boolean isCertificate() {
        return certificate;
    }

    public String getJoiningDate() {
        return joiningDate;
    }
}
