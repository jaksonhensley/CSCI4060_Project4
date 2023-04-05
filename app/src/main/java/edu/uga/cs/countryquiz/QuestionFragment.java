package edu.uga.cs.countryquiz;

import android.os.Bundle;
import androidx.annotation.NonNull;
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
    private int position;

    View view;

    public QuestionFragment() {
        // required empty public constructor
    }

    public static QuestionFragment newInstance( int questionNumber ) {

        QuestionFragment fragment = new QuestionFragment();

        Log.d(TAG, "QuestionFragment.newInstance(): fragment: " + fragment);
        Bundle args = new Bundle();
        args.putInt( "question", questionNumber);
        fragment.setArguments( args );
        return fragment;

    }

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );
        if( getArguments() != null ) {
            country = getArguments().getString("country");
            position = getArguments().getInt("question");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the Layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState);

        TextView countryText = view.findViewById(R.id.QuestionCountryView);
        RadioButton optionOne = view.findViewById(R.id.OptionOne);
        RadioButton optionTwo = view.findViewById(R.id.OptionTwo);
        RadioButton optionThree = view.findViewById(R.id.OptionThree);

        Random rand = new Random();
        int A,B;
        A = rand.nextInt(6);
        B = rand.nextInt(6);

        countryText.setText(Integer.toString(position));
        optionOne.setText(continents[A]);
        optionTwo.setText(continents[B]);
        optionThree.setText(continents[rand.nextInt(6)]);

        if (position == 6) {
            countryText.setText("FINAL GRADING PAGE");
        }

    }

    public static int getNumberOfQuestions() { return 7;}

}