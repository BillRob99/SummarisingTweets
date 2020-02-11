package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.User;

public class ViewSet extends AppCompatActivity {
    private Twitter twitter;
    private AccountSet set;
    private AccountAdapter adapter;
    private final int ADD_ACCOUNTS_REQUEST_CODE = 1;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_set);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("View Set");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            twitter = (Twitter) extras.get("Twitter");
            set = (AccountSet) extras.get("Set");
        }

        TextView titleTxt = findViewById(R.id.setTitleTxt);
        titleTxt.setText(set.getName());

        updateNumberOfAccounts();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AccountAdapter(this, set.getAccountList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACCOUNTS_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Bundle bundle = data.getExtras();

                set = (AccountSet) bundle.get("Set");
                writeSetInfo();
                recyclerView = findViewById(R.id.recyclerView);
                adapter = new AccountAdapter(this, set.getAccountList());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

                updateNumberOfAccounts();

            }
        }
    }

    public void addAccounts(View v) {
        Intent intent = new Intent(this, AddAccounts.class);
        intent.putExtra("Twitter", twitter);
        intent.putExtra("Set", set);
        startActivityForResult(intent, ADD_ACCOUNTS_REQUEST_CODE);

    }

    public void deleteAccount(int position) {
        User account = set.getAccountList().get(position);
        set.removeAccount(account);
        recyclerView.getAdapter().notifyDataSetChanged();
        updateNumberOfAccounts();
        writeSetInfo();
    }

    private void updateNumberOfAccounts() {
        TextView noOfAccountsTxt = findViewById(R.id.noOfAccountsTxt);
        noOfAccountsTxt.setText("Number of accounts: " + set.getNoOfAccounts());
    }

    private void writeSetInfo() {
        SharedPreferences sharedPref = getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String data = "";
        ArrayList<User> accountList = set.getAccountList();

        for(int i = 0; i < set.getNoOfAccounts() - 1; i++) {
            data += accountList.get(i).getId() + System.lineSeparator();
        }
        data += accountList.get(set.getNoOfAccounts() - 1).getId();

        editor.putString(set.getName(), data);
        editor.commit();
    }

}
