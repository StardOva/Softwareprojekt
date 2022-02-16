package com.example.fitforfit.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.CalendarViewHolder;
import com.example.fitforfit.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    LocalDate date;
    Context context;

    public CalendarAdapter(ArrayList<String> daysOfMonth, LocalDate date, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.date = date;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) ((parent.getHeight() * 0.16666666666));
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        holder.dayOfMonth.setText(daysOfMonth.get(position));


        //Log.d("TEST", daysOfMonth.get(position));
        //Log.d("TEST", date.toString().substring(0,7));
        try {
            AppDatabase db = Database.getInstance(this.context);
            String day = daysOfMonth.get(position);
            try {
                if(Integer.parseInt(daysOfMonth.get(position)) < 10){
                    day = "0" + day;
                }
                int id = db.dayDao().getIdByDate(date.toString().substring(0,8)+day);
                if(id != 0){

                    if (!LocalDate.now().toString().equals(date.toString().substring(0,8)+day)){
                        holder.dayOfMonth.setTextColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this.context, R.color.fit_green))));
                    }else{
                        holder.dayOfMonth.setTextColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(this.context, R.color.fit_orange_light))));
                    }
                }
            }catch (NumberFormatException n){}

        }catch(SQLiteException e) {}

    }

    @Override
    public int getItemCount() {

        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
