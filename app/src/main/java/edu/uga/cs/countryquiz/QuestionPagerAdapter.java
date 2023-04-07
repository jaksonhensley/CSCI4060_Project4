package edu.uga.cs.countryquiz;

import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class QuestionPagerAdapter extends FragmentStateAdapter {

    Country[] quizCountries = new Country[6];
    String[] quizCountryStrings = new String[6];
    String[] quizContinentStrings = new String[6];
    ViewPager2 page;
    Button submitButton;



    public QuestionPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, Country[] countries, ViewPager2 pager, Button submit) {
        super( fragmentManager, lifecycle );

        page = pager;
        submitButton = submit;
        //retrieve Country array and transfer it's data to a String array
        quizCountries = countries;
        for(int i = 0; i < quizCountries.length; i++) {
            quizCountryStrings[i] = quizCountries[i].getCountry();
            quizContinentStrings[i] = quizCountries[i].getContinent();
        }

    }

    @Override
    public Fragment createFragment(int position){

        return QuestionFragment.newInstance(position, quizCountryStrings[position], quizContinentStrings[position], submitButton);
    }



    @Override
    public int getItemCount() {
        return 6;
    }


}

