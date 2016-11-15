package org.teamhonda.trackapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserEventHelper {
    public List<UserEventData> getCalendarData(Context context, Date from, Date to) {
        Log.i(UserEventHelper.class.getName(), "Fetching user calendar data");

        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] { CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.VISIBLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Events.EVENT_LOCATION };
        String selection = "((" + CalendarContract.Events.DTSTART + " >= " + from.getTime() + ") AND (" + CalendarContract.Events.DTEND + " <= " + to.getTime() + "))";

        List<UserEventData> data = new ArrayList<>();

        try (Cursor cursor = contentResolver.query(Uri.parse(CalendarContract.Events.CONTENT_URI.toString()), projection, selection, null,null)) {
            while (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                boolean isVisible = cursor.getInt(3) == 1 && id != 3;
                if (isVisible) {
                    data.add(new UserEventData(title, cursor.getString(2), new Date(cursor.getLong(4)), new Date(cursor.getLong(5)), cursor.getString(6), cursor.getString(7)));
                }
            }
        }

        return data;
    }
}
