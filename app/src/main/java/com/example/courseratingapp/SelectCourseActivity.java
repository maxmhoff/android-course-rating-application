package com.example.courseratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectCourseActivity extends AppCompatActivity {

    private String username;
    private Spinner spnCourse;
    private Button btnChooseCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
        retrieveDataFromIntent();
        initViews();
        initListeners();
        populateSpinner();
    }
    private void retrieveDataFromIntent(){
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    private void initViews(){
        spnCourse = findViewById(R.id.spnCourse);
        btnChooseCourse = findViewById(R.id.btnChooseCourse);
    }

    private void initListeners(){
        btnChooseCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRating();
            }
        });
    }

    // this will be a call to the database in the future...
    private void populateSpinner(){
        List<String> courses = new ArrayList<>(Arrays.asList("Python", "Angular", "Android", "AI"));
        ArrayAdapter<String> spnArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner, courses);
        spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCourse.setAdapter(spnArrayAdapter);
    }

    // and so will this... a call to the database to find a teacher
    private Teacher findTeacher(String courseName) {
        switch(courseName){
            case "Python":
                return new Teacher("Teacher1", "teacher1@teacher.com");

            case "Angular":
                return new Teacher("Teacher2", "teacher2@teacher.com");

            case "Android":
                return new Teacher("Teacher3", "teacher3@teacher.com");

            case "AI":
                return new Teacher("Teacher4", "teacher4@teacher.com");
        }
        return new Teacher("","");
    }

    // I'm not completely sure, that I would normally initialize the parcelable here (in this activity),
    // but due to the nature of the assignment I will.
    private void openActivityRating(){
        Intent intent = new Intent(this, RateActivity.class);
        String courseName = spnCourse.getSelectedItem().toString();
        Teacher teacher = findTeacher(courseName);
        intent.putExtra("course", new Course(courseName, username, teacher.getName(), teacher.getEmail()));

        startActivity(intent);
    }
}
