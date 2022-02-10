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

    private int getBase(int colorId) {
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(this.context, colorId)));
    }

    public int getFitRed() {
        return getBase(R.color.fit_red);
    }

    public int getFitOrangeDark() {
        return getBase(R.color.fit_orange_dark);
    }

    public int getFitOrangeLight() {
        return getBase(R.color.fit_orange_light);
    }

    public int getFitBlueDark() {
        return getBase(R.color.fit_blue_dark);
    }

    public int getFitBlueLight() {
        return getBase(R.color.fit_blue_light);
    }

    public int getFitBrown() {
        return getBase(R.color.fit_brown);
    }

    public int getFitGreen() {
        return getBase(R.color.fit_green);
    }

    public int getFitGrey() {
        return getBase(R.color.fit_grey);
    }
}
