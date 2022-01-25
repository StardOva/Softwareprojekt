package com.example.fitforfit.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.fitforfit.adapter.MealListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.TrackerDayActivity;

import java.util.List;

public class TrackerMealsFragment extends Fragment {

    private FragmentMainBinding binding;
    private MealListAdapter mealListAdapter;



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
        loadMealList();

    }

    private void loadMealList() {
        Meal meal = new Meal();
        meal.meal_name = "Mittag";
        meal.time = "18:34";
        meal.day_id = 3;

        AppDatabase db = Database.getInstance(getActivity());
        db.mealDao().insert(meal);

        List<Meal> mealList = db.mealDao().getAllMeals();
        mealListAdapter.setContext(getActivity());
        mealListAdapter.setDayList(mealList);
    }

    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTrackerMeals);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            mealListAdapter = new MealListAdapter(getActivity());
            recyclerView.setAdapter(mealListAdapter);
        });
    }


}
