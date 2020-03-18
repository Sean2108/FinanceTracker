package com.sean.financialtracker.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by sean on 27/8/17.
 */

@Entity(tableName = "tracker")
public class Expenditure {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "cost")
    private float cost;
    @ColumnInfo(name = "exp_type")
    private String type;
    @ColumnInfo(name = "description")
    private String desc;
    @ColumnInfo(name = "exp_date")
    private String date;

    public Expenditure(Integer id, float cost, String type, String desc, String date) {
        this.id = id;
        this.cost = cost;
        this.type = type;
        this.desc = desc;
        this.date = date;
    }

    public Integer getId() {
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

    public String getDateReadable(Date now) {
        String resultDate = "";
        try {
            Date formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
            long millisSinceExp = now.getTime() - formattedDate.getTime();
            long minSinceExp = TimeUnit.MILLISECONDS.toMinutes(millisSinceExp);
            long hoursSinceExp = TimeUnit.MILLISECONDS.toHours(millisSinceExp);
            if (minSinceExp < 60l) {
                if (minSinceExp == 1) resultDate = String.valueOf(minSinceExp) + " minute ago";
                else resultDate = String.valueOf(minSinceExp) + " minutes ago";
            }
            else if (hoursSinceExp < 24l) {
                if (hoursSinceExp == 1) resultDate = String.valueOf(hoursSinceExp) + " hour ago";
                else resultDate = String.valueOf(hoursSinceExp) + " hours ago";
            }
            else resultDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Expenditure)) {
            return false;
        }
        Expenditure other = (Expenditure) obj;
        return super.equals(obj) && id == other.id && cost == other.cost && type == other.type && desc == other.desc && date == other.date;
    }
}
