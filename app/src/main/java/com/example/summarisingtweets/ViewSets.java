package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class ViewSets extends AppCompatActivity {
    private Twitter twitter;
    private SetAdapter adapter;
    private ArrayList<AccountSet> sets = new ArrayList<>();
    private final int CREATE_SET_REQUEST_CODE = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sets);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Your Sets");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            twitter = (Twitter) extras.get("Twitter");
        }


        makeSets();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SetAdapter(this, sets);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNew:
                Intent intent = new Intent(this, CreateSet.class);
                intent.putExtra("Sets", sets);
                startActivityForResult(intent, CREATE_SET_REQUEST_CODE);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_SET_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = data.getExtras();
                sets = (ArrayList<AccountSet>) bundle.get("Sets");

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                adapter = new SetAdapter(this, sets);
                recyclerView.setAdapter(adapter);

            }
        }
    }

    public void deleteSet(int position) {
        AccountSet tempSet = sets.get(position);
        sets.remove(position);
        deleteSetData(tempSet.getName());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void viewSet(int position) {
        AccountSet set = sets.get(position);
        Intent intent = new Intent(this, ViewSet.class);
        intent.putExtra("Set", set);
        intent.putExtra("Twitter", twitter);
        startActivity(intent);
    }

    private void makeSets() {
        SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        String data = sharedPref.getString("Sets", null);

        if(data != null) {
            String[] setNames = data.split(System.getProperty("line.separator"));
            sets = new ArrayList<>();

            for(int i = 0; i < setNames.length; i++) {
                sets.add(constructSet(setNames[i]));
            }
        }

    }

    /**
     * This method is for creating a set based off information stored in shared preferences.
     * @param setName The name of the set, as a String.
     * @return The constructed AccountSet.
     */
    private AccountSet constructSet(String setName) {
        final AccountSet set = new AccountSet(setName);
        SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        String data = sharedPref.getString(setName, null);

        if(data != null) {
            final String[] userIDs = data.split(System.getProperty("line.separator"));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        for(int i = 0; i < userIDs.length; i++) {
                            long ID = Long.parseLong(userIDs[i]);

                            set.addAccount(twitter.showUser(ID));
                        }

                    } catch(TwitterException e) {
                        Log.i("Twitter Exception", e.toString());
                    }
                }
            });
            try{
                thread.start();
                thread.join();
            } catch(InterruptedException e) {
                Log.i("Error", e.toString());
            }
        }
        return set;
    }

    /**
     * Method for deleting all stored data about a set. Should be called after
     * the set is already deleted from the set list.
     * @param setName The name of the set to have its stored data deleted.
     */
    private void deleteSetData(String setName) {
        SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String data = "";

        for(int i = 0; i < sets.size(); i++) {
            data = data + sets.get(i).getName() + System.lineSeparator();
        }

        editor.putString("Sets", data);
        editor.putString(setName, null);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeSets();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new SetAdapter(this, sets);
        recyclerView.setAdapter(adapter);
    }
}