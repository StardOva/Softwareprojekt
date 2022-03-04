package com.example.fitforfit.utils;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.example.fitforfit.R;

public class ColorUtils {

    Context context;

    public ColorUtils(Context context) {
        this.context = context;
    }

    public int getColor(int color) {
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(this.context, color)));
    }
}
