package org.teamhonda.trackapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;

import hirondelle.date4j.DateTime;

public class CalendarDayView extends CaldroidGridAdapter {
    public static String SCHEDULE = "schedule";

    private final Schedule schedule;

    public CalendarDayView(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        schedule = (Schedule) extraData.get(SCHEDULE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView != null ? convertView : inflater.inflate(R.layout.custom_cell, null);

        // Get dateTime of this cell
        DateTime dateTime = datetimeList.get(position);

        // Set color of the dates in previous / next month
        TextView dateField = (TextView) cellView.findViewById(R.id.tv1);
        dateField.setTextColor(dateTime.getMonth() != month ? Color.LTGRAY : Color.BLACK);

        boolean shouldResetDisabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            dateField.setTextColor(CaldroidFragment.disabledTextColor);
            cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable == -1 ? com.caldroid.R.drawable.disable_cell : CaldroidFragment.disabledBackgroundDrawable);

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }
        } else {
            shouldResetDisabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources.getColor(com.caldroid.R.color.caldroid_sky_blue));
            dateField.setTextColor(Color.BLACK);
        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDisabledView && shouldResetSelectedView) {
            // Customize for today
            cellView.setBackgroundResource(dateTime.equals(getToday()) ? com.caldroid.R.drawable.red_border : com.caldroid.R.drawable.cell_bg);
        }

        dateField.setText(String.format("%s", dateTime.getDay()));

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(cellView.getPaddingLeft(), cellView.getPaddingTop(), cellView.getPaddingRight(), cellView.getPaddingBottom());

        // Set custom color if required
        setCustomResources(dateTime, cellView, dateField);

        return cellView;
    }

}