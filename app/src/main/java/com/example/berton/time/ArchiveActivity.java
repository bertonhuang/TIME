package com.example.berton.time;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArchiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Archive");

        LinearLayout layout = (LinearLayout) findViewById(R.id.archivecontainer);
        SQLiteDatabase mydb = openOrCreateDatabase("sugar_example.db", MODE_PRIVATE, null);
        Cursor crs = mydb.rawQuery("SELECT * FROM EItem;", null);
        if (crs.moveToFirst()) {
            while (crs.isAfterLast() == false) {
                String start = crs.getString(4);
                String name = crs.getString(3);
                String desc = crs.getString(1);
                String finish = crs.getString(2);
                int id = crs.getInt(0);
                Eitem eItem = Eitem.findById(Eitem.class, id);
                createItems(name, desc, start, finish, layout, eItem);
                crs.moveToNext();
            }
        }
    }
    public void createItems(String name, String desc, String start, final String finishdate, final LinearLayout parent, final Eitem eItems) {
        //Create variables for the views
        final View item = getLayoutInflater().inflate(R.layout.item, null);
        //Setting id for the view

        TextView nameView = (TextView) item.findViewById(R.id.item_nameView);
        TextView descView = (TextView) item.findViewById(R.id.item_descView);
        TextView finishView = (TextView) item.findViewById(R.id.item_finishView);
        //Settings the text for the views
        nameView.setText(name);
        if (desc == "Description") {
            descView.setVisibility(View.GONE);
        } else {
            descView.setText(desc);
        }
        finishView.setText(finishdate);
        //Check if the start button has been pressed
        parent.addView(item);
        final Button startButton = (Button) item.findViewById(R.id.item_startButton);
        final TextView startTime = (TextView) item.findViewById(R.id.item_startTimeText);
        if (start != null) {
            startButton.setVisibility(View.GONE);
            startTime.setText(eItems.start);
        } else {
            startButton.setVisibility(View.VISIBLE);
        }
        ImageView delete = (ImageView) item.findViewById(R.id.item_deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removes the item from the screen and database
                parent.removeView(item);
                eItems.delete();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, TimeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
