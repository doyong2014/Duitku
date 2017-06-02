package com.mi1.duitku.Tab5.Register;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.badoualy.stepperindicator.StepperIndicator;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.HelpActivity;

public class RegisterChildActivity extends AppCompatActivity {

    private  StepperIndicator indicator;
    private  ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);

        pager = (ViewPager) findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(new EmptyPagerAdapter(getSupportFragmentManager()));

        indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);
        assert indicator != null;
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, true);

        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                pager.setCurrentItem(step, true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            RegisterChildActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void jumpToPage(View view) {
        if (indicator.getCurrentStep() != indicator.getStepCount()) {
            //indicator.setCurrentStep(indicator.getCurrentStep() + 1);
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        }
    }

    private class EmptyPagerAdapter extends FragmentPagerAdapter {

        public EmptyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new StepRegisterOneFragment();
            }
            else if (position == 2) {
                return new StepRegisterTwoFragment();
            }
            else if (position == 3) {
                return new StepRegisterThreeFragment();
            }
            else if (position == 1) {
                return new StepRegisterFourFragment();
            }
            else
                return StepRegisterFragment.newInstance(position + 1, position == getCount() - 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
}
