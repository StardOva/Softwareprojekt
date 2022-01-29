package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ScanErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_error);

        TextView errorText = findViewById(R.id.errorTextView);
        if(getIntent().hasExtra("error")){
            errorText.setText(getIntent().getStringExtra("error"));
        }

        Button againbtn = findViewById(R.id.againButton);
        againbtn.setOnClickListener(view -> {finish();});
    }
}