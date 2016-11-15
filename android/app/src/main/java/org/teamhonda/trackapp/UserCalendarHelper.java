package org.teamhonda.trackapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCalendarHelper {
    public List<UserCalendarData> getCalendarData(Context context, Date from, Date to) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] { CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Events.EVENT_LOCATION };
        String selection = "((" + CalendarContract.Events.DTSTART + " >= " + from.getTime() + ") AND (" + CalendarContract.Events.DTEND + " <= " + to.getTime() + "))";

        List<UserCalendarData> data = new ArrayList<>();

        try (Cursor cursor = contentResolver.query(Uri.parse(CalendarContract.Events.CONTENT_URI.toString()), projection, selection, null,null)) {
            while (cursor != null && cursor.moveToNext()) {
                data.add(new UserCalendarData(cursor.getString(0), cursor.getString(1), new Date(cursor.getLong(2)), new Date(cursor.getLong(3)), cursor.getString(4), cursor.getString(5)));
            }
        }

        return data;
    }
}
