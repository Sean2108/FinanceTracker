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

    public void setRemainingBudget(float remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public float getFoodExp() {
        return foodExp;
    }

    public void setFoodExp(float foodExp) {
        this.foodExp = foodExp;
    }

    public float getTransportExp() {
        return transportExp;
    }

    public void setTransportExp(float transportExp) {
        this.transportExp = transportExp;
    }

    public float getEntertainmentExp() {
        return entertainmentExp;
    }

    public void setEntertainmentExp(float entertainmentExp) {
        this.entertainmentExp = entertainmentExp;
    }

    public float getSubscriptionExp() {
        return subscriptionExp;
    }

    public void setSubscriptionExp(float subscriptionExp) {
        this.subscriptionExp = subscriptionExp;
    }

    public float getOthersExp() {
        return othersExp;
    }

    public void setOthersExp(float othersExp) {
        this.othersExp = othersExp;
    }

    public List<PieEntry> getPieEntries() {
        List<PieEntry> entries = new ArrayList<PieEntry>();
        if (foodExp != 0f) entries.add(new PieEntry(foodExp, "Food", ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.food)));
        if (transportExp != 0f) entries.add(new PieEntry(transportExp, "Transport", ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.transport)));
        if (entertainmentExp != 0f) entries.add(new PieEntry(entertainmentExp, "Entertainment", ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.entertainment)));
        if (subscriptionExp != 0f) entries.add(new PieEntry(subscriptionExp, "Subscription", ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.subscription)));
        if (othersExp != 0f) entries.add(new PieEntry(othersExp, "Others", ContextCompat.getDrawable(thisActivity.getApplicationContext(),R.drawable.others)));
        return entries;
    }

}
