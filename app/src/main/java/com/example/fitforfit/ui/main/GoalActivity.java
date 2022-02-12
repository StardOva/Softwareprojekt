package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.singleton.Database;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoalActivity extends AppCompatActivity {

    private int dayId;
    AppDatabase db = Database.getInstance(this);
    EditText weightText;

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
            try {
                db.dayDao().updateWeightById(Float.parseFloat(String.valueOf(weightText.getText())), this.dayId);
                this.weightText.setText(String.valueOf(db.dayDao().getWeightById(this.dayId)));
            }catch (NumberFormatException n){
                String message = "Bitte gebe nur Zahlen ein";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                //Log.d("TEST2", dayText + " " + monthYearFromDate(selectedDate));
            }

        });

        LineChart volumeReportChart = findViewById(R.id.reportingChart);
        volumeReportChart.setTouchEnabled(true);
        volumeReportChart.setPinchZoom(true);
        LimitLine ll1 = new LimitLine(30f,"Title");
        ll1.setLineColor(Color.BLUE);
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(35f, "");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);

        XAxis xAxis = volumeReportChart.getXAxis();
        YAxis leftAxis = volumeReportChart.getAxisLeft();

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);

        LineDataSet lineDataSet = new LineDataSet(getLineData(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        volumeReportChart.setData(data);
        volumeReportChart.invalidate();

    }

    private ArrayList<Entry> getLineData() {
        ArrayList<Entry> data = new ArrayList<Entry>();
        data.add(new Entry(0,20));
        data.add(new Entry(1,24));
        data.add(new Entry(2,2));
        data.add(new Entry(3,10));

        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.weightText.setText(String.valueOf(db.dayDao().getWeightById(this.dayId)));
    }

    //TODO
    //GEWICHT WERTE AUS DB IN LINCHART
    //TAGE IN DB AN X ACHSE
    //MAX ANZAHL AN TAGEN ANZEIGEN
    //SUCH FUNKTION FÜR GEWICHT VERGANGENER TAGE
}