package com.example.fitforfit.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
        loadUserList();
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

    private void loadUserList() {
        Calendar cal = Calendar.getInstance();  //neue Kalender Instanz
        //cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); //Formater für richtiges Format
        String formatted_date_today = format1.format(cal.getTime());    //String des aktuellen Datum und richtigen Format


        AppDatabase db = Database.getInstance(getActivity());

        String lastDate = db.dayDao().getLastDate();   //Auslesen letzten Date Eintrag(letztes Date)
        if(lastDate == null){
            //lastDate = "2000-10-06";

            Day day = new Day();
            day.progress = 75;

            day.date = formatted_date_today;
            db.dayDao().insert(day);
        }else{

            Calendar cal_last = Calendar.getInstance(); //neue Kalender Instanz
            String[] split = lastDate.split("-");   //String Array der Werte Jahr-Monat-Tag des letzten Date Eintrags
            cal_last.set(Integer.valueOf(split[0]),Integer.valueOf(split[1])-1,Integer.valueOf(split[2])); //erzeugen Kalender Objekt

            if(cal.compareTo(cal_last) > 0){ //Vergleich Heute und letzter Eintrag -> wenn >= 0 heute ist letzter Eintrag

                Day day = new Day();
                day.progress = 75;

                day.date = formatted_date_today;
                db.dayDao().insert(day);


            }
        }
        /*
        TO DO
        Alle fehlenden tage werden hinzugefügt

        Bsp.->
        3 tage nicht App aktiviert
        -> 3 letzten Tage hinzufügen, nicht nur aktuellen
         */


        List<Day> dayList = db.dayDao().getAllDays();
        dayListAdapter.setDayList(dayList);
    }
}
