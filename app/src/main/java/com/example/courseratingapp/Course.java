package com.example.courseratingapp;

import android.os.Parcel;
import android.os.Parcelable;

// To ease the burden I have opted not to make more than one object
// This is a lazy approach that definitely does not reflect my personality, in ANY way!..ehem...
public class Course implements Parcelable {

    private String name;

    // User
    private String username;

    // Teacher
    private String teacherName;
    private String teacherEmail;

    public Course(String name, String username, String teacherName, String teacherEmail) {
        this.name = name;
        this.username = username;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
    }

    protected Course(Parcel in) {
        name = in.readString();
        username = in.readString();
        teacherName = in.readString();
        teacherEmail = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(teacherName);
        dest.writeString(teacherEmail);
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }
}
