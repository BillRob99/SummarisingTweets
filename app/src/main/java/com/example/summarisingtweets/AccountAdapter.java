package com.example.summarisingtweets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import twitter4j.User;

/**
 * This class is used as an adapter to insert multiple sets of accounts into a RecyclerView.
 *
 * @author William Roberts
 * @version 1.0
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<User> accounts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    AccountAdapter(Context context, List<User> accounts) {
        this.mInflater = LayoutInflater.from(context);
        this.accounts = accounts;
        this.mContext = context;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.account_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int POSITION) {
        String profilePicURL = accounts.get(POSITION).getOriginalProfileImageURL();
        new DownloadProfilePicTask(holder.profileImage).execute(profilePicURL);
        holder.nameTxt.setText(accounts.get(POSITION).getScreenName());

        if(mContext instanceof ViewSet) {
            holder.button.setImageResource(R.mipmap.delete);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ViewSet)mContext).deleteAccount(POSITION);
                }
            });

        } else if(mContext instanceof AddAccounts) {
            holder.button.setImageResource(R.mipmap.add);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddAccounts)mContext).addAccount(POSITION);
                }
            });
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return accounts.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTxt;
        ImageButton button;

        ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profilePicture);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            button = itemView.findViewById(R.id.button);
        }
    }


    // convenience method for getting data at click position
    String getItem(int id) {
        return accounts.get(id).getScreenName();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {

        void onEditClick(View view, int position);

        void onDeleteClick(View view, int position);
    }
}
