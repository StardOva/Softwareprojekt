package com.example.fitforfit.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.fitforfit.utils.DateUtils;

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

    // die aktuelle ID der Trainings Session
    private int trainingId;

    // Liste der Übungen eines Workouts
    private List<Exercise> exerciseList;

    // speichert Sätze zur aktuell ausgeführten Übung
    private ArrayList<Training> currentTrainingList;

    // jede Übung braucht einen eigenen Adapter
    private HashMap<Integer, TrainingSetListAdapter> setListAdapterHashMap;

    // HashMap aller exerciseIds und der dazugehörigen Sätze
    private HashMap<Integer, ArrayList<Training>> exerciseTrainingList;

    private TrainingSetListAdapter currentSetListAdapter;

    RecyclerView recyclerView;

    private Button prevButton;
    private Button nextButton;

    private AlertDialog.Builder finishAlertDialog;

    private String dateString;

    boolean editSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        binding = ActivityTrainingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());
        workoutId = getIntent().getIntExtra("workoutId", 0);

        // wenn die trainingId mitgeliefert wurde, dann nimm diese (wenn eine Session später nochmal bearbeitet wird)
        trainingId = getIntent().getIntExtra("trainingId", 0);
        // wird die Session gerade bearbeitet oder ist es eine neue
        editSession = true;

        if (trainingId == 0) {
            trainingId = db.trainingDao().getLastId() + 1;
            editSession = false;
        }

        // wenn Session bearbeitet wird, nimm dessen Datum
        dateString = getIntent().getStringExtra("createdAt");
        if (dateString == null) {
            // aktuelles Datum DD-MM-YYYY
            dateString = DateUtils.getGermanDateString();
        }

        if (workoutId > 0) {
            exerciseList = db.workoutDao().getRelatedExercises(workoutId);
            Workout workout = db.workoutDao().getById(workoutId);

            TextView workoutNameTextView = binding.trainingWorkoutName;
            workoutNameTextView.setText(workout.name);

            currentExercisePos = 0;
            currentExerciseId = exerciseList.get(currentExercisePos).id;
            updateTextOfExerciseTextView();

            currentTrainingList = new ArrayList<>();
            exerciseTrainingList = new HashMap<>();

            // initiales Training Objekt, falls die Session gerade nicht bearbeitet wird
            if (!editSession) {
                Training training = getNewTrainingInstance(0);
                currentTrainingList.add(training);
                exerciseTrainingList.put(currentExerciseId, currentTrainingList);
            } else {
                // ansonsten gehe durch die Liste der Übungen
                boolean firstRun = true;
                for (Exercise exercise : exerciseList) {
                    // und lade alle zugehörigen Sätze ein
                    List<Training> dbTrainingList = db.trainingDao().getAllSetsByWorkoutAndTrainingAndExerciseId(workoutId, trainingId, exercise.id);

                    // beim ersten Durchlauf gleich noch die Variable currentTrainingList setzen
                    if (firstRun) {
                        currentTrainingList = (ArrayList<Training>) dbTrainingList;
                        firstRun = false;
                    }

                    exerciseTrainingList.put(exercise.id, (ArrayList<Training>) dbTrainingList);
                }
            }

            initViews();

            currentSetListAdapter = new TrainingSetListAdapter(getApplicationContext());
            currentSetListAdapter.setTrainingList(currentTrainingList);

            setListAdapterHashMap = new HashMap<>();
            setListAdapterHashMap.put(currentExerciseId, currentSetListAdapter);

            initRecyclerView();
        }

        finishAlertDialog = new AlertDialog.Builder(this)
                .setTitle("Bestätigen")
                .setMessage("Das Training beenden und alle Werte speichern?")
                .setPositiveButton("JA", (dialogInterface, i) -> finish())
                .setNegativeButton("NEIN", null);

    }

    private void updateTextOfExerciseTextView() {
        TextView exerciseNameTV = binding.trainingExerciseName;

        String text = exerciseList.get(currentExercisePos).name + " (" + (currentExercisePos + 1) + "/" + exerciseList.size() + ")";
        exerciseNameTV.setText(text);
    }

    private Training getNewTrainingInstance(int set) {
        Training training = new Training();
        training.id = trainingId;
        training.workoutId = workoutId;
        training.exerciseId = currentExerciseId;
        training.set = set;
        training.createdAt = dateString;
        training.reps = 0;
        training.weight = 0;
        return training;
    }

    private void initRecyclerView() {
        recyclerView = binding.trainingSets;
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(currentSetListAdapter);
    }

    private void initViews() {
        prevButton = binding.buttonTrainingPrevExercise;
        nextButton = binding.buttonTrainingNextExercise;
        prevButton.setEnabled(false);

        prevButton.setOnClickListener(view -> performExerciseChange(--currentExercisePos));

        if (exerciseList.size() > 1) {
            nextButton.setOnClickListener(view -> performExerciseChange(++currentExercisePos));
        } else {
            nextButton.setText(R.string.finished);
            nextButton.setOnClickListener(view -> finishAlertDialog.show());
        }

        Button addSetButton = binding.buttonAddSet;
        addSetButton.setOnClickListener(view -> {
            currentTrainingList = currentSetListAdapter.getTrainingList();
            Training training = getNewTrainingInstance(currentTrainingList.size());
            currentSetListAdapter.addTrainingToList(training);
        });
    }

    /**
     * geht eine Übung vor oder zurück
     *
     * @param newExercisePos neue Position in der Liste der Übungen
     */
    private void performExerciseChange(int newExercisePos) {
        currentExercisePos = newExercisePos;

        // aus dem Adapter die aktuelle Trainingsliste mit jedem Satz und Gewicht + Wiederholungen holen
        currentTrainingList = currentSetListAdapter.getTrainingList();

        // Liste der Sätze der aktuellen Übung (vor dem Wechsel der Übung) speichern
        exerciseTrainingList.put(currentExerciseId, currentTrainingList);

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

        if (currentExercisePos + 1 == exerciseList.size()) {
            nextButton.setText(R.string.finished);
            nextButton.setOnClickListener(view -> finishAlertDialog.show());
        } else {
            nextButton.setText(R.string.next);
            nextButton.setOnClickListener(view -> performExerciseChange(++currentExercisePos));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // vor dem Speichern noch die aktuelle Liste laden
        currentTrainingList = currentSetListAdapter.getTrainingList();

        // Liste der Sätze der aktuellen Übung (vor dem Wechsel der Übung) speichern
        exerciseTrainingList.put(currentExerciseId, currentTrainingList);

        // wenn das Workout bearbeitet wird, vor dem Speichern noch alle bisherigen Einträge aus der DB löschen
        if (editSession) {
            db.trainingDao().deleteByTrainingId(trainingId);
        }

        // alle Werte speichern
        for (ArrayList<Training> trainingList : exerciseTrainingList.values()) {
            for (Training training : trainingList) {
                db.trainingDao().insert(training);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAlertDialog.show();
    }
}
