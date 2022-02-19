package com.example.fitforfit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityMainBinding;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //TODO Layout Farben an DarkMode anpassen
        //immer Darkmode aktiviert
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        setTheme(R.style.Theme_FitforFit_NoActionBar);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db = Database.getInstance(getApplicationContext());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2           viewPager            = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TAB_TITLES[position])
        ).attach();
    }
}