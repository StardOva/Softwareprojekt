package com.example.fitforfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.ui.main.TrackerDayActivity;

import java.util.List;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.MyViewHolder> {

    private Context context;
    private Context mainActivity;

    private List<Day> dayList = null;

    public DayListAdapter(Context context) {
        this.context = context;
    }

    public void setContext(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DayListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_tracker_main, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayListAdapter.MyViewHolder holder, int position) {
        String   date        = this.dayList.get(position).date;
        String[] split       = this.dayList.get(position).date.split("-");
        String   displayDate = split[2] + "." + split[1] + ".";
        holder.dayButton.setText(displayDate);

        holder.dayButton.setOnClickListener(v -> {
            Log.d("DayButton", split[2] + "." + split[1] + ".");

            Intent intent = new Intent(mainActivity, TrackerDayActivity.class);
            intent.putExtra("date", date);
            mainActivity.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        if (this.dayList != null) {
            return this.dayList.size();
        }

        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /*TextView tvFirstName;
        TextView tvLastName;*/
        Button dayButton;

        public MyViewHolder(View view) {
            super(view);
            /*tvFirstName = view.findViewById(R.id.tvFirstName);
            tvLastName = view.findViewById(R.id.tvLastName);*/
            dayButton = view.findViewById(R.id.buttonDay);

        }
    }
}
