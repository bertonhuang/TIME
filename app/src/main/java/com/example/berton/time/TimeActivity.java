package com.example.berton.time;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class TimeActivity extends AppCompatActivity{
    private static final int REQUEST_CODE = 12345;
    public static int active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        FloatingActionButton create = (FloatingActionButton) findViewById(R.id.button);
        assert create != null;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity.this,FirstCreateActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
            LinearLayout layout = (LinearLayout) findViewById(R.id.container);
            SQLiteDatabase mydb = openOrCreateDatabase("sugar_example.db",MODE_PRIVATE,null);
            Cursor crs = mydb.rawQuery("SELECT * FROM EItem;",null);
            if(crs.moveToFirst()) {
                while (crs.isAfterLast() == false) {
//                    int active = crs.getInt(4);
//                    if (active == 1)
                    String start = crs.getString(4);
                    String name = crs.getString(3);
                    String desc = crs.getString(1);
                    String finish = crs.getString(2);
                    int id = crs.getInt(0);
                    Eitem eItem = Eitem.findById(Eitem.class,id);
                    createItem(name,desc,start,finish,layout,eItem);
                    crs.moveToNext();
//                    }
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //Retrieves the data input by the user and displays it using a inflater.
            int day = data.getIntExtra("itemday",1);
            int month = data.getIntExtra("itemmonth",1);
            int year = data.getIntExtra("itemyear",2016);
            int hour = data.getIntExtra("itemhour",1);
            int minute = data.getIntExtra("itemminute",2016);
            String name = data.getStringExtra("itemname");
            String desc = data.getStringExtra("itemdesc");
            LinearLayout layout = (LinearLayout) findViewById(R.id.container);
            GregorianCalendar date = new GregorianCalendar(year,month,day,hour,minute);

//          Writes the data to the database.
            Eitem item = new Eitem (name,desc,null, String.valueOf(date.getTime()));
            item.save();
            createItem(name, desc,null,String.valueOf(date.getTime()), layout,item);

//            SQLiteDatabase mydb = openOrCreateDatabase("sugar_example.db",MODE_PRIVATE,null);
//            Cursor crs = mydb.rawQuery("SELECT * FROM EItem;",null);
//            crs.moveToFirst();
//            while (crs.isAfterLast()== false) {
//                String n1 = crs.getString(0);
//                String n2 = crs.getString(1);
//                String n3 = crs.getString(2);
//                Toast.makeText(this, n1 + n2 + n3, Toast.LENGTH_SHORT).show();
//                crs.moveToNext();
//            }
        }else{
            Toast.makeText(this, "Error: Could not return.", Toast.LENGTH_SHORT).show();

        }
    }

    public void createItem(String name, String desc, String start, final String finishdate, final LinearLayout parent, final Eitem eItems) { //GregorianCalendar date
        //Create variables for the views
        final View item =  getLayoutInflater().inflate(R.layout.item,null);
        //Setting id for the view

        TextView nameView = (TextView) item.findViewById(R.id.item_nameView);
        TextView descView = (TextView) item.findViewById(R.id.item_descView);
        TextView finishView = (TextView) item.findViewById(R.id.item_finishView);
        //Settings the text for the views
        nameView.setText(name);
        if (desc == "Description"){
            descView.setVisibility(View.GONE);
        }else{
            descView.setText(desc);
        }
        finishView.setText(finishdate);
        //Check if the start button has been pressed
        parent.addView(item);
        final Button startButton = (Button) item.findViewById(R.id.item_startButton);
        final TextView startTime = (TextView) item.findViewById(R.id.item_startTimeText);
        if (start != null){
            startButton.setVisibility(View.GONE);
            startTime.setText(eItems.start);
        }else{
            startButton.setVisibility(View.VISIBLE);
        }

        //Onclick for start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar currentTime = new GregorianCalendar();
                startTime.setText(String.valueOf(currentTime.getTime()));
                startButton.setVisibility(View.GONE);
                //Create alarm
                GregorianCalendar passedTime = null;
                try {
                    passedTime = convertToGregorianCalender(finishdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createAlarm(passedTime, eItems);
                //Update SQL database
                eItems.start = String.valueOf(currentTime.getTime());
                eItems.save();
            }
        });
        ImageView delete = (ImageView) item.findViewById(R.id.item_deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removes the item from the screen and cancels the alarm if one is active
                eItems.save();
                Intent alertIntent = new Intent(TimeActivity.this, AlarmReceiver.class);

                PendingIntent pIntent = PendingIntent.getBroadcast(TimeActivity.this,eItems.getId().intValue(),alertIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                parent.removeView(item);
            }
        });

    }

    public void createAlarm (GregorianCalendar date, Eitem eItem){
        //Launches an alarm that triggers on the user specified date and time
        Long alertTime = date.getTimeInMillis();
        Toast.makeText(this, String.valueOf(alertTime), Toast.LENGTH_SHORT).show();
        Intent alertIntent = new Intent(this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime, PendingIntent.getBroadcast(this,eItem.getId().intValue(),alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void clearDatabaseClick(View view) {
        Eitem.deleteAll(Eitem.class);
        }

    public GregorianCalendar convertToGregorianCalender(String date) throws ParseException {
        //Converts a string to GregorianCalender
        Log.d("test", date);
        Date dates = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(date);
        DateTime dateTime = new DateTime(dates);
        GregorianCalendar cal = dateTime.toGregorianCalendar();
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Makes a settings menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Launches the clicked item in the settings menu
        switch(item.getItemId()){
            case R.id.archive:
                Intent intent = new Intent(this, ArchiveActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}