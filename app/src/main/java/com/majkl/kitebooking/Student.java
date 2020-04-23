package com.majkl.kitebooking;

public class Student {

    int id;
    String name;
    String course;
    String joiningDate;
    int dob;

    public Student(int id, String name, String course, String joiningDate, int dob) {
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

    public int getDob() {
        return dob;
    }
}

