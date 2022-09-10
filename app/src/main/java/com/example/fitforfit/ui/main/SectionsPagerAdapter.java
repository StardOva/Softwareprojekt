package com.example.fitforfit.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitforfit.fragments.TrackerMainFragment;
import com.example.fitforfit.fragments.WorkoutMainFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrackerMainFragment();
            case 1:
                return new WorkoutMainFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}