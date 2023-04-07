package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.Random;

public class QuestionFragment extends Fragment {

    private static final String TAG = "Countries";

    // Array of continents to randomly select from.
    private static final String[] continents = {
            "North America",
            "South America",
            "Europe",
            "Asia",
            "Africa",
            "Oceania"
    };
    private String country;
    private String continent;
    private int position;

    View view;

    public QuestionFragment() {
        // required empty public constructor
    }

    //
    public static QuestionFragment newInstance(int questionNumber, String questionCountry, String questionContinent) {

        QuestionFragment fragment = new QuestionFragment();

        Log.d(TAG, "QuestionFragment.newInstance(): fragment: " + fragment);

        //Store the question country and continent into the argument bundle
        Bundle args = new Bundle();
        args.putString( "country", questionCountry );
        args.putString( "continent" , questionContinent );
        Log.d(TAG, "QuestionFragment.newInstance(): Country: " + questionCountry);
        Log.d(TAG, "QuestionFragment.newInstance(): Continent: " + questionContinent);
        fragment.setArguments( args );
        return fragment;

    }

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );

        //Retrieve arguments from bundle and set in local variables
        if( getArguments() != null ) {
            country = getArguments().getString("country");
            continent = getArguments().getString("continent");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the Layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }


    // Method to randomly select continents from a list and set them in random positions
    // of an array.
    public String[] setOptions() {
        String[] option = new String[3];

        Random rand = new Random();

        //Initialize strings to hold random selections
        String newContinent = "", usedContinent = "", correct = continent;

        //Obtain a random position for the correct answer
        int answerPosition = rand.nextInt(3);
        Log.d(TAG, "QuestionFragment.setOptions(): Correct position: " + answerPosition);

        int i = 0;

        while (i < 3) {

            //If i = correct answer slot, insert the correct answer passed from the parameter
            if (i == answerPosition) {
                option[i] = correct;
                i++;
            } else {
                //Obtain a random continent from the list of continents
                newContinent = continents[rand.nextInt(6)];
                Log.d(TAG, "QuestionFragment.setOptions(): Random continent " + i + ": " + newContinent);

                //  Check if the randomly chosen continent is neither the correct answer
                //  nor a random continent already chosen
                if (!(newContinent.equals(correct)) && !(newContinent.equals(usedContinent))) {
                    //place the new continent into the options array
                    option[i] = newContinent;
                    //Mark the new continent as used now.
                    usedContinent = newContinent;
                    i++;
                } //if
            } //else

        } //while


        return option;
    }



    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState);


        //Set views to each R.id
        TextView countryText = view.findViewById(R.id.QuestionCountryView);
        RadioButton optionOne = view.findViewById(R.id.OptionOne);
        RadioButton optionTwo = view.findViewById(R.id.OptionTwo);
        RadioButton optionThree = view.findViewById(R.id.OptionThree);

        //initialize choice array and randomize choices
        String[] choices = setOptions();

        //Set questions country
        countryText.setText(country);

        optionOne.setText(choices[0]);
        optionTwo.setText(choices[1]);
        optionThree.setText(choices[2]);

    }

    public static int getNumberOfQuestions() { return 7;}

}