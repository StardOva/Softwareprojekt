package com.example.fitforfit.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.WorkoutProgressAdapter;
import com.example.fitforfit.calendar.DayViewContainer;
import com.example.fitforfit.calendar.MonthHeaderBinder;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.TrainingActivity;
import com.example.fitforfit.utils.ColorUtils;
import com.example.fitforfit.utils.DateUtils;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

public class WorkoutProgressFragment extends Fragment {

    private FragmentMainBinding binding;
    private WorkoutProgressAdapter workoutProgressAdapter;
    private int workoutId;

    ArrayList<String> workoutProgressList = null;
    private int[] trainingIds;

    // wie viele Sessions sollen unter dem Kalender angezeigt werden
    public static final int MAX_PREVIEW_SESSIONS = 3;

    public WorkoutProgressFragment() {
        super(R.layout.fragment_workout_progress);
    }

    public static WorkoutProgressFragment newInstance(int workoutId) {
        Bundle args = new Bundle();
        args.putInt("workoutId", workoutId);
        WorkoutProgressFragment fragment = new WorkoutProgressFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_workout_progress, binding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.workoutId = getArguments().getInt("workoutId");

        loadWorkoutProgressList();
        ColorUtils colorUtils = new ColorUtils(getContext());

        com.kizitonwose.calendarview.CalendarView calView = view.findViewById(R.id.calendarViewLib);
        calView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer viewContainer, @NonNull CalendarDay calendarDay) {
                if (calendarDay.getOwner() == DayOwner.THIS_MONTH) {
                    viewContainer.textView.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

                    String currentDate = calendarDay.getDate().format(DateTimeFormatter.ofPattern(DateUtils.getGermanDateFormat()));
                    if (workoutProgressList.contains(currentDate)) {
                        viewContainer.textView.setTextColor(colorUtils.getFitOrangeLight());
                        viewContainer.textView.setTypeface(viewContainer.textView.getTypeface(), Typeface.BOLD);

                        viewContainer.textView.setOnClickListener(view1 -> {
                            Intent intent = new Intent(getActivity(), TrainingActivity.class);
                            intent.putExtra("workoutId", workoutId);
                            intent.putExtra("trainingId", trainingIds[workoutProgressList.indexOf(currentDate)]);
                            intent.putExtra("createdAt", currentDate);

                            requireActivity().startActivity(intent);
                        });

                    }
                }
            }
        });

        calView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderBinder>() {
            @NonNull
            @Override
            public MonthHeaderBinder create(@NonNull View view) {
                return new MonthHeaderBinder(view);
            }

            @Override
            public void bind(@NonNull MonthHeaderBinder viewContainer, @NonNull CalendarMonth calendarMonth) {
                String lowercaseMonth  = calendarMonth.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN);
                String displayedHeader = lowercaseMonth.substring(0, 1).toUpperCase() + lowercaseMonth.substring(1) + " " + calendarMonth.getYear();
                viewContainer.textView.setText(displayedHeader);
            }
        });

        YearMonth currentMonth = YearMonth.now();

        // was ist der erste Monat der angezeigt wird?
        YearMonth firstMonth = YearMonth.parse(workoutProgressList.get(workoutProgressList.size() - 1),
                DateTimeFormatter.ofPattern(DateUtils.getGermanDateFormat()));

        YearMonth lastMonth      = currentMonth.plusMonths(0);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

        calView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calView.scrollToMonth(currentMonth);

        initRecyclerView(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initRecyclerView(View view) {
        AsyncTask.execute(() -> {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWorkoutProgress);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            this.workoutProgressAdapter = new WorkoutProgressAdapter(getActivity(), this.workoutId);
            this.workoutProgressAdapter.setParentActivity(getContext());

            // zeige nur die letzten drei Sessions in der Recycler View an
            ArrayList<String> copyWorkoutProgressList = new ArrayList<>();
            int[]             copyTrainingIds         = new int[MAX_PREVIEW_SESSIONS];

            for (int i = 0; i < MAX_PREVIEW_SESSIONS; i++) {
                copyWorkoutProgressList.add(workoutProgressList.get(i));
                copyTrainingIds[i] = trainingIds[i];
            }

            this.workoutProgressAdapter.setWorkoutProgressList(copyWorkoutProgressList, copyTrainingIds);

            recyclerView.setAdapter(this.workoutProgressAdapter);
            // wegen Fehler muss das notify() auf UI Thread laufen
            recyclerView.post(() -> workoutProgressAdapter.notifyDataSetChanged());
        });
    }

    private void loadWorkoutProgressList() {
        AppDatabase db = Database.getInstance(getContext());

        trainingIds = db.trainingDao().getAllIdsDesc(this.workoutId);
        workoutProgressList = new ArrayList<>();

        for (int id : trainingIds) {
            String createdAt = db.trainingDao().getCreatedAt(id);
            workoutProgressList.add(createdAt);
        }
    }
}
