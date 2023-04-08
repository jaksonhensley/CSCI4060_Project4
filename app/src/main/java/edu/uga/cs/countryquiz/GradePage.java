package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GradePage extends AppCompatActivity {

    private static final String TAG = "Grade Page";

    int[] grades = QuestionActivity.getGrades();
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.grade_page );

        int gradeTotal = 0;

        //Finds views
        TextView text = findViewById( R.id.pageText);
        TextView gradeDisplay = findViewById( R.id.gradeView);
        Button homeButton = findViewById( R.id.homeButton);

        //Counts up total grade from individual question grades
        for (int i = 0; i < grades.length; i++) {
            gradeTotal += grades[i];
        }

        //Displays grade
        text.setText("Quiz Grade");
        gradeDisplay.setText(Integer.toString((gradeTotal/grades.length) * 100));

        //Store date and grade into database
        CountriesData countriesData = CountriesData.getInstance(this);
        SQLiteDatabase db = countriesData.getWritableDatabase();


        //Sets the
        countriesData.putRecord(date, (float)((gradeTotal/grades.length)*100));

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the "Submit Quiz" button
                Intent intent = new Intent(GradePage.this, MainActivity.class);
                startActivity(intent);
            } // onClick
        });




    }


}

