package com.example.fitforfit.ui.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_workout);

        binding = ActivityCreateNewWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Database.getInstance(getApplicationContext());

        // zeige Tastatur an bei Start der Activity
        EditText editText = binding.textViewWorkoutName;
        editText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
                db.workoutDao().insert(workout);
            });

            // zurück zur Main Activity
            finish();
        });

        // alle bisherigen Workouts einladen
        List<Workout> workoutList = db.workoutDao().getAll();

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
                // TODO Hinweis an den Nutzer geben, irgendeine Art von Fehlermeldung,
                // TODO damit er weiß warum er den Button nicht drücken kann
                for (Workout workout : workoutList) {
                    if (textFieldString.equalsIgnoreCase(workout.name)) {
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
