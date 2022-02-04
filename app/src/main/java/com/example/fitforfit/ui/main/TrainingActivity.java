package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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

    // speichert die aktuelle Übungs ID
    private int currentExerciseId;

    // Liste der Übungen eines Workouts
    private List<Exercise> exerciseList;

    // speichert Sätze zur aktuell ausgeführten Übung
    private ArrayList<Training> currentTrainingList;

    // jede Übung braucht einen eigenen Adapter
    private HashMap<Integer, TrainingSetListAdapter> setListAdapterHashMap;

    // TODO Liste aller exerciseIds und der dazugehörigen Sätze
    private HashMap<Integer, ArrayList<Training>> exerciseTrainingList;

    private TrainingSetListAdapter currentSetListAdapter;

    RecyclerView recyclerView;

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
            // TODO die exercise ID ist momentan nicht eindeutig, da ein Workout mehrmals dieselbe Übung enthalten kann
            currentExerciseId = exerciseList.get(currentExercisePos).id;
            updateTextOfExerciseTextView();

            // initiales Training Objekt
            Training training = getNewTrainingInstance(0);
            currentTrainingList = new ArrayList<>();
            currentTrainingList.add(training);

            exerciseTrainingList = new HashMap<>();
            exerciseTrainingList.put(currentExerciseId, currentTrainingList);

            initViews();

            currentSetListAdapter = new TrainingSetListAdapter(getApplicationContext());
            currentSetListAdapter.setTrainingList(currentTrainingList);

            setListAdapterHashMap = new HashMap<>();
            setListAdapterHashMap.put(currentExerciseId, currentSetListAdapter);

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
        training.exerciseId = currentExerciseId;
        training.set = set;
        return training;
    }

    private void initRecyclerView() {
        recyclerView = binding.trainingSets;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(currentSetListAdapter);
    }

    private void initViews() {
        prevButton = binding.buttonTrainingPrevExercise;
        nextButton = binding.buttonTrainingNextExercise;
        prevButton.setEnabled(false);

        prevButton.setOnClickListener(view -> {
            performExerciseChange(--currentExercisePos);
        });
        nextButton.setOnClickListener(view -> performExerciseChange(++currentExercisePos));

        Button addSetButton = binding.buttonAddSet;
        addSetButton.setOnClickListener(view -> {
            int      currentSet = currentTrainingList.size();
            Training training   = getNewTrainingInstance(currentSet + 1);
            currentTrainingList.add(training);
            currentSetListAdapter.setTrainingList(currentTrainingList);
        });
    }

    /**
     * geht eine Übung vor oder zurück
     *
     * @param newExercisePos neue Position in der Liste der Übungen
     */
    private void performExerciseChange(int newExercisePos) {
        currentExercisePos = newExercisePos;

        // Liste der Sätze der aktuellen Übung (vor dem Wechsel der Übung) speichern
        // wenn es schon einen Eintrag in der Hashmap gibt, muss dieser erst entfernt und dann
        // neu hinzugefügt werden, da replace() erst ab Android N funktioniert
        if (exerciseTrainingList.containsKey(currentExerciseId)) {
            exerciseTrainingList.remove(currentExerciseId);
            exerciseTrainingList.put(currentExerciseId, currentTrainingList);
        } else {
            // wenn die Satzliste noch nicht enthalten ist, kann sie einfach hinzugefügt werden
            exerciseTrainingList.put(currentExerciseId, currentTrainingList);
        }

        // danach die neue exerciseId holen
        currentExerciseId = exerciseList.get(currentExercisePos).id;
        updateTextOfExerciseTextView();

        // wenn das Exercise Objekt schon erzeugt wurde, lade es neu ein, ansonsten erzeuge es
        if (exerciseTrainingList.containsKey(currentExerciseId)) {
            currentTrainingList = exerciseTrainingList.get(currentExerciseId);
        } else {
            currentTrainingList = new ArrayList<>();
            Training training = getNewTrainingInstance(0);
            currentTrainingList.add(training);
            exerciseTrainingList.put(currentExerciseId, currentTrainingList);
        }

        // Adapter zur Übung einladen oder erzeugen
        if (setListAdapterHashMap.containsKey(currentExerciseId)) {
            currentSetListAdapter = setListAdapterHashMap.get(currentExerciseId);
        } else {
            currentSetListAdapter = new TrainingSetListAdapter(getApplicationContext());
            setListAdapterHashMap.put(currentExerciseId, currentSetListAdapter);
        }

        currentSetListAdapter.setTrainingList(currentTrainingList);
        recyclerView.setAdapter(currentSetListAdapter);

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
