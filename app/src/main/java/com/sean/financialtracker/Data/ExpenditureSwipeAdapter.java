package com.sean.financialtracker.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.sean.financialtracker.R;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sean on 29/8/17.
 */

public class ExpenditureSwipeAdapter extends ArrayAdapter<Expenditure> {

    private final LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper;
    private NumberFormat formatter;
    private OnDataChangeListener mOnDataChangeListener;
    private DBHandler db;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public ExpenditureSwipeAdapter(Context context, List<Expenditure> objects, DBHandler db) {
        super(context, R.layout.item_expenditure, objects);
        mInflater = LayoutInflater.from(context);
        binderHelper = new ViewBinderHelper();
        formatter = NumberFormat.getCurrencyInstance();
        this.db = db;

        // uncomment if you want to open only one row at a time
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

            // Get the data item for this position
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expenditure, parent, false);
                holder = new ViewHolder();
                holder.desc = (TextView) convertView.findViewById(R.id.desc);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.date = (TextView) convertView.findViewById(R.id.submit_date);
                holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
                holder.swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_view);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

        // Populate the data into the template view using the data object
        final Expenditure expenditure = getItem(position);
        if (expenditure != null) {
            binderHelper.bind(holder.swipeLayout, String.valueOf(expenditure.getId()));
            holder.desc.setText(expenditure.getDesc());
            holder.price.setText(formatter.format(expenditure.getCost()));
            holder.date.setText(expenditure.getDateReadable(new Date()));
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteExp(expenditure);
                                Toast.makeText(getContext(), "Expenditure deleted!", Toast.LENGTH_SHORT).show();
                                remove(expenditure);
                                notifyDataSetChanged();
                                if(mOnDataChangeListener != null){
                                    mOnDataChangeListener.onDataChanged();
                                }
                            }
                        });

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });

            // Return the completed view to render on screen
            return convertView;
        }


    private class ViewHolder {
        TextView desc;
        TextView price;
        TextView date;
        ImageButton delete;
        SwipeRevealLayout swipeLayout;
    }

    public interface OnDataChangeListener{
        public void onDataChanged();
    }
}
