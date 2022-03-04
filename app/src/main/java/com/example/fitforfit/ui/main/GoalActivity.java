package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.utils.ColorUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoalActivity extends AppCompatActivity {

    private int dayId;
    AppDatabase db = Database.getInstance(this);
    TextView weightText;

    ArrayList<LegendEntry> legendList;
    ArrayList<Entry> data;
    List<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        initData();
        initViews();
    }

    private void initData() {
        this.dayId = db.dayDao().getLastDayId();
    }

    private void initViews() {

        this.weightText = findViewById(R.id.weightText);
        Button changeBtn = findViewById(R.id.changeWeightBtn);

        this.weightText.setText(String.valueOf(db.dayDao().getWeightById(this.dayId)));

        changeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeWeightActivity.class);
            this.startActivity(intent);
        });

        initLineCart();
        initOldWeight();



    }

    private void initOldWeight() {

        TextView oldWeightText = findViewById(R.id.weightOldText);
        EditText yearEdit = findViewById(R.id.yearText);
        EditText monthText = findViewById(R.id.monthText);
        EditText dayText = findViewById(R.id.dayText);
        Button showBtn = findViewById(R.id.showOldWeightBtn);
        Button changeOldBtn = findViewById(R.id.changeOldWeightBtn);

        try {
            Day lastDay = db.dayDao().getLastWeightDay(db.dayDao().getWeightById(this.dayId));
            oldWeightText.setText(String.valueOf(lastDay.weight));
            yearEdit.setText(lastDay.date.substring(0, 4));
            monthText.setText(lastDay.date.substring(5,7));
            dayText.setText(lastDay.date.substring(8,10));
        }catch (SQLiteException | NumberFormatException | NullPointerException e){
            Day lastDay = db.dayDao().getLastDay();
            oldWeightText.setText(String.valueOf(lastDay.weight));
            yearEdit.setText(lastDay.date.substring(0, 4));
            monthText.setText(lastDay.date.substring(5,7));
            dayText.setText(lastDay.date.substring(8,10));
        }


        showBtn.setOnClickListener(view -> {
            try {
                String month = String.valueOf(monthText.getText());
                String day = String.valueOf(dayText.getText());

                if(Integer.parseInt(month)<10 && !month.contains("0")){
                    month = "0" + month;
                }
                if(Integer.parseInt(day)<10 && !day.contains("0")){
                    day = "0" + day;
                }
                String date = yearEdit.getText()+ "-" + month + "-" + day;
                Log.d("TEST", date);
                int id = db.dayDao().getIdByDate(date);
                Day oldDay = db.dayDao().getDayById(id);

                oldWeightText.setText(String.valueOf(oldDay.weight));
                yearEdit.setText(oldDay.date.substring(0, 4));
                monthText.setText(oldDay.date.substring(5,7));
                dayText.setText(oldDay.date.substring(8,10));
            }catch (SQLiteException | NumberFormatException | NullPointerException e){
                String message = "Keine Daten vorhanden";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        changeOldBtn.setOnClickListener(view -> {
            try {
                String month = String.valueOf(monthText.getText());
                String day = String.valueOf(dayText.getText());
                if(Integer.parseInt(month)<10  && !month.contains("0")){
                    month = "0" + month;
                }
                if(Integer.parseInt(day)<10 && !day.contains("0")){
                    day = "0" + day;
                }
                String date = yearEdit.getText()+ "-" + month + "-" + day;
                int id = db.dayDao().getIdByDate(date);

                Intent intent = new Intent(getBaseContext(), ChangeWeightActivity.class);
                intent.putExtra("dayId", String.valueOf(id));
                this.startActivity(intent);

            }catch (SQLiteException | NumberFormatException | NullPointerException e){
                String message = "Keine Daten vorhanden";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }


        });
    }

    private void initLineCart() {
        ColorUtils colorUtils = new ColorUtils(this);
        LineChart lineChart = findViewById(R.id.reportingChart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        LimitLine ll1 = new LimitLine(30f,"Title");
        ll1.setLineColor(colorUtils.getColor(R.color.fit_blue_dark));
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);

        LineDataSet lineDataSet = new LineDataSet(getLineData(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(colorUtils.getColor(R.color.fit_blue_dark));
        lineDataSet.setCircleColor(colorUtils.getColor(R.color.fit_blue_light));
        lineDataSet.setValueTextSize(0f);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();

        lineChart.getLegend().setCustom(this.legendList);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(days.size());
        xAxis.setGranularity(1f);
        //XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        //xAxis.setPosition(position);

        //xAxis.setLabelCount(days.size(), true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(getMaxWeight(this.days) + 20);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    private ArrayList<Entry> getLineData() {
        this.data = new ArrayList<Entry>();
        this.legendList = new ArrayList<>();
        this.days = db.dayDao().getDaysOfLastMonth();
        int i = 1;

        //Log.d("TEST", days.get(days.size() - 1).date);

        for (i = 1; i <= days.size(); i++){
            this.data.add(new Entry(i, days.get(days.size() - i).weight));
            //Log.d("TEST", String.valueOf(i) + "/" +String.valueOf(days.get(days.size() - i).weight));
            if(i == 1){
                insertLegendEntry(i);
            }else if(i == days.size()){
                insertLegendEntry(i);
            }

            if(days.size() > 20){
                if(i%4 ==0 ){
                    insertLegendEntry(i);
                }
            }else if(days.size() > 10){
                if(i%2 ==0 ){
                    insertLegendEntry(i);
                }
            }
            else{
                insertLegendEntry(i);

            }


        }
        this.data.add(new Entry(i+1, 50));
        return data;
    }

    private void insertLegendEntry(int i) {
        LegendEntry legendEntry = new LegendEntry();
        legendEntry.label = days.get(days.size() - i).date.substring(5);
        this.legendList.add(legendEntry);
    }

    private float getMaxWeight(List<Day> dayList){
        float max = 0;
        for (Day day: dayList) {
            if(day.weight > max){
                max = day.weight;
            }
        }
        return max;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.weightText.setText(String.valueOf(db.dayDao().getWeightById(this.dayId)));
        initLineCart();
        initOldWeight();
    }

    //TODO
    //GEWICHT WERTE AUS DB IN LINCHART
    //TAGE IN DB AN X ACHSE
    //MAX ANZAHL AN TAGEN ANZEIGEN
    //SUCH FUNKTION FÜR GEWICHT VERGANGENER TAGE
}