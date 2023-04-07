package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class GradePage extends AppCompatActivity {

    static int[] grades = QuestionActivity.getGrades();


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.grade_page );

        int gradeTotal = 0;

        TextView gradeDisplay = findViewById( R.id.gradeView);
        Button homeButton = findViewById( R.id.homeButton);

        for (int i = 0; i < grades.length; i++) {
            gradeTotal += grades[i];
        }

        gradeDisplay.setText(Integer.toString(gradeTotal));

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
