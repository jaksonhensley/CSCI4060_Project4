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

public class QuestionFragment extends Fragment {

    private static final String TAG = "Countries";
    private static final String[] continents = {
            "North America",
            "South America",
            "Europe",
            "Asia",
            "Africa",
            "Oceania"
    };
    private String country;

    View view;

    public QuestionFragment() {
        // required empty method
    }

    public static QuestionFragment newInstance( int num) {

        QuestionFragment fragment = new QuestionFragment();

        Log.d(TAG, "QuestionFragment.newInstance(): fragment: " + fragment);

        Bundle args = new Bundle();
        args.putInt( "num", num);
        fragment.setArguments( args );

        return fragment;

    }

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );
        if( getArguments() != null ) {
            country = getArguments().getString("country");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState);

        TextView countryText = view.findViewById(R.id.QuestionCountryView);
        RadioButton optionOne = view.findViewById(R.id.OptionOne);
        RadioButton optionTwo = view.findViewById(R.id.OptionTwo);
        RadioButton optionThree = view.findViewById(R.id.OptionThree);

        countryText.setText("Filler Text");
        optionOne.setText(continents[2]);
        optionTwo.setText(continents[1]);
        optionThree.setText(continents[3]);

    }

    public static int getNumberOfQuestions() { return 5;}

}