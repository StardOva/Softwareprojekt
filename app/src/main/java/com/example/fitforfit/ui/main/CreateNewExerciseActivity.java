package com.example.fitforfit.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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

    public static final int MAX_EXERCISE_NAME_LENGTH = 25;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_exercise);

        binding = ActivityCreateNewExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());

        // zeige Tastatur an bei Start der Activity
        EditText editText = binding.textViewExerciseName;
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Textfeld auf maximale Länge begrenzen
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_EXERCISE_NAME_LENGTH)});

        // Button zum zurückgehen
        Button cancelButton = binding.btnCreateNewExerciseCancel;
        cancelButton.setOnClickListener(view -> finish());

        saveButton = binding.btnCreateNewExerciseConfirm;
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(view -> {
            Exercise exercise = new Exercise();
            exercise.name = editText.getText().toString();
            AsyncTask.execute(() -> {
                db.exerciseDao().insert(exercise);
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
                // TODO Hinweis an den Nutzer geben, irgendeine Art von Fehlermeldung,
                // TODO damit er weiß warum er den Button nicht drücken kann
                for (Exercise exercise : exerciseList) {
                    if (textFieldString.equalsIgnoreCase(exercise.name)) {
                        saveButton.setEnabled(false);
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
