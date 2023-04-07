package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private String country; //Country for current question
    private String continent; //Continent for current country
    private int position; //Current fragment slide number
    int correctIndex = 0; //Index for the correct answer

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
        args.putInt("position", questionNumber );
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
            position = getArguments().getInt("position");
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
        String newContinent = "", usedContinent = "";

        //Obtain a random position for the correct answer
        correctIndex = rand.nextInt(3);
        Log.d(TAG, "QuestionFragment.setOptions(): Correct answer index: " + correctIndex);

        int i = 0;

        while (i < 3) {

            //If i = correct answer slot, insert the correct answer passed from the parameter
            if (i == correctIndex) {
                option[i] = continent;
                i++;
            } else {
                //Obtain a random continent from the list of continents
                newContinent = continents[rand.nextInt(6)];
                Log.d(TAG, "QuestionFragment.setOptions(): Random continent in position" + i + ": " + newContinent);

                //  Check if the randomly chosen continent is neither the correct answer
                //  nor a random continent already chosen
                if (!(newContinent.equals(continent)) && !(newContinent.equals(usedContinent))) {
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

        //Set views to each id
        TextView countryText = view.findViewById(R.id.QuestionCountryView);
        RadioGroup options = view.findViewById(R.id.radioGroup);
        RadioButton optionOne = view.findViewById(R.id.OptionOne);
        RadioButton optionTwo = view.findViewById(R.id.OptionTwo);
        RadioButton optionThree = view.findViewById(R.id.OptionThree);

        //initialize choice array and randomize choices
        String[] choices = setOptions();

        //Set questions country
        countryText.setText(country);
        optionOne.setText("A " + choices[0]);
        optionTwo.setText("B " + choices[1]);
        optionThree.setText("C " + choices[2]);


        optionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correctIndex == 0) {
                    QuestionActivity.setGrade(position,1);
                } else {
                    QuestionActivity.setGrade(position,0);
                }
            }
        });
        optionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correctIndex == 1) {
                    QuestionActivity.setGrade(position,1);
                } else {
                    QuestionActivity.setGrade(position,0);
                }
            }
        });
        optionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correctIndex == 2) {
                    QuestionActivity.setGrade(position,1);
                } else {
                    QuestionActivity.setGrade(position,0);
                }
            }
        });

    }


}