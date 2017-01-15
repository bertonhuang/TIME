package com.example.berton.time;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.orm.query.Select;

public class FirstCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_create);
    }

    public void typeClick(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("type",view.getId());
        startActivity(intent);
        finish();
    }
}
