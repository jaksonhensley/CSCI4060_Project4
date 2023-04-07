package edu.uga.cs.countryquiz;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewPreviousQuizzes extends AppCompatActivity {

    private QuizRecord[] quizRecords;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_quizzes);

        CountriesData countriesData = CountriesData.getInstance(this);
        SQLiteDatabase db = countriesData.getReadableDatabase();

        quizRecords = countriesData.getAllQuizRecords(db);


        ListView listViewResults = findViewById(R.id.viewPreviousQuizzesListView);
        if (quizRecords != null) {
            ResultsAdapter adapter = new ResultsAdapter(this, quizRecords);
            listViewResults.setAdapter(adapter);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
}
