package com.sean.financialtracker.Data;

import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.data.PieEntry;
import com.sean.financialtracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 28/8/17.
 */

public class ExpenditureSum {
    private float remainingBudget;
    private float foodExp;
    private float transportExp;
    private float entertainmentExp;
    private float subscriptionExp;
    private float othersExp;
    private Activity thisActivity;

    public ExpenditureSum(float remainingBudget, float foodExp, float transportExp, float entertainmentExp, float subscriptionExp, float othersExp, Activity thisActivity) {
        this.remainingBudget = remainingBudget;
        this.foodExp = foodExp;
        this.transportExp = transportExp;
        this.entertainmentExp = entertainmentExp;
        this.subscriptionExp = subscriptionExp;
        this.othersExp = othersExp;
        this.thisActivity = thisActivity;
    }

    public float getRemainingBudget() {
        return remainingBudget;
    }

    public List<PieEntry> getPieEntries() {
        List<PieEntry> entries = new ArrayList<PieEntry>();
        if (foodExp != 0f) entries.add(new PieEntry(foodExp, ExpType.Food.name(), ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.food)));
        if (transportExp != 0f) entries.add(new PieEntry(transportExp, ExpType.Transport.name(), ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.transport)));
        if (entertainmentExp != 0f) entries.add(new PieEntry(entertainmentExp, ExpType.Entertainment.name(), ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.entertainment)));
        if (subscriptionExp != 0f) entries.add(new PieEntry(subscriptionExp, ExpType.Subscription.name(), ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.subscription)));
        if (othersExp != 0f) entries.add(new PieEntry(othersExp, ExpType.Others.name(), ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.others)));
        return entries;
    }

}
