package com.example.fitforfit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitforfit.R;
import com.example.fitforfit.databinding.FragmentMainBinding;

public class WorkoutMainFragment extends Fragment {

    private FragmentMainBinding binding;

    public WorkoutMainFragment(){
        super(R.layout.fragment_workout);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout, binding.getRoot());
    }

}
