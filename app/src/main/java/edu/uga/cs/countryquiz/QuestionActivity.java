package edu.uga.cs.countryquiz;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_question );

        ViewPager2 pager = findViewById( R.id.viewPager );
        QuestionPagerAdapter questionAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), getLifecycle() );
        pager.setOrientation( ViewPager2.ORIENTATION_HORIZONTAL );
        pager.setAdapter( questionAdapter );

    }
    
    
}
