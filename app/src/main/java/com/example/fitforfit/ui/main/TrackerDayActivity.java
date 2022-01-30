package com.example.fitforfit.ui.main;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityDayBinding;
import com.example.fitforfit.databinding.ActivityMainBinding;
import com.example.fitforfit.singleton.Database;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TrackerDayActivity extends AppCompatActivity {

    private ActivityDayBinding binding;

    int dayId;



    Button returnButton;



    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.day_tab_text_1, R.string.day_tab_text_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_day);

        binding = ActivityDayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TrackerDaySectionsPagerAdapter daysectionsPagerAdapter = new TrackerDaySectionsPagerAdapter(this);
        ViewPager2           viewPager            = binding.dayViewPager;
        viewPager.setAdapter(daysectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(TAB_TITLES[position])
        ).attach();

        initViews();
    }

    private void initViews() {

        String date = getIntent().getStringExtra("date");

        AppDatabase db = Database.getInstance(this);
        dayId = db.dayDao().getIdByDate(date);

        //dateText = findViewById(R.id.date_text);
        //dateText.setText((date + " ID:" + String.valueOf(dayId)).toString());

    }

    public int getCurrentDayId(){
        return this.dayId;
    }

    private void onReturnButtonClick() {
        finish();
    }
}