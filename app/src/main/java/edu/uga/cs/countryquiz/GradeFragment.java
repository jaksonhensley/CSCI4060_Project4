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

public class GradeFragment extends Fragment {

    private int position;

    public GradeFragment() {
        // required empty public constructor
    }

    public static GradeFragment newInstance( int questionNumber ) {

        GradeFragment fragment = new GradeFragment();

        Bundle args = new Bundle();
        args.putInt( "question", questionNumber);
        fragment.setArguments( args );
        return fragment;

    }

    @Override
    public void onCreate( Bundle savedInstance ) {
        super.onCreate( savedInstance );
        if( getArguments() != null ) {
            position = getArguments().getInt("question");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the Layout for this fragment
        return inflater.inflate(R.layout.fragment_grade, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState);

        TextView finalScore = view.findViewById(R.id.finalScore);


        finalScore.setText(Integer.toString(position));

    }

    public static int getNumber() { return 1;}
}