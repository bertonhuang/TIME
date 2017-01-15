package com.example.berton.time;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

public class CreateActivity extends AppCompatActivity {
    public static int newyear;
    public static int newmonth;
    public static int newday;
    public static int newhour;
    public static int newminute;
    public static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        JodaTimeAndroid.init(this);
        DateTime dt = new DateTime();
        //Set default date
        TextView date = (TextView) findViewById(R.id.selectedDate);
        TextView time = (TextView) findViewById(R.id.selectedTime);
        String month = String.valueOf(dt.getMonthOfYear());
        String year = String.valueOf(dt.getYear());
        String day = String.valueOf(dt.getDayOfMonth());
        date.setText(month+"/" +day+ "/"+year);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createClick);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClick(v);
            }
        });
        Intent i1 = getIntent();
        String type = i1.getStringExtra("type");
    }


    public void createClick(View view) {
        TextView name = (TextView) findViewById(R.id.nameEdit);
        TextView desc = (TextView) findViewById(R.id.descEdit);
        String itemname = name.getText().toString();
        String itemdesc = desc.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("itemname", itemname);
        intent.putExtra("itemdesc", itemdesc);
        intent.putExtra("itemyear",newyear );
        intent.putExtra("itemmonth",newmonth );
        intent.putExtra("itemday",newday);
        intent.putExtra("itemhour",newhour );
        intent.putExtra("itemminute",newminute );
        setResult(RESULT_OK,intent);
        finish();

//        GregorianCalendar calender = new GregorianCalendar();
//        String cal2 = String.valueOf(calender.getTime());
//        Toast.makeText(this,cal2,Toast.LENGTH_SHORT).show();


//        TimePicker time = (TimePicker) findViewById(R.id.timePicker);
//        int hour = time.getHour();
//        int minute = time.getMinute();

    }

    public void setTimeClick(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(),"timePicker");
    }

    public void setDateClick(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }
}
