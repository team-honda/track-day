package org.teamhonda.trackapp;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_CALENDAR = 1;

    private boolean readCalendarPermissionGranted = false;
    private CalendarMonthView view;
    public static final SimpleDateFormat SDF = new SimpleDateFormat("MMdd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CalendarMonthView();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        view.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, view);
        t.commit();

        setContentView(R.layout.activity_main);
        initializeCalendar();
    }

    public void initializeCalendar() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission.READ_CALENDAR)) {
                Log.e(getPackageName(), "Must show rationale for requesting permission");
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission.READ_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }
        else {
            readCalendarPermissionGranted = true;
        }

        view.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                TextView text = (TextView) findViewById(R.id.textView);
                Toast.makeText(getApplicationContext(), "Date Changed", Toast.LENGTH_LONG).show();
                text.setText(date.toString());
            }

            @Override
            public void onChangeMonth(int month, int year) {
                // We display a 6-week calendar so make the window larger than just the current month
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, 15);
                Date end = cal.getTime();
                cal.set(Calendar.MONTH, month - 2);
                Date start = cal.getTime();

                Toast.makeText(getApplicationContext(), start + " - " + end, Toast.LENGTH_SHORT).show();

                List<? extends TimeBasedEvent> userEventData = readCalendarPermissionGranted ? new UserEventHelper().getCalendarData(getBaseContext(), start, end) : Collections.<TimeBasedEvent>emptyList();
                List<? extends TimeBasedEvent> trackEventData = new TrackServerAdapter().getEventData(start, end);

                Map<String, List<TimeBasedEvent>> userSchedule = new HashMap<>();
                Map<String, List<TimeBasedEvent>> trackSchedule = new HashMap<>();
                addEventsToCalendar(userEventData, getResources().getColor(R.color.caldroid_sky_blue), Collections.<String, List<TimeBasedEvent>>emptyMap(), userSchedule);
                addEventsToCalendar(trackEventData, getResources().getColor(R.color.green), userSchedule, trackSchedule);
            }

            private void addEventsToCalendar(List<? extends TimeBasedEvent> events, int clr, Map<String, List<TimeBasedEvent>> readSchedule, Map<String, List<TimeBasedEvent>> writeSchedule) {
                for (TimeBasedEvent event : events) {
                    Calendar cal2 = Calendar.getInstance();
                    Date curr = event.getStart();
                    cal2.setTime(curr);
                    while (curr.before(event.getEnd())) {
                        String date = SDF.format(curr);
                        List<TimeBasedEvent> dayEvents = writeSchedule.get(date);
                        if (dayEvents == null) {
                            dayEvents = new ArrayList<>();
                            writeSchedule.put(date, dayEvents);
                        }
                        dayEvents.add(event);
                        boolean conflict = readSchedule.containsKey(date);
                        view.setBackgroundDrawableForDate(new ColorDrawable(conflict ? Color.RED : clr), curr);
                        cal2.add(Calendar.DATE, 1);
                        curr = cal2.getTime();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readCalendarPermissionGranted = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
