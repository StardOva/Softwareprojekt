package com.example.fitforfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitforfit.R;
import com.example.fitforfit.databinding.FragmentMainBinding;

public class TrackerMealsFragment extends Fragment {

    private FragmentMainBinding binding;



    public TrackerMealsFragment(){
        super(R.layout.fragment_tracker_meals);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_tracker_meals, binding.getRoot());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);

    }

    private void initRecyclerView(View view) {

    }


}
