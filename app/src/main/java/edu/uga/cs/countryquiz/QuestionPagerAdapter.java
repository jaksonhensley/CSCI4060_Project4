package edu.uga.cs.countryquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Random;

public class QuestionPagerAdapter extends FragmentStateAdapter {


    public QuestionPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle ) {
        super( fragmentManager, lifecycle );
    }

    @Override
    public Fragment createFragment(int position){

        Fragment fragment = new Fragment();

        if( position <= 5 ) {
            fragment = QuestionFragment.newInstance(position);
        } else if ( position == 6 ) {
            fragment = GradeFragment.newInstance(position);
        }

        return fragment;
    }



    @Override
    public int getItemCount() {
        return QuestionFragment.getNumberOfQuestions();
    }


}

