package org.teamhonda.trackapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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
}
