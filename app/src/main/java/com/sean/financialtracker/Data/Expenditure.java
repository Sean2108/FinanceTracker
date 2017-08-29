package com.sean.financialtracker.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sean on 27/8/17.
 */

public class Expenditure {
    private int id;
    private float cost;
    private String type;
    private String desc;
    private String date;

    public Expenditure(int id, float cost, String type, String desc, String date) {
        this.id = id;
        this.cost = cost;
        this.type = type;
        this.desc = desc;
        this.date = date;
    }

    public Expenditure() {
        this.type = "Others";
        this.desc = "";
        this.date = "";
    }

    public int getId() {
        return id;
    }

    public float getCost() { return cost; }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getDateReadable() {
        String resultDate = "";
        try {
            Date formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
            resultDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCost(float cost) { this.cost = cost; }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
