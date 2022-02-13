package com.example.fitforfit.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitforfit.fragments.TrackerMealsFragment;
import com.example.fitforfit.fragments.TrackerStatsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TrackerDaySectionsPagerAdapter extends FragmentStateAdapter {

    public TrackerDaySectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TrackerStatsFragment();
            case 1:
                return new TrackerMealsFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}