package com.majkl.kitebooking;

public class Student {

    int id;
    String name, course, joiningDate;
    double dob;

    public Student(int id, String name, String course, String joiningDate, double dob) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.joiningDate = joiningDate;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public double getDob() {
        return dob;
    }
}

