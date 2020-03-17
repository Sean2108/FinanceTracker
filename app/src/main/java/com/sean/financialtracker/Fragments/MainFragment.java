package com.sean.financialtracker.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sean.financialtracker.Activities.ListExpActivity;
import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;
import com.sean.financialtracker.Data.ExpenditureSum;
import com.sean.financialtracker.R;

import java.text.NumberFormat;

public class MainFragment extends Fragment {

    private DBHandler db;
    private static final String PREFS_NAME = "MyBudget";
    private static String[] ranges = {"daily", "weekly", "monthly"};
    private NumberFormat formatter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        super.onCreate(savedInstanceState);

        formatter = NumberFormat.getCurrencyInstance();
        int timeRange = getArguments().getInt("timeRange");
        String timeRangeStr = ranges[timeRange];

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        //get budget values from SharedPreferences
        int budget;
        budget = settings.getInt(timeRangeStr + "_budget", 1000);

        //get sum of each expenditure type from DB
        db = new DBHandlerImpl(getActivity());
//        db.deleteAllExp();

        ExpenditureSum expSum = getExpenditures(db, budget, timeRangeStr);
        updateDonutChart(getActivity(), expSum, budget, rootView);

        return rootView;
    }

    private void updateDonutChart(final Context context, ExpenditureSum expSum, int budget, View rootView) {
        float remainder = expSum.getRemainingBudget();
        PieDataSet set = new PieDataSet(expSum.getPieEntries(), "");
        set.setDrawIcons(true);
        set.setDrawValues(false);
//        set.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
//        set.setValueTextSize(10f);
//        set.setIconsOffset(MPPointF.getInstance(15f, 0f));
//        set.setValueLineColor(Color.TRANSPARENT);
        set.setColors(new int[] {  R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5, R.color.color6 }, context);
        PieData data = new PieData(set);
        PieChart chart = (PieChart) rootView.findViewById(R.id.chart);
        if (remainder > 0f) {
            chart.setMaxAngle(((float)budget - remainder) / (float) budget * 360f);
            chart.setCenterText("BALANCE: " + formatter.format(remainder));
        }
        else {
            chart.setMaxAngle(360f);
            chart.setCenterTextColor(Color.RED);
            chart.setCenterText("BUDGET EXCEEDED");
        }
        chart.setData(data);
        chart.invalidate();
        chart.setCenterTextSize(20f);
        chart.setTransparentCircleColor(Color.LTGRAY);
        chart.setTransparentCircleAlpha(10);
        chart.setDrawEntryLabels(true);
        chart.setEntryLabelTextSize(0f);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String label = ((PieEntry) e).getLabel();
                Intent intent = new Intent(context, ListExpActivity.class);
                intent.putExtra("EXP_TYPE", label);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private ExpenditureSum getExpenditures(DBHandler db, int budget, String timeRangeStr) {
        float foodExp = db.getCategoryExpSum("Food", timeRangeStr);
        float transportExp = db.getCategoryExpSum("Transport", timeRangeStr);
        float entertainmentExp = db.getCategoryExpSum("Entertainment", timeRangeStr);
        float subExp = db.getCategoryExpSum("Subscription", timeRangeStr);
        float othersExp = db.getCategoryExpSum("Others", timeRangeStr);

        float remainingBudget = (float)budget - foodExp - transportExp - entertainmentExp - subExp - othersExp;
        return new ExpenditureSum(remainingBudget, foodExp, transportExp, entertainmentExp, subExp, othersExp, getActivity());
    }
}
