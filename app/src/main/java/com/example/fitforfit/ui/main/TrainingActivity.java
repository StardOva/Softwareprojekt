package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.TrainingSetListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityTrainingBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    private ActivityTrainingBinding binding;
    private AppDatabase db;
    private int workoutId;

    // speichert die aktuelle Position in der Liste der Übungen
    private int currentExercisePos;

    // Liste der Übungen eines Workouts
    private List<Exercise> exerciseList;

    // speichert Sätze zur aktuell ausgeführten Übung
    private ArrayList<Training> currentTrainingList;

    // TODO Liste aller exerciseIds und der dazugehörigen Sätze
    private HashMap<Integer, ArrayList<Training>> exerciseTrainingList;

    private TrainingSetListAdapter trainingSetListAdapter;

    private Button prevButton;
    private Button nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        binding = ActivityTrainingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());
        workoutId = getIntent().getIntExtra("workoutId", 0);

        if (workoutId > 0) {
            exerciseList = db.workoutDao().getRelatedExercises(workoutId);
            Workout workout = db.workoutDao().getById(workoutId);

            TextView workoutNameTextView = binding.trainingWorkoutName;
            workoutNameTextView.setText(workout.name);

            currentExercisePos = 0;
            updateTextOfExerciseTextView();

            // initiales Training Objekt
            Training training = getNewTrainingInstance(0);
            currentTrainingList = new ArrayList<>();
            currentTrainingList.add(training);

            exerciseTrainingList = new HashMap<>();
            exerciseTrainingList.put(exerciseList.get(currentExercisePos).id, currentTrainingList);

            initViews();

            trainingSetListAdapter = new TrainingSetListAdapter(getApplicationContext());
            trainingSetListAdapter.setTrainingList(currentTrainingList);
            initRecyclerView();

        }
    }

    private void updateTextOfExerciseTextView() {
        TextView exerciseNameTV = binding.trainingExerciseName;

        String text = exerciseList.get(currentExercisePos).name + " (" + (currentExercisePos + 1) + "/" + exerciseList.size() + ")";
        exerciseNameTV.setText(text);
    }

    private Training getNewTrainingInstance(int set) {
        Training training = new Training();
        training.workoutId = workoutId;
        training.exerciseId = exerciseList.get(currentExercisePos).id;
        training.set = set;
        return training;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.trainingSets;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(trainingSetListAdapter);
    }

    private void initViews() {
        prevButton = binding.buttonTrainingPrevExercise;
        nextButton = binding.buttonTrainingNextExercise;
        prevButton.setEnabled(false);

        prevButton.setOnClickListener(view -> performExerciseChange(--currentExercisePos));
        nextButton.setOnClickListener(view -> performExerciseChange(++currentExercisePos));

        Button addSetButton = binding.buttonAddSet;
        addSetButton.setOnClickListener(view -> {
            int      currentSet = currentTrainingList.size();
            Training training   = getNewTrainingInstance(currentSet + 1);
            currentTrainingList.add(training);
            trainingSetListAdapter.setTrainingList(currentTrainingList);
        });
    }

    /**
     * geht eine Übung vor oder zurück
     *
     * @param newExercisePos neue Position in der Liste der Übungen
     */
    private void performExerciseChange(int newExercisePos) {
        currentExercisePos = newExercisePos;
        updateTextOfExerciseTextView();

        // wenn das Exercise Objekt schon erzeugt wurde, lade es neu ein, ansonsten erzeuge es
        if (exerciseTrainingList.containsKey(exerciseList.get(currentExercisePos).id)){
            currentTrainingList = exerciseTrainingList.get(exerciseList.get(currentExercisePos).id);
        }
        else {
            currentTrainingList = new ArrayList<>();
            Training training = getNewTrainingInstance(0);
            currentTrainingList.add(training);
            exerciseTrainingList.put(exerciseList.get(currentExercisePos).id, currentTrainingList);
        }

        trainingSetListAdapter.setTrainingList(currentTrainingList);

        prevButton.setEnabled(currentExercisePos != 0);

        //noinspection RedundantIfStatement
        if (currentExercisePos + 1 == exerciseList.size()) {
            // TODO hier Funktion bauen die das Training abschließt und die Activity beendet
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
    }
}
