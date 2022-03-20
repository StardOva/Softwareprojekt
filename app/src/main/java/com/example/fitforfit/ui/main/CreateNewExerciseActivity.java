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
import com.example.fitforfit.databinding.ActivityCreateNewExerciseBinding;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Workout;
import com.example.fitforfit.singleton.Database;

import java.util.List;

public class CreateNewExerciseActivity extends AppCompatActivity {

    private ActivityCreateNewExerciseBinding binding;
    private Button saveButton;
    private AppDatabase db;
    private int exerciseId = 0;
    private int context;
    private Exercise exercise = null;

    public static final int MAX_EXERCISE_NAME_LENGTH = 25;
    public static final int CONTEXT_ADD = 0;
    public static final int CONTEXT_EDIT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_exercise);

        binding = ActivityCreateNewExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());

        context = CONTEXT_ADD;

        if (getIntent().getIntExtra("exerciseId", 0) != 0) {
            exerciseId = getIntent().getIntExtra("exerciseId", 0);
            // zeigt an, dass die Übung geändert und nicht hinzugefügt werden soll
            context = CONTEXT_EDIT;
            // Übung einladen
            exercise = db.exerciseDao().getById(exerciseId);
        }

        EditText editText = binding.textViewExerciseName;

        // falls die Übung bearbeitet wird, schreibe den bisherigen Namen rein
        if (context == CONTEXT_EDIT && exercise != null) {
            editText.setText(exercise.name);
        }

        // zeige Tastatur an bei Start der Activity
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Textfeld auf maximale Länge begrenzen
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_EXERCISE_NAME_LENGTH)});

        // Button zum zurückgehen
        Button cancelButton = binding.btnCreateNewExerciseCancel;
        cancelButton.setOnClickListener(view -> finish());

        saveButton = binding.btnCreateNewExerciseConfirm;
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(view -> {
            Exercise exercise = new Exercise();
            exercise.name = editText.getText().toString().trim();
            AsyncTask.execute(() -> {
                if (context == CONTEXT_ADD) {
                    db.exerciseDao().insert(exercise);
                } else {
                    // Name ändern und updaten
                    exercise.id = exerciseId;
                    db.exerciseDao().update(exercise);
                }
            });

            finish();
        });

        // alle bisherigen Übungen einladen
        List<Exercise> exerciseList = db.exerciseDao().getAll();

        // Speichern soll nur möglich sein, wenn der Exercise Name noch nicht existiert und nicht leer ist
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

                // prüfe, ob die Übung bereits existiert
                for (Exercise exercise : exerciseList) {
                    if (textFieldString.equalsIgnoreCase(exercise.name)) {
                        saveButton.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Diese Übung existiert bereits", Toast.LENGTH_SHORT).show();
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
}
