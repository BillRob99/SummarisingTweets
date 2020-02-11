package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import twitter4j.Twitter;

/**
 * An Activity that allows a user to create a new set.
 * @author William Roberts
 * @version 1.0
 */
public class CreateSet extends AppCompatActivity {
    private ArrayList<AccountSet> sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Your Sets");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            sets = (ArrayList<AccountSet>) extras.get("Sets");
        }
    }

    /**
     * Inflates the toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_layout, menu);
        return true;
    }

    /**
     * Called when the Create button is clicked.
     * Creates a new set and adds it to the ArrayList.
     */
    public void createSet(View v) {
        EditText editText = findViewById(R.id.setNameTxt);
        String name = editText.getText().toString();

        if(checkIfNameExists(name)) {
            Toast.makeText(this, "That set name already exists!" +
                    " Please choose another." , Toast.LENGTH_LONG).show();

        } else if(name.equals("")) {
            Toast.makeText(this, "Please enter a name into the box.",
                    Toast.LENGTH_LONG).show();
        } else {
            //Creates the new set.
            AccountSet newSet = new AccountSet(name);
            sets.add(newSet);
            updateSetsData();
            //Create intent to return to ViewSets activity.
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Sets", sets);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }
    }

    /**
     * A method for checking whether the set name already exists in the ArrayList.
     * @param name The name that is going to be checked.
     * @return Boolean value of true if the name does already exist, false otherwise.
     */
    private boolean checkIfNameExists(String name) {
        for(int i = 0; i < sets.size(); i++) {
            if(sets.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Updates Shared Preferences with the new list of sets.
     */
    public void updateSetsData() {
        SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String data = "";
        for(int i = 0; i < sets.size() - 1; i++) {
            data += sets.get(i).getName() + System.lineSeparator();
        }
        data += sets.get(sets.size() - 1).getName();

        editor.putString("Sets", data);
        editor.apply();
    }
}
