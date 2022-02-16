package com.example.fitforfit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Exercise;
import com.example.fitforfit.entity.Training;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.utils.ColorUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutEvaluationAdapter extends RecyclerView.Adapter<WorkoutEvaluationAdapter.WorkoutEvaluationViewHolder> {

    private List<Exercise> exerciseList = null;
    private HashMap<Integer, ArrayList<Training>> exerciseTrainingList = null;
    private final Context context;
    private final AppDatabase db;
    private final int workoutId;

    public WorkoutEvaluationAdapter(Context context, int workoutId) {
        this.context = context;
        this.db = Database.getInstance(context);
        this.workoutId = workoutId;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }

    public void setExerciseTrainingList(HashMap<Integer, ArrayList<Training>> exerciseTrainingList) {
        this.exerciseTrainingList = exerciseTrainingList;
    }

    @NonNull
    @Override
    public WorkoutEvaluationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_row_workout_evaluation, parent, false);

        return new WorkoutEvaluationAdapter.WorkoutEvaluationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutEvaluationViewHolder holder, int position) {
        Exercise  exercise  = exerciseList.get(position);
        LineChart lineChart = holder.lineChart;

        ColorUtils colorUtils = new ColorUtils(this.context);

        // Chart Einstellungen
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        // Eine Liste für das Gewicht, eine für die Wiederholungen und eine für die Legende
        ArrayList<Entry>       weightList = new ArrayList<>();
        ArrayList<Entry>       repList    = new ArrayList<>();
        ArrayList<LegendEntry> legendList = new ArrayList<>();

        //List<Training> trainingList = db.trainingDao().getMaxWeightSetsByWorkoutAndExerciseId(workoutId, exercise.id);

        // ArrayList<Training> trainingList = new ArrayList<>();
        ArrayList<Training> trainingList = exerciseTrainingList.get(exercise.id);

        if (trainingList != null && trainingList.size() > 0){
            int   i         = 0;
            float maxWeight = 0;
            int   maxReps   = 0;

            for (Training training : trainingList) {
                weightList.add(new Entry(i, training.weight));
                repList.add(new Entry(i, training.reps));

                if (training.weight > maxWeight) {
                    maxWeight = training.weight;
                }

                if (training.reps > maxReps) {
                    maxReps = training.reps;
                }

                LegendEntry legendEntry = new LegendEntry();
                legendEntry.label = training.createdAt;
                legendList.add(legendEntry);

                i++;
            }

            LineDataSet weightSet = new LineDataSet(weightList, "Gewicht");
            weightSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            weightSet.setColor(colorUtils.getFitGreen());
            weightSet.setCircleColor(colorUtils.getFitGreen());
            weightSet.setValueTextSize(12f);

            LineDataSet repSet = new LineDataSet(repList, "Wiederholungen");
            repSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            repSet.setColor(colorUtils.getFitOrangeDark());
            repSet.setCircleColor(colorUtils.getFitOrangeDark());
            repSet.setValueTextSize(12f);

            LineData lineData = new LineData(weightSet, repSet);
            lineChart.setData(lineData);

            lineChart.getLegend().setCustom(legendList);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setTextSize(12f);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(weightList.size() - 1);
            xAxis.setGranularity(1f);

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setTextSize(12f);
            leftAxis.setAxisMinimum(0);
            leftAxis.setAxisMaximum(maxWeight + 20);
            leftAxis.setGranularity(1f);

            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setTextSize(12f);
            rightAxis.setAxisMaximum(maxReps + 10);
            rightAxis.setGranularity(1f);

        }
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null) {
            return exerciseList.size();
        }

        return 0;
    }

    public static class WorkoutEvaluationViewHolder extends RecyclerView.ViewHolder {
        LineChart lineChart;

        public WorkoutEvaluationViewHolder(@NonNull View itemView) {
            super(itemView);
            lineChart = itemView.findViewById(R.id.lineChart);
        }
    }
}
