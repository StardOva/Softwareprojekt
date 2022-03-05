package com.example.fitforfit.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fitforfit.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

public class MonthHeaderBinder extends ViewContainer {
    public TextView textView;

    public MonthHeaderBinder(@NonNull View view) {
        super(view);

        textView = view.findViewById(R.id.headerTextView);
    }
}
