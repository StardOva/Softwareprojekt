package com.example.fitforfit.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.ActivityCreateNewWorkoutBinding;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class CreateNewWorkoutActivity extends AppCompatActivity {

    private ActivityCreateNewWorkoutBinding binding;
    private Button saveButton;
    private AppDatabase db;
    private List<Workout> workoutList = null;

    private int context;
    private int workoutId;
    private Workout workout;

    public static final int MAX_WORKOUT_NAME_LENGTH = 25;
    public static final int CONTEXT_ADD = 0;
    public static final int CONTEXT_EDIT = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_workout);

        binding = ActivityCreateNewWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());

        context = CONTEXT_ADD;

        if (getIntent().getIntExtra("workoutId", 0) != 0){
            workoutId = getIntent().getIntExtra("workoutId", 0);
            // zeigt an, dass das Workout geändert und nicht hinzugefügt werden soll
            context = CONTEXT_EDIT;
            // Übung einladen
            workout = db.workoutDao().getById(workoutId);
        }

        EditText editText = binding.textViewWorkoutName;

        if (context == CONTEXT_EDIT && workout != null){
            editText.setText(workout.name);
        }

        // zeige Tastatur an bei Start der Activity
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Textfeld auf maximale Länge begrenzen
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_WORKOUT_NAME_LENGTH)});

        // Button zum zurückgehen
        Button cancelButton = binding.btnCreateNewWorkoutCancel;
        cancelButton.setOnClickListener(view -> finish());

        // Button zum Speichern
        saveButton = binding.btnCreateNewWorkoutConfirm;
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(view -> {
            // neues Workout speichern
            Workout workout = new Workout();
            workout.name = editText.getText().toString();
            AsyncTask.execute(() -> {
                if (context == CONTEXT_ADD){
                    db.workoutDao().insert(workout);
                }
                else {
                    workout.id = workoutId;
                    db.workoutDao().update(workout);
                }
            });

            // zurück zur Main Activity
            finish();
        });

        loadWorkoutList();

        // Speichern soll nur möglich sein, wenn der Workoutname noch nicht existiert und nicht leer ist
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textFieldString = charSequence.toString().trim();

                // bei Länge 0
                if (textFieldString.length() == 0) {
                    saveButton.setEnabled(false);
                    return;
                }

                // prüfe, ob das Workout bereits existiert
                for (Workout workout : workoutList) {
                    if (textFieldString.equalsIgnoreCase(workout.name)) {
                        saveButton.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Dieses Workout existiert bereits", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // ansonsten den Button aktivieren
                saveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void loadWorkoutList() {
        AsyncTask.execute(() -> {
            // alle bisherigen Workouts einladen
            // TODO gefühlt laggt es mehr wenn die Workouts im separaten Thread geladen werden
            // TODO muss mal schauen wie das vorher war als es noch im UI Thread geladen wurde
            workoutList = db.workoutDao().getAll();
        });
    }
}
