package com.example.fitforfit.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitforfit.R;
import com.example.fitforfit.adapter.DayListAdapter;
import com.example.fitforfit.dao.IngredientDao;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.databinding.FragmentMainBinding;
import com.example.fitforfit.entity.Day;
import com.example.fitforfit.entity.Ingredient;
import com.example.fitforfit.entity.Meal;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.ui.main.TrackerDayActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrackerStatsFragment extends Fragment {

    private FragmentMainBinding binding;
    private int dayId;
    List<Meal> meals = new ArrayList<>();
    List<Ingredient> ingredientsOfDay = new ArrayList<>();

    float kcalOfDay;
    float fatOfDay;
    float satfatOfDay;
    float carbOfDay;
    float sugarOfDay;
    float fiberOfDay;
    float proteinOfDay;
    float saltOfDay;

    TextView kcalText;
    TextView fatText;
    TextView satfatText;
    TextView carbText;
    TextView sugarText;
    TextView fiberText;
    TextView proteinText;
    TextView saltText;
    TextView dateText;
    TextView weekDayText;

    String date;

    AppDatabase db = Database.getInstance(getActivity());



    public TrackerStatsFragment(){
        super(R.layout.fragment_tracker_stats);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return inflater.inflate(R.layout.fragment_tracker_stats, binding.getRoot());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);

        getData();


    }

    private void initViews(View view) {

        date = db.dayDao().getDateById(this.dayId).toString();
        dateText = view.findViewById(R.id.DateValue);
        dateText.setText(date);


        Calendar c = Calendar.getInstance(); //neue Kalender Instanz
        String[] split = date.split("-");   //String Array der Werte Jahr-Monat-Tag des letzten Date Eintrags
        c.set(Integer.valueOf(split[0]),Integer.valueOf(split[1]),Integer.valueOf(split[2])); //erzeugen Kalender Objekt#
        String weekDay = "";
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == 0){
            weekDay = "Mittwoch";
        }else if(dayOfWeek == 1){weekDay = "Donnerstag";}
        else if(dayOfWeek == 2){weekDay = "Freitag";}
        else if(dayOfWeek == 3){weekDay = "Samstag";}
        else if(dayOfWeek == 4){weekDay = "Sonntag";}
        else if(dayOfWeek == 5){weekDay = "Montag";}
        else if(dayOfWeek == 6){weekDay = "Dienstag";}
        weekDayText = view.findViewById(R.id.WeekDayvalue);
        weekDayText.setText(weekDay);


        kcalText = view.findViewById(R.id.ckalValue);
        fatText = view.findViewById(R.id.fatValue);
        satfatText = view.findViewById(R.id.satfatValue);
        carbText = view.findViewById(R.id.carbValue);
        sugarText = view.findViewById(R.id.sugarValue);
        fiberText = view.findViewById(R.id.fiberValue);
        proteinText = view.findViewById(R.id.proteinValue);
        saltText = view.findViewById(R.id.saltValue);

        kcalText.setText(String.valueOf(this.kcalOfDay));
        fatText.setText(String.valueOf(this.fatOfDay));
        satfatText.setText(String.valueOf(this.satfatOfDay));
        carbText.setText(String.valueOf(this.carbOfDay));
        sugarText.setText(String.valueOf(this.sugarOfDay));
        fiberText.setText(String.valueOf(this.fiberOfDay));
        proteinText.setText(String.valueOf(this.proteinOfDay));
        saltText.setText(String.valueOf(this.saltOfDay));

        int[] colors = new int[10];
        int counter = 0;

        for(int color : ColorTemplate.JOYFUL_COLORS){
            colors[counter] = color;
            counter++;
        }
        for(int color : ColorTemplate.MATERIAL_COLORS){
            colors[counter] = color;
            counter++;
        }

        /*
        TODO Werte fpr Piechart bestimmen
         */

        PieChart pie = view.findViewById(R.id.pieChart);
        ArrayList<PieEntry> list = new ArrayList<>();
        list.add(new PieEntry(1,"Fett"));
        list.add(new PieEntry(2,"gesättigte Fette"));
        list.add(new PieEntry(2,"Kohlenhydrate"));
        list.add(new PieEntry(2,"Zucker"));
        list.add(new PieEntry(2,"Ballaststoffe"));
        list.add(new PieEntry(4,"Eiweiß"));
        list.add(new PieEntry(2,"Salz"));



        Legend legend = pie.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(16f);
        //legend.setForm(Legend.LegendForm.CIRCLE);


        Description des = new Description();
        des.setText("");
        pie.setDescription(des);


        PieDataSet pieDataSet = new PieDataSet(list, "");
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setColors(colors);
        //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pie.setData(new PieData(pieDataSet));
        pie.setDrawEntryLabels(false);
        pie.setUsePercentValues(true);

        ///BARCHART

        /*
        TODO Werte fpr Barchart bestimmen
        (tägliche Min. Werte ausdenken -> davon anteil)
         */

        BarChart bar = view.findViewById(R.id.barChart);
        ArrayList<BarEntry> listb = new ArrayList<>();
        listb.add(new BarEntry(1, new float[]{40,100-40}));
        listb.add(new BarEntry(2, new float[]{20,100-20}));
        listb.add(new BarEntry(3, new float[]{90,100-90}));
        listb.add(new BarEntry(4, new float[]{90,100-90}));


        BarDataSet barDataSet = new BarDataSet(listb, "");
        barDataSet.setColors(Color.GREEN, Color.RED);
        barDataSet.setValueTextSize(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barDataSet.setStackLabels(new String[]{"IST", "SOLL"});
        BarData barData = new BarData(barDataSet);

        bar.setDrawValueAboveBar(false);

        bar.setFitBars(true);
        bar.setData(barData);
        bar.getDescription().setText("");
        bar.animateY(2000);
    }

    private void getData() {
        kcalOfDay = 0;
        fatOfDay = 0;
        satfatOfDay = 0;
        carbOfDay = 0;
        sugarOfDay = 0;
        fiberOfDay = 0;
        proteinOfDay = 0;
        saltOfDay = 0;

        this.ingredientsOfDay.clear();
        this.meals.clear();



        TrackerDayActivity dayActivity = (TrackerDayActivity) getActivity();
        this.dayId = dayActivity.getCurrentDayId();
        Log.d("GET_DATA", "DayID: "+ String.valueOf(this.dayId));


        this.meals = db.mealDao().getAllMealsOnDay(this.dayId);
        try {
            for(Meal meal : meals){
                List<Ingredient> ingredientsOfMeal  = db.ingredientDao().getAllIngredientsOnMeal(meal.id);
                //for(Ingredient ing: ingredientsOfMeal){
                //    this.ingredientsOfDay.add(ingredient);
                //Log.d("GET_DATA", "IngredientID: "+ String.valueOf(ing.id));
                //}
                Log.d("GET_DATA", "MealID: " + String.valueOf(meal.id));


                this.ingredientsOfDay.addAll(ingredientsOfMeal);
                Log.d("GET_DATA", "CHECK");

            }
        }catch (NullPointerException e){
            Log.d("GET_DATA", "MEAL ITERATION NULLPOINTER");
        }

        try {
            for(Ingredient ingredient : this.ingredientsOfDay){
                Product product = db.productDao().getProductById(ingredient.product_id);
                float quant = (float) ingredient.quantity;
                float quantFactor = quant / 100;

                this.kcalOfDay = kcalOfDay + (product.ckal * quantFactor);
                this.fatOfDay = fatOfDay + (product.fat * quantFactor);
                this.satfatOfDay = satfatOfDay + (product.saturated_fat * quantFactor);
                this.carbOfDay = carbOfDay + (product.carb * quantFactor);
                this.sugarOfDay = sugarOfDay + (product.sugar * quantFactor);
                this.fiberOfDay = fiberOfDay + (product.fiber * quantFactor);
                this.proteinOfDay = proteinOfDay + (product.protein * quantFactor);
                this.saltOfDay = saltOfDay + (product.salt * quantFactor);
            }

        }catch (NullPointerException e){
            Log.d("GET_DATA", "INGREDIENT ITERATION NULLPOINTER");
        }

        initViews(getView());


    }

    private void initRecyclerView(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();


    }
}
