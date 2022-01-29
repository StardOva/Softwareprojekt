package com.example.fitforfit.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.example.fitforfit.ui.main.AddNewMealActivity;
import com.example.fitforfit.ui.main.TrackerDayActivity;

import java.util.List;

public class TrackerMealsFragment extends Fragment {

    private FragmentMainBinding binding;
    private MealListAdapter mealListAdapter;// = new MealListAdapter(getActivity());
    int dayId;
    List<Meal> mealList;


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
        //onAttach(getActivity());
        initViews(view);


    }
    private void cleanProducts() {
        AppDatabase db = Database.getInstance(getActivity());
        db.productDao().cleanProducts();
    }

    private void initViews(View view) {
        cleanMeals();
        cleanProducts();
        initRecyclerView(view);
        //loadMealList();//TODO

        Button addMealButton = view.findViewById(R.id.addMealButton);
        addMealButton.setText("+ Meal");
        addMealButton.setOnClickListener(v -> {

            AppDatabase db = Database.getInstance(getActivity());
            Meal newmeal = new Meal();
            newmeal.meal_name = "NO_PLACEHOLDER";
            newmeal.time = "";
            newmeal.day_id = this.dayId;
            db.mealDao().insert(newmeal);

            Intent intent = new Intent(getActivity(), AddNewMealActivity.class);
            intent.putExtra("dayId", String.valueOf(dayId));
            getActivity().startActivity(intent);

        });
    }

    private void cleanMeals() {
        AppDatabase db = Database.getInstance(getActivity());
        db.mealDao().cleanMeals();
    }


    private void loadMealList() {
        TrackerDayActivity dayActivity = (TrackerDayActivity) getActivity();
        this.dayId = dayActivity.getCurrentDayId();

        AppDatabase db = Database.getInstance(getActivity());

        this.mealList = db.mealDao().getAllMealsOnDay(this.dayId);
        Log.d("DEBUG NULLPOINTER", "CHECKPOINT 1");
        mealListAdapter.setContext(getActivity());
        mealListAdapter.setMealList(this.mealList);

    }

    private void initRecyclerView(View view) {

            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTrackerMeals);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            mealListAdapter = new MealListAdapter(getActivity());

            recyclerView.setAdapter(mealListAdapter);

            //TODO not efficient - content of loadMeals()

            TrackerDayActivity dayActivity = (TrackerDayActivity) getActivity();
            this.dayId = dayActivity.getCurrentDayId();

            AppDatabase db = Database.getInstance(getActivity());

            this.mealList = db.mealDao().getAllMealsOnDay(this.dayId);
            Log.d("DEBUG NULLPOINTER", "CHECKPOINT 1");
            mealListAdapter.setContext(getActivity());
            mealListAdapter.setMealList(this.mealList);

    }

    @Override
    public void onResume() {
        super.onResume();
        cleanMeals();
        cleanProducts();
        // nach Anlegen eines neuen Workouts in der CreateNewWorkoutActivity
        // muss die Liste neu geladen werden, damit das neue Workout auch dargestellt wird
        loadMealList();
    }


}
