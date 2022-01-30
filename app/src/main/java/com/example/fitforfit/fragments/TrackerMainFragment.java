package com.example.fitforfit.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TrackerMainFragment extends Fragment {

    private FragmentMainBinding binding;
    private DayListAdapter dayListAdapter;


    public TrackerMainFragment(){
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
        initRecyclerView(view);
        loadDayList();
    }

    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTrackerMain);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            dayListAdapter = new DayListAdapter(getActivity());
            recyclerView.setAdapter(dayListAdapter);
        });
    }

    private void loadDayList() {

        //neue Kalender Instanz
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, 2);//Addiert ein Tag
        //Formater für richtiges Format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String des aktuellen Datum und richtigen Format
        String formatted_date_today = format.format(cal.getTime());

        AppDatabase db = Database.getInstance(getActivity());

        //Auslesen letzten Date Eintrag(letztes Date)
        String lastDate = db.dayDao().getLastDate();

        //neue Kalender Instanz
        Calendar cal_last = Calendar.getInstance();
        //Damit bei ersten Aufruf App kein NullPointer entsteht
        if(lastDate != null){

        //neue Kalender Instanz

        //String Array der Werte Jahr-Monat-Tag des letzten Date Eintrags
        String[] split = lastDate.split("-");
        //erzeugen Kalender Objekt -> Monat - 1 weil Januar in CalendarObjekt 0, Februar 1 ,...
        cal_last.set(Integer.valueOf(split[0]),Integer.valueOf(split[1]),Integer.valueOf(split[2]));
        }

        Calendar tempCal = Calendar.getInstance();
        String[] splitToday = formatted_date_today.split("-");
        cal.set(Integer.valueOf(splitToday[0]),Integer.valueOf(splitToday[1])+1,Integer.valueOf(splitToday[2]));
        tempCal.set(Integer.valueOf(splitToday[0]),Integer.valueOf(splitToday[1]),Integer.valueOf(splitToday[2]));
        long diffmilli = tempCal.getTimeInMillis() - cal_last.getTimeInMillis();
        long diffday = diffmilli / (24 * 60 *60 * 1000);

        if(lastDate == null){

            //Aktueller tag wird eingefügt
            newDayInsert(formatted_date_today, db);

        }else{
            Log.d("CONTROL_POINT_1", "diffday: " + String.valueOf(diffday));
            if(diffday>1) {
                int k = (int)diffday - 1 ;
                for (int i = 0; i < (int)diffday; i++) {

                    Calendar newCal = Calendar.getInstance();
                    newCal.add(Calendar.DATE, k * (-1));
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatted_date_today2 = format2.format(newCal.getTime());
                    newDayInsert(formatted_date_today2, db);
                    k--;

                }
            }else{

                //Vergleich Heute und letzter Eintrag -> wenn >= 0 heute ist letzter Eintrag
                cal.set(Integer.valueOf(splitToday[0]),Integer.valueOf(splitToday[1]),Integer.valueOf(splitToday[2]));
                if(cal.compareTo(cal_last) > 0){
                    Log.d("CONTROL_POINT_2", "Compare: " + String.valueOf(cal.compareTo(cal_last)));
                    newDayInsert(formatted_date_today, db);

                }
            }
        }

        //Alle Tage mit RecyclerView anzeigen
        List<Day> dayList = db.dayDao().getAllDays();
        dayListAdapter.setContext(getContext());
        dayListAdapter.setDayList(dayList);
    }

    public void newDayInsert(String newday, AppDatabase db){
        Day day = new Day();
        day.progress = 75;
        day.date = newday;
        db.dayDao().insert(day);
    }
}
