package com.example.fitforfit.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Training;

import java.util.ArrayList;
import java.util.List;

public class TrainingSetListAdapter extends RecyclerView.Adapter<TrainingSetListAdapter.TrainingSetViewHolder> {

    private Context context;
    private ArrayList<Training> trainingList = new ArrayList<>();

    public TrainingSetListAdapter(Context context) {
        this.context = context;
    }

    public void setTrainingList(ArrayList<Training> trainingList) {
        this.trainingList = trainingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrainingSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_training_sets, parent, false);

        return new TrainingSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingSetViewHolder holder, int position) {
        String text = (position + 1) + ". Satz";
        holder.setTV.setText(text);
    }

    @Override
    public int getItemCount() {
        return this.trainingList.size();
    }

    public static class TrainingSetViewHolder extends RecyclerView.ViewHolder {

        // TODO Layout designen
        TextView setTV;

        public TrainingSetViewHolder(@NonNull View itemView) {
            super(itemView);
            setTV = itemView.findViewById(R.id.trainingSetNumberTextView);
        }
    }
}
