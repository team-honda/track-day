package org.teamhonda.trackapp;

import android.Manifest.permission;
import android.content.pm.PackageManager;
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

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_CALENDAR = 1;

    private CalendarMonthView view;

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

        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.green));
        view.setBackgroundDrawableForDate(blue, Calendar.getInstance().getTime());


        setContentView(R.layout.activity_main);
        initializeCalendar();
    }

    public void initializeCalendar() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission.READ_CALENDAR)) {
                Log.e(getPackageName(), "Must show reationale for requesting permission");
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission.READ_CALENDAR},
                        PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        }
        else {
            fetchUserCalendarData();
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
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, 15);
                Date end = cal.getTime();
                cal.set(Calendar.MONTH, month - 2);
                Date start = cal.getTime();

                ServerAdapter b = new ServerAdapter();
                b.getEventData(start, end);

                Toast.makeText(getApplicationContext(), start.toString() + " - " + end.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchUserCalendarData();

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

    private void fetchUserCalendarData() {
        Log.i(this.getPackageName(), "Fetching user calendar data");

        int year = view.getYear();
        int month = view.getMonth();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        Date end = cal.getTime();
        cal.set(Calendar.MONTH, month - 2);
        Date start = cal.getTime();

        UserCalendarHelper c = new UserCalendarHelper();
        c.getCalendarData(getBaseContext(), start, end);
    }
}
