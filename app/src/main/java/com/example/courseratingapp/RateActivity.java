package com.example.courseratingapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RateActivity extends AppCompatActivity {


    private Course course;
    private List<String> questions;
    private TextView rateTitle;

    private NestedScrollView nestedScrollView;
    private LinearLayout linearLayout;

    private Button btnSubmit;

    private final int AMOUNT_OF_RATING_OPTIONS = 5;

    private List<RadioButton> radioButtons;
    private Map<String, Integer> questionRating = new LinkedHashMap<>();
    private final String QUESTIONRATING_KEY = "questionRating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        retrieveDataFromIntent();
        initViews();
        questions = populateQuestions();
        initQuestionsInView(questions);
        initListeners();
    }

    private void retrieveDataFromIntent() {
        if(getIntent().getExtras().getParcelable("course") != null){
            course =  getIntent().getExtras().getParcelable("course");
        }
    }

    private void initViews() {
        rateTitle = findViewById(R.id.rateTitle);
        String title = "Rate the " + course.getName() + " course:";
        rateTitle.setText(title);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        linearLayout = findViewById(R.id.linearLayout);
    }

    private void initListeners() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                // title animation
                if(i1 != 0){
                    rateTitle.setAlpha(1 - ((float) i1/100));
                } else {
                    rateTitle.setAlpha(1);
                }
            }
        });
        for (int i = 0; i < radioButtons.size(); i++) {
            radioButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateQuestionRating();
                }
            });
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numSelected = 0;
                for (int i = 0; i < radioButtons.size(); i++) {
                    if(radioButtons.get(i).isChecked()){
                        numSelected++;
                    }
                }
                if(numSelected != questions.size()){
                    Toast.makeText(RateActivity.this, "You have to rate all questions", Toast.LENGTH_SHORT).show();
                } else {
                    sendEmail();
                }

            }
        });

    }

    // yet another database call
    private List<String> populateQuestions(){
        List<String> list = new ArrayList<String>();
        list.add("Are your happy overall with the course?");
        list.add("Do your classes in " + course.getName() + " inspire you?");
        list.add("Would you characterize your lessons as innovative?");
        list.add("Do you feel burdened by the amount of homework?");
        list.add("Has your knowledge of " + course.getName() + " increased since you started?");

        return list;
    }

    private void initQuestionsInView(final List<String> questions){
        radioButtons = new ArrayList<>();

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.colorLight)
                        , ContextCompat.getColor(this, R.color.colorAccent)
                }
        );


        for (int i = 0; i < questions.size(); i++) {
            TextView textView = new TextView(this);

            textView.setTextColor(ContextCompat.getColor(this, R.color.colorLight));
            textView.setTextSize(20);
            textView.setGravity(1);
            String questionText = (i + 1) + ". " + questions.get(i);
            textView.setText(questionText);

            // just some styling
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20,0,100);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setLayoutParams(params);
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < AMOUNT_OF_RATING_OPTIONS; j++) {
                // more styling
                RadioGroup.LayoutParams btnParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                btnParams.setMargins(10,0,10,0);

                RadioButton radioButton = new AppCompatRadioButton(this);
                radioButton.setLayoutParams(btnParams);
                radioButton.setTag(j+1);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton.setButtonTintList(colorStateList);
                }
                radioGroup.addView(radioButton);
                radioButtons.add(radioButton);
            }


            linearLayout.addView(textView);
            linearLayout.addView(radioGroup);
        }
        btnSubmit = new Button(this);
        btnSubmit.setText(getResources().getString(R.string.btnSubmit));
        linearLayout.addView(btnSubmit);
    }

    private void updateQuestionRating(){
        for (int i = 0; i < questions.size(); i++) {
            for (int j = i * 5; j < AMOUNT_OF_RATING_OPTIONS + i * 5; j++) {
                if(radioButtons.get(j).isChecked()){
                    questionRating.put(questions.get(i).toString(), Integer.parseInt(radioButtons.get(j).getTag().toString()));
                }
            }
        }
    }

    private void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "I've rated your " + course.getName() + " course!");
        intent.putExtra(Intent.EXTRA_TEXT, messageBody());
        intent.setData(Uri.parse("mailto:" + course.getTeacherEmail())); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }
    private String messageBody(){
        String messageBody = "Dear " + course.getTeacherName() + "\nI have rated your course.\nThese were the scores given:\n\n";
        for (int i = 0; i < questions.size(); i++) {
            messageBody += "Question #" + (i+1) + ":\n\"" + questions.get(i) + "\"" + "\n"
                    + "Rating: " + questionRating.get(questions.get(i)) + "/5\n\n";
        }
        messageBody += "The average score was: " + calculateAvgScore() + "\n\nBest regards\n" + course.getUsername();
        return messageBody;
    }

    private double calculateAvgScore(){
        double avgScore = 0;
        for (int i = 0; i < questionRating.size(); i++) {
            avgScore += questionRating.get(questions.get(i));
        }
        avgScore = avgScore / questions.size();

        return avgScore;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(QUESTIONRATING_KEY, (Serializable) questionRating);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionRating = (LinkedHashMap<String, Integer>)savedInstanceState.getSerializable(QUESTIONRATING_KEY);

        // set selected radiobuttons
        for (int i = 0; i < questionRating.size(); i++) {
            for (int j = i * 5; j < AMOUNT_OF_RATING_OPTIONS + i * 5; j++) {
                try{
                    if(questionRating.get(questions.get(i)) == Integer.parseInt(radioButtons.get(j).getTag().toString())){
                        radioButtons.get(j).setChecked(true);
                    }
                } catch (NullPointerException nPEX) {
                    nPEX.printStackTrace();
                }

            }
        }
    }
}
