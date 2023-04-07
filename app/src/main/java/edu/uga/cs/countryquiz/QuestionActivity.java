package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Random;




public class QuestionActivity extends AppCompatActivity {

    static int[] grades = {0,0,0,0,0,0};

    public static int[] getGrades() {
        return grades;
    }

    public static void setGrade( int position, int grade ) {
        grades[position] = grade;
        return;
    }


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_question );

        //The following chunk connects to the database and stores some variables to be used in the questions
        CountriesData countriesData = CountriesData.getInstance(this);
        SQLiteDatabase db = countriesData.getReadableDatabase();

        Country[] allCountries = countriesData.getCountries(db);
        Country[] quizCountries = new Country[6];
        String[] allContinents = countriesData.getContinents(db);
        Random random = new Random();
        int[] selectedIndices = new int[6];
        int numSelected = 0;

        // We want to use a while loop instead of a for loop so that if the index is already in the
        // Array it tries again instead of skipping that index
        while (numSelected < 6) {
            int randomIndex = random.nextInt(allCountries.length);
            if (!contains(selectedIndices, randomIndex)) {
                quizCountries[numSelected] = allCountries[randomIndex];
                selectedIndices[numSelected] = randomIndex;
                numSelected++;
            } // if
        } // while

        //Finds and sets views
        ViewPager2 pager = findViewById( R.id.viewPager );
        Button submitButton = findViewById( R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for the "Submit Quiz" button
                Intent intent = new Intent(QuestionActivity.this, GradePage.class);
                startActivity(intent);
            } // onClick
        });



        QuestionPagerAdapter questionAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), getLifecycle(), quizCountries);
        pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
        pager.setAdapter( questionAdapter );
        pager.setOffscreenPageLimit(4);



        /* USAGE:
         *
         * Country[] quizCountries contains 6 Country objects for the quiz.
         *
         * quizCountries[i].getCountry() returns the String representation of the Country's name
         * for the question
         *
         * quizCountries[i].getContinent() returns the String representation of the Country's
         * continent for the correct answer choice
         *
         * String[] allContinents contains all of the continent values stored in the database to use
         * as answer choices. This is functionally the same as hardcoding these values in an array,
         * but would be beneficial if the dataset omitted all countries in a particular continent.
         *
         */

    }

    // Helper method to check if an array contains a value
    private static boolean contains(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    
}
