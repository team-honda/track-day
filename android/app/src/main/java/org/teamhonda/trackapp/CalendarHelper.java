package org.teamhonda.trackapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class CalendarHelper {
    public List<Void> getCalendarData(Context context, Date from, Date to) {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"), (new String[] { "_id", "displayName", "selected"}), null, null, null);

        HashSet<String> calendarIds = new HashSet<>();

        System.out.println("Count="+cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String _id = cursor.getString(0);
                String displayName = cursor.getString(1);
                Boolean selected = !cursor.getString(2).equals("0");
                calendarIds.add(_id);
            }
        }

        // For each calendar, display all the events from the previous week to the end of next week.
        for (String id : calendarIds) {
            Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
            ContentUris.appendId(builder, from.getTime());
            ContentUris.appendId(builder, to.getTime());

            Cursor eventCursor = contentResolver.query(builder.build(), new String[]  { "title", "begin", "end", "allDay"}, "Calendars._id=" + id, null, "startDay ASC, startMinute ASC");

            if (eventCursor != null && eventCursor.getCount() > 0 && eventCursor.moveToFirst()) {
                do {
                    final String title = eventCursor.getString(0);
                    final Date begin = new Date(eventCursor.getLong(1));
                    final Date end = new Date(eventCursor.getLong(2));
                    final Boolean allDay = !eventCursor.getString(3).equals("0");

                    Pattern p = Pattern.compile(" ");
                    String[] items = p.split(begin.toString());

                    String scalendar_metting_beginday = items[0];
                    String scalendar_metting_beginmonth = items[1];
                    String scalendar_metting_begindate = items[2];
                    String scalendar_metting_begintime = items[3];
                    String scalendar_metting_begingmt = items[4];
                    String scalendar_metting_beginyear = items[5];

                    String calendar_metting_beginday = scalendar_metting_beginday;
                    String calendar_metting_beginmonth = scalendar_metting_beginmonth.toString().trim();

                    int calendar_metting_begindate = Integer.parseInt(scalendar_metting_begindate.trim());

                    String calendar_metting_begintime = scalendar_metting_begintime.toString().trim();
                    String calendar_metting_begingmt = scalendar_metting_begingmt;
                    int calendar_metting_beginyear = Integer.parseInt(scalendar_metting_beginyear.trim());

                    Pattern p1 = Pattern.compile(" ");
                    String[] enditems = p.split(end.toString());

                    String scalendar_metting_endday = enditems[0];
                    String scalendar_metting_endmonth = enditems[1];
                    String scalendar_metting_enddate = enditems[2];
                    String scalendar_metting_endtime = enditems[3];
                    String scalendar_metting_endgmt = enditems[4];
                    String scalendar_metting_endyear = enditems[5];

                    String calendar_metting_endday = scalendar_metting_endday;
                    String calendar_metting_endmonth = scalendar_metting_endmonth.toString().trim();

                    int calendar_metting_enddate = Integer.parseInt(scalendar_metting_enddate.trim());

                    String calendar_metting_endtime = scalendar_metting_endtime.toString().trim();
                    String calendar_metting_endgmt = scalendar_metting_endgmt;
                    int calendar_metting_endyear = Integer.parseInt(scalendar_metting_endyear.trim());

                    int beg_date = begin.getDate();
                    String mbeg_date = begin.getDate()+"/"+calendar_metting_beginmonth+"/"+calendar_metting_beginyear;
                    int beg_time = begin.getHours();

                    int end_date = end.getDate();
                    int end_time = end.getHours();

                    System.out.println(beg_date + " " + mbeg_date + " " + calendar_metting_begintime + " " + end_date + " " + end_time + " " + calendar_metting_endtime);
                }
                while(eventCursor.moveToNext());
            }
        }
        return Collections.emptyList();
    }
}
