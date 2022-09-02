package com.example.fitforfit.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.DayListAdapter;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.sync.DatabaseSync;
import com.example.fitforfit.ui.main.CalendarActivity;
import com.example.fitforfit.ui.main.GoalActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TrackerMainFragment extends BaseFragment {

    private FragmentMainBinding binding;
    private DayListAdapter dayListAdapter;

    public TrackerMainFragment() {
        super(R.layout.fragment_tracker);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_tracker, binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button cBtn = view.findViewById(R.id.calendarBtn);
        cBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CalendarActivity.class);
            //intent.putExtra("date", date);
            this.startActivity(intent);
        });
        Button gBtn = view.findViewById(R.id.goalBtn);
        gBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), GoalActivity.class);
            //intent.putExtra("date", date);
            this.startActivity(intent);
        });
        initRecyclerView(view);

        initToolbar(getString(R.string.tracker_name));
        toolbar.setNavigationIcon(null);

        Button uploadDbBtn = view.findViewById(R.id.uploadDbBtn);
        Button downloadDbBtn = view.findViewById(R.id.downloadDbBtn);

        String backupUrl = DatabaseSync.getBackupUrl(getContext());

        uploadDbBtn.setOnClickListener(view1 -> {
            DatabaseSync.uploadDB(getContext(), backupUrl);
        });

        downloadDbBtn.setOnClickListener(view1 -> {
            DatabaseSync.downloadDB(getContext(), backupUrl);
        });
    }


    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTrackerMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        dayListAdapter = new DayListAdapter(getActivity());
        recyclerView.setAdapter(dayListAdapter);

        AsyncTask.execute(this::loadDayList);
    }

    private void loadDayList() {

        AppDatabase db = Database.getInstance(getActivity());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//FORMAT
        Calendar cal = Calendar.getInstance();//HEUTE
        //cal.add(Calendar.DATE, 30);
        String formatted_date_today = format.format(cal.getTime());//HEUTE STRING
        String[] splitToday = formatted_date_today.split("-");
        cal.set(Integer.valueOf(splitToday[0]), Integer.valueOf(splitToday[1]) - 1, Integer.valueOf(splitToday[2]));//HEUTE ALS CAL

        String lastDate = db.dayDao().getLastDate();//AUSLESEN LETZTES DATUM
        //String lastDate = "2022-02-02";//STRING LETZTES DATUM
        Calendar cal_last = Calendar.getInstance();
        if (lastDate != null) {
            String[] split = lastDate.split("-");
            cal_last.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));//LETZTES DATUM ALS CAL (Jan-0, Feb-1,...)
        }

        long diffmilli = cal.getTimeInMillis() - cal_last.getTimeInMillis();
        long diffday = diffmilli / (24 * 60 * 60 * 1000);
        Log.d("CHECK_DATE", "HEUTE: " + format.format(cal.getTime()));
        Log.d("CHECK_DATE", "Letztes eingetragendes Datum: " + lastDate + "(" + format.format(cal_last.getTime()) + ") mit d:" + String.valueOf(diffday));
        //System.out.println("Letztes eingetragendes Datum: "+ lastDate + "("+ format.format(cal_last.getTime())+") mit d:" + String.valueOf(diffday));
        if (lastDate == null) {
            newDayInsert(formatted_date_today, db);//HEUTE WIRD EINGEFÜGT
            Log.d("CHECK_DATE", "Noch kein Tag vorhanden - heute als ersten tag eingetragen: " + formatted_date_today);
            //System.out.println("Noch kein Tag vorhanden - heute als ersten tag eingetragen " + formatted_date_today);
        } else {
            Log.d("CONTROL_POINT_1", "diffday: " + String.valueOf(diffday));
            if (diffday > 1) {
                int k = (int) diffday;
                for (int i = 0; i <= (int) diffday; i++) {

                    Calendar newCal = cal;
                    newCal.add(Calendar.DATE, k * (-1));
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatted_date_today2 = format2.format(newCal.getTime());
                    newDayInsert(formatted_date_today2, db);
                    //System.out.println("DAtum eingetragen: " + formatted_date_today2);
                    //System.out.println(String.valueOf(k));
                    Log.d("CHECK_DATE", formatted_date_today2 + String.valueOf(k));
                    newCal.add(Calendar.DATE, k);
                    k--;


                }
            } else {

                //Vergleich Heute und letzter Eintrag -> wenn >= 0 heute ist letzter Eintrag
                //System.out.println("Datum ist aktuell: " + splitToday[0]+"-"+splitToday[1]+"-"+splitToday[2] + "("+ format.format(cal.getTime())+")");
                if (cal.compareTo(cal_last) > 0) {
                    Log.d("CONTROL_POINT_2", "Compare: " + String.valueOf(cal.compareTo(cal_last)));
                    newDayInsert(formatted_date_today, db);
                    //System.out.println("Datum geändert zu: "+ formatted_date_today);

                } else {
                    System.out.println("Keine Änderung");
                }
            }
        }

        //Alle Tage mit RecyclerView anzeigen
        List<Day> dayList = db.dayDao().getAllDays();

        requireActivity().runOnUiThread(() -> {
            dayListAdapter.setContext(getContext());
            dayListAdapter.setDayList(dayList);
        });
    }

    public void newDayInsert(String newday, AppDatabase db) {
        Day day = new Day();
        day.progress = 75;
        day.date = newday;
        if (db.dayDao().getDayIdCount() < 1) {
            day.weight = 70;
        } else {
            day.weight = db.dayDao().getLastWeight();
        }

        db.dayDao().insert(day);
    }
}
