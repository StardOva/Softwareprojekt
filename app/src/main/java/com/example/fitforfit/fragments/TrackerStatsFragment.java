package com.example.fitforfit.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.DayListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.singleton.Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TrackerStatsFragment extends Fragment {

    private FragmentMainBinding binding;



    public TrackerStatsFragment(){
        super(R.layout.fragment_tracker_stats);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_tracker_stats, binding.getRoot());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);

    }

    private void initRecyclerView(View view) {

    }


}
