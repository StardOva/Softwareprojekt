package com.example.fitforfit.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Training;

import java.util.ArrayList;

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

    public ArrayList<Training> getTrainingList() {
        return this.trainingList;
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

        int   reps   = trainingList.get(position).reps;
        float weight = trainingList.get(position).weight;

        if (reps > 0) {
            holder.repCount.setText(String.valueOf(reps));
        }

        if (weight > 0) {
            holder.trainingWeight.setText(String.valueOf(weight));
        }

        // speichere die Anzahl direkt im Training Objekt
        holder.repCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.equals("")) {
                    trainingList.get(holder.getAbsoluteAdapterPosition()).reps = 0;
                } else {
                    trainingList.get(holder.getAbsoluteAdapterPosition()).reps = Integer.parseInt(text);
                }
            }
        });

        // speichere die Anzahl direkt im Training Objekt
        holder.trainingWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.equals("")) {
                    trainingList.get(holder.getAbsoluteAdapterPosition()).weight = 0;
                } else {
                    trainingList.get(holder.getAbsoluteAdapterPosition()).weight = Float.parseFloat(text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.trainingList.size();
    }

    public static class TrainingSetViewHolder extends RecyclerView.ViewHolder {
        TextView setTV;
        EditText repCount;
        EditText trainingWeight;

        public TrainingSetViewHolder(@NonNull View itemView) {
            super(itemView);
            setTV = itemView.findViewById(R.id.trainingSetNumberTextView);
            repCount = itemView.findViewById(R.id.repCount);
            trainingWeight = itemView.findViewById(R.id.trainingWeight);
        }
    }
}
