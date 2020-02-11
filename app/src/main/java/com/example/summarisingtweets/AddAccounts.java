package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * This class is used to add Twitter accounts to a set.
 *
 * @author William Roberts
 * @version 1.0
 */
public class AddAccounts extends AppCompatActivity {
    private Twitter twitter;
    private AccountSet set;
    private final ArrayList<User> FRIENDS_LIST = new ArrayList<>();
    private final long CURSOR = -1;
    private RecyclerView recyclerView;
    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accounts);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Add Accounts");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            twitter = (Twitter) extras.get("Twitter");
            set = (AccountSet) extras.get("Set");
        }

        fillFriendsList();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AccountAdapter(this, FRIENDS_LIST);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL));
    }

    /**
     * Method for adding an account to the set.
     * Called when the user presses the add button on an account.
     */
    public void addAccount(int position) {
        User account = FRIENDS_LIST.get(position);
        set.addAccount(account);
        FRIENDS_LIST.remove(position);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Called when the user clicks the 'done' button.
     * Returns to the previous activity, and adds the AccountSet as an extra.
     */
    public void finishActivity(View view) {
        //Create intent to return to ViewSet activity.
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Set", set);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Fills the 'FRIENDS_LIST' with accounts that the user follows,
     * but that are not already contained in the set.
     */
    private void fillFriendsList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Returns a list of accounts the user follows.
                    PagableResponseList<User> friends = twitter.getFriendsList
                            (twitter.getId(), CURSOR);
                    //Loops through, adds any account not in the set to FRIENDS_LIST
                    for(int i = 0;i < friends.size(); i++) {
                        if(!set.checkIfContains(friends.get(i))) {
                            FRIENDS_LIST.add(friends.get(i));
                        }
                    }
                } catch(TwitterException e) {
                    Log.i("Twitter Exception", e.toString());
                }

            }
        });

        thread.start();
        try{
            thread.join();
        } catch(InterruptedException e) {
            Log.i("Exeption", e.toString());
        }
    }
}
