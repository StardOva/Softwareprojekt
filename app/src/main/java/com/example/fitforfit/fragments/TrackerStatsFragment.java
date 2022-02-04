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
import androidx.core.content.ContextCompat;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.slider.LabelFormatter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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

    int gewicht = 80;//in kg
    int groesse = 180;//in cm
    int alter = 21;
    int alltag = 500; //in kcal



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
        Log.d("WEEK_DAY", String.valueOf(dayOfWeek));
        if(dayOfWeek == 0){
            weekDay = "Samstag";
        }else if(dayOfWeek == 1){weekDay = "Sonntag";}
        else if(dayOfWeek == 2){weekDay = "Montag";}
        else if(dayOfWeek == 3){weekDay = "Dienstag";}
        else if(dayOfWeek == 4){weekDay = "Mittwoch";}
        else if(dayOfWeek == 5){weekDay = "Donnserstag";}
        else if(dayOfWeek == 6){weekDay = "Freitag";}
        weekDayText = view.findViewById(R.id.WeekDayvalue);
        weekDayText.setText(weekDay);

        DecimalFormat round = new DecimalFormat("#.##");

        kcalText = view.findViewById(R.id.ckalValue);
        fatText = view.findViewById(R.id.fatValue);
        satfatText = view.findViewById(R.id.satfatValue);
        carbText = view.findViewById(R.id.carbValue);
        sugarText = view.findViewById(R.id.sugarValue);
        fiberText = view.findViewById(R.id.fiberValue);
        proteinText = view.findViewById(R.id.proteinValue);
        saltText = view.findViewById(R.id.saltValue);

        kcalText.setText(String.valueOf(round.format(this.kcalOfDay)));
        fatText.setText(String.valueOf(round.format(this.fatOfDay)));
        satfatText.setText(String.valueOf(round.format(this.satfatOfDay)));
        carbText.setText(String.valueOf(round.format(this.carbOfDay)));
        sugarText.setText(String.valueOf(round.format(this.sugarOfDay)));
        fiberText.setText(String.valueOf(round.format(this.fiberOfDay)));
        proteinText.setText(String.valueOf(round.format(this.proteinOfDay)));
        saltText.setText(String.valueOf(round.format(this.saltOfDay)));

        ///BARCHART
        float grundumsatz = (float)(66.47 + (13.7 * this.gewicht) + (5 * this.groesse) - (6.8 * 21)) + this.alltag;
        float kcal = (this.kcalOfDay * 100) / grundumsatz;
        //Log.d("Grundumsatz", String.valueOf(grundumsatz));

        float proteinsoll = (float)(1.8 * this.gewicht); //1.8g in Aufbau
        float protein = (this.proteinOfDay * 100) / proteinsoll;

        float fatsoll = (float)(1 * this.gewicht); //1g in Aufbau
        float fat = (this.fatOfDay * 100) / fatsoll;

        float carbsoll = (float)(((grundumsatz)-((proteinsoll * 4.1) + (fatsoll * 9.3)))/4.1);
        float carb = (this.carbOfDay * 100) /carbsoll;
        //protein -> 4.1kcal pro gramm
        //fat -> 9.3kcal pro gramm
        //carbs -> 4.1kcal pro gramm
        //carbs sollten rest an benötigten kcal auffüllen(proteine und fette bestimmte menge)

        /*
        TODO Werte fpr Barchart bestimmen
        (tägliche Min. Werte ausdenken -> davon anteil)
         */

        BarChart bar = view.findViewById(R.id.barChart);
        ArrayList<BarEntry> listb = new ArrayList<>();
        float a = 0;
        if(100-kcal <= 0){a = 0;}else{a = 100-kcal;};
        float b = 0;
        if(100-protein <= 0){b = 0;}else{b = 100-protein;};
        float e = 0;
        if(100-fat <= 0){e = 0;}else{e = 100-fat;};
        float d = 0;
        if(100-carb <= 0){d = 0;}else{d = 100-carb;};
        listb.add(new BarEntry(1, new float[]{kcal,a}));
        listb.add(new BarEntry(2, new float[]{protein,b}));
        listb.add(new BarEntry(3, new float[]{fat,e}));
        listb.add(new BarEntry(4, new float[]{carb,d}));


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
        String[] labels = {"", "Energie","Proteine", "Fette", "Kohlendydrate"};
        bar.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        bar.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        XAxis x = bar.getXAxis();
        x.setGranularity(1f);
        x.setGranularityEnabled(true);
        x.setCenterAxisLabels(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new IndexAxisValueFormatter(labels));

        /*
        TODO Werte fpr Piechart bestimmen
         */

        PieChart pie = view.findViewById(R.id.pieChart);
        ArrayList<PieEntry> list = new ArrayList<>();
        list.add(new PieEntry(this.fatOfDay,"Fett"));
        list.add(new PieEntry(this.carbOfDay,"Kohlenhydrate"));
        list.add(new PieEntry(this.fiberOfDay,"Ballaststoffe"));
        list.add(new PieEntry(this.proteinOfDay,"Eiweiß"));
        list.add(new PieEntry(this.saltOfDay,"Salz"));

        Legend legend = pie.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(16f);
        legend.setEnabled(false);
        //legend.setForm(Legend.LegendForm.CIRCLE);

        Description des = new Description();
        des.setText("");
        pie.setDescription(des);

        PieDataSet pieDataSet = new PieDataSet(list, "");
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setColors(
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_orange_dark))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_blue_dark))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_brown))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_green))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_grey))));
        //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pie.setData(new PieData(pieDataSet));
        pie.setDrawEntryLabels(true);
        pie.setUsePercentValues(true);


        PieChart piefat = view.findViewById(R.id.pieChartfat);
        ArrayList<PieEntry> listfat = new ArrayList<>();
        listfat.add(new PieEntry(this.fatOfDay-this.satfatOfDay,"ungesättigte Fett"));
        listfat.add(new PieEntry(this.satfatOfDay,"gesättigte Fette"));
        PieDataSet pieDataSetfat = new PieDataSet(listfat, "");
        pieDataSetfat.setColors(
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_orange_dark))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_orange_light))));
        pieDataSetfat.setValueTextSize(16f);
        piefat.setData(new PieData(pieDataSetfat));
        piefat.getLegend().setEnabled(false);
        piefat.setDescription(des);

        PieChart piecarb = view.findViewById(R.id.pieChartcarb);
        ArrayList<PieEntry> listcarb = new ArrayList<>();
        listcarb.add(new PieEntry(this.carbOfDay-this.sugarOfDay,"andere Kohlenhydrate"));
        listcarb.add(new PieEntry(this.sugarOfDay,"Zucker"));
        PieDataSet pieDataSetcarb = new PieDataSet(listcarb, "");
        pieDataSetcarb.setColors(
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_blue_dark))),
                Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getContext(), R.color.fit_blue_light))));
        pieDataSetcarb.setValueTextSize(16f);
        piecarb.setData(new PieData(pieDataSetcarb));
        piecarb.getLegend().setEnabled(false);
        piecarb.setDescription(des);



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
