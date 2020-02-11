package com.example.summarisingtweets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 * This class represents the dashboard of the application.
 *
 * @author William Roberts
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private final static String CONSUMER_KEY
            = "jB6Be6HR78KiAFtmkEL7xDJwh";
    private final static String CONSUMER_KEY_SECRET
            = "2HsL5LteVTO8wjBXyPY3OIACVVI0oQVc49eUB84tTw3Pnjo4bi";
    private final static String ACCESS_TOKEN
            = "2924767664-El4pq8XgMVUbPINojLnyZoAp6dWYj4tMXVQha9G";
    private final static String ACCESS_TOKEN_SECRET
            = "LJu8fa9IZ5inuOP4pc9CyekcoL0poR32tzfuawutdHRyk";
    private Twitter twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Dashboard");
        toolBar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolBar);

        final ImageView profileImageView = findViewById(R.id.profilePicture);

        //Creates a new Twitter instance.
        twitter = new TwitterFactory().getInstance();
        //Sets the consumer keys of the Twitter instance.
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        //Creates a new access token, and sets them to the Twitter instance.
        AccessToken oAuthAccessToken = new AccessToken(ACCESS_TOKEN,
                ACCESS_TOKEN_SECRET);
        twitter.setOAuthAccessToken(oAuthAccessToken);
        Log.i("Twitter", "Connected");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    long userID = twitter.getId();
                    User currentUser = twitter.showUser(userID);
                    String profilePicURL = currentUser.getOriginalProfileImageURL();
                    new DownloadProfilePicTask(profileImageView).execute(profilePicURL);
                } catch (TwitterException e) {
                    Log.e("Error", e.toString());
                }
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_toolbar_layout, menu);
        return true;
    }

    /**
     * This method nagivates to the 'ViewAccounts' activity.
     * @param view
     */
    public void viewAccounts(View view) {
        Intent intent = new Intent(this, ViewAccounts.class);
        startActivity(intent);
    }

    /**
     * This method navigates to the 'ViewSets' activity.
     * @param view
     */
    public void viewSets(View view) {
        Intent intent = new Intent(this, ViewSets.class);
        intent.putExtra("Twitter", twitter);
        startActivity(intent);
    }
}
