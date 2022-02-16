package com.example.fitforfit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;

import java.util.ArrayList;

public class TrainingSetListAdapter extends RecyclerView.Adapter<TrainingSetListAdapter.TrainingSetViewHolder> {

    private final Context context;
    private ArrayList<Training> trainingList = new ArrayList<>();

    public TrainingSetListAdapter(Context context) {
        this.context = context;
    }

    public void setTrainingList(ArrayList<Training> trainingList) {
        this.trainingList = trainingList;
        notifyItemRangeChanged(0, trainingList.size());
    }

    public ArrayList<Training> getTrainingList() {
        return this.trainingList;
    }

    public void addTrainingToList(Training training) {
        trainingList.add(training);
        notifyItemInserted(trainingList.size());
    }

    @NonNull
    @Override
    public TrainingSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_training_sets, parent, false);

        return new TrainingSetViewHolder(view, new TrainingRepCountEditTextListener(), new TrainingWeightEditTextListener());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull TrainingSetViewHolder holder, int position) {
        String text = (position + 1) + ". Satz";
        holder.setTV.setText(text);

        holder.repCountListener.updatePosition(holder.getAbsoluteAdapterPosition());
        holder.weightListener.updatePosition(holder.getAbsoluteAdapterPosition());

        int   reps   = trainingList.get(position).reps;
        float weight = trainingList.get(position).weight;

        if (reps > 0) {
            holder.repCount.setText(String.valueOf(reps));
        }

        if (weight > 0) {
            holder.trainingWeight.setText(String.valueOf(weight));
        }

        // Optionsmenü bauen
        holder.trainingSetOptions.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this.context, holder.trainingSetOptions);
            popup.inflate(R.menu.training_set_options_menu);
            popup.setOnMenuItemClickListener(menuItem -> {
                int pos = holder.getAbsoluteAdapterPosition();
                //noinspection SwitchStatementWithTooFewBranches
                switch (menuItem.getItemId()) {
                    case R.id.removeTrainingSet:
                        // aus der Liste löschen
                        trainingList.remove(pos);

                        // jetzt die Liste mit den Sätzen neu bauen
                        int i = 0;
                        for (Training training : trainingList) {
                            training.set = i;
                            trainingList.set(i, training);
                            i++;
                        }

                        // UI updaten
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, trainingList.size());
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TrainingSetViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TrainingSetViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.disableTextWatcher();
    }

    @Override
    public int getItemCount() {
        return this.trainingList.size();
    }

    public static class TrainingSetViewHolder extends RecyclerView.ViewHolder {
        TextView setTV;
        TextView trainingSetOptions;
        EditText repCount;
        EditText trainingWeight;

        private final TrainingRepCountEditTextListener repCountListener;
        private final TrainingWeightEditTextListener weightListener;

        public TrainingSetViewHolder(@NonNull View itemView, TrainingRepCountEditTextListener repCountListener,
                                     TrainingWeightEditTextListener weightListener) {
            super(itemView);
            setTV = itemView.findViewById(R.id.trainingSetNumberTextView);
            repCount = itemView.findViewById(R.id.repCount);
            trainingWeight = itemView.findViewById(R.id.trainingWeight);
            trainingSetOptions = itemView.findViewById(R.id.trainingSetOptions);

            this.repCountListener = repCountListener;
            this.weightListener = weightListener;
        }

        void enableTextWatcher() {
            repCount.addTextChangedListener(repCountListener);
            trainingWeight.addTextChangedListener(weightListener);
        }

        void disableTextWatcher() {
            repCount.removeTextChangedListener(repCountListener);
            trainingWeight.removeTextChangedListener(weightListener);
        }
    }

    private class TrainingRepCountEditTextListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

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
                trainingList.get(position).reps = 0;
            } else {
                trainingList.get(position).reps = Integer.parseInt(text);
            }
        }
    }

    private class TrainingWeightEditTextListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

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
                trainingList.get(position).weight = 0;
            } else {
                trainingList.get(position).weight = Float.parseFloat(text);
            }
        }
    }
}
