package com.example.fitforfit.ui.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.CalendarAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.singleton.Database;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView textView;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void setMonthView() {
        textView.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        textView = findViewById(R.id.textView);

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(view -> nextMonthAction(view));

        Button btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(view -> previousMonthAction(view));

    }

    private String monthYearFromDate(LocalDate date){
        //API Level min26 in gradle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        String m = "";
        String d = "";
        if(selectedDate.getMonthValue() < 10){
            m = "0" + String.valueOf(selectedDate.getMonthValue());
        }else{
            m = String.valueOf(selectedDate.getMonthValue());
        }
        if(Integer.parseInt(dayText) < 10){
            d = "0" + dayText;
        }else{
            d = dayText;
        }
        String date = selectedDate.getYear() + "-" + m + "-" + d;
        Log.d("TEST", date);

        //TODO CEHCK IF DATE IN DB
        try {
            AppDatabase db = Database.getInstance(this);
            int id = db.dayDao().getIdByDate(date);
            if(id > 0){
                Intent intent = new Intent(getBaseContext(), TrackerDayActivity.class);
                intent.putExtra("date", date);
                this.startActivity(intent);
                Log.d("TEST" , String.valueOf(id));
            }else{
                if(!dayText.equals("")){
                    String message = "Keine Daten für " + dayText + " " + monthYearFromDate(selectedDate);
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    Log.d("TEST2", dayText + " " + monthYearFromDate(selectedDate));
                }
            }

        }catch (SQLiteException e){
            if(!dayText.equals("")){
                String message = dayText + " " + monthYearFromDate(selectedDate);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.d("TEST2", dayText + " " + monthYearFromDate(selectedDate));
            }
        }

    }

    public void previousMonthAction(View view){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view){
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }
}