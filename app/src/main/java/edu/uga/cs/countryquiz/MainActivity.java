package edu.uga.cs.countryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of the CountryData class, which manages the database and provides
        // Methods to query it
        CountriesData countriesData = CountriesData.getInstance(this);

        //TODO: Write AsyncTask to get an instance of CountriesData.

        //These are the buttons from the layout
        Button viewPreviousQuizzesButton = findViewById(R.id.mainMenuViewPreviousQuizzesButton);
        Button helpButton = findViewById(R.id.mainMenuHelpButton);
        Button startQuizButton = findViewById(R.id.mainMenuStartQuizButton);

        //The next chunk of code is our button listeners.

        viewPreviousQuizzesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the "View Previous Quizzes" button
                Intent intent = new Intent(MainActivity.this, ViewPreviousQuizzes.class);
                startActivity(intent);
            } // onClick
        }); // viewPreviousQuizzesButton.setOnClickListener

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the "Help" button
                Intent intent = new Intent(MainActivity.this, HelpScreen.class);
                startActivity(intent);
            } // onClick
        }); // helpButton.setOnClickListener

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the "Start Quiz" button
                Intent intent = new Intent(MainActivity.this, NewQuiz.class);
                startActivity(intent);
            } // onClick
        }); // startQuizButton.setOnClickListener

    } // onCreate
} // MainActivity