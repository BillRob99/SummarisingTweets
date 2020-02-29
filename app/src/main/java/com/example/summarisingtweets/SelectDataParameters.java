package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.DatePicker;

import java.util.Calendar;

import twitter4j.Twitter;

public class SelectDataParameters extends AppCompatActivity {
    private Twitter twitter;
    private AccountSet set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data_parameters);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Parameters");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            twitter = (Twitter) extras.get("Twitter");
            set = (AccountSet) extras.get("Set");
        }

        DatePicker datePicker = findViewById(R.id.datePicker);
        
        //datePicker.setMaxDate(Calendar.getInstance().getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_layout, menu);
        return true;
    }
}
