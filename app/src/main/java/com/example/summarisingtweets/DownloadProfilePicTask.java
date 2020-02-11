package com.example.summarisingtweets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * This class is used when a profile picture needs to be downloaded in the background
 * and assigned to an ImageView. Performed as an AsyncTask to ensure less frame drops.
 *
 * @author William Roberts
 * @version 1.0
 */
public class DownloadProfilePicTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageHolder;

    /**
     * The constructor for the class.
     * @param imageHolder The ImageView that is going to be changed.
     */
    public DownloadProfilePicTask(ImageView imageHolder) {

        this.imageHolder = imageHolder;
    }

    /**
     * The main task of the class. The Image is downloaded here.
     * @param urls The URL that the Image is downloaded from.
     * @return Returns a bitmap of the Image that has been downloaded.
     */
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap profilePic = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            profilePic = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.e("Error", e.getMessage());
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return profilePic;
    }

    /**
     * Once the image has been downloaded, set it to the ImageView.
     * @param result The Bitmap of the downloaded Image.
     */
    protected void onPostExecute(Bitmap result) {
        imageHolder.setImageBitmap(result);
    }
}
