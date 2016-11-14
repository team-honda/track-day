package org.teamhonda.trackapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeCalendar();
    }

    public void initializeCalendar() {
        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

//        calendar.setEnabled(true);
        calendar.setFirstDayOfWeek(1);

        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                String date = day + "/" + month + "/" + year;
                Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
                TextView text = (TextView) findViewById(R.id.textView);
                text.setText(date);
            }
        });
    }
}