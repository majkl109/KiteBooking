package com.majkl.kitebooking;

public class Student {

    int id;
    String name;
    String course;
    String joiningDate;
    int dob;
    byte[] image;

    public Student(int id, String name, String course, String joiningDate, int dob, byte[] image) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.joiningDate = joiningDate;
        this.dob = dob;
        this.image = image;
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

    public byte[] getImage() {
        return image; }
}

