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

/**
 * This class is used as an adapter to insert multiple sets of accounts into a RecyclerView.
 *
 * @author William Roberts
 * @version 1.0
 */
public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private List<AccountSet> sets;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
     SetAdapter(Context context, List<AccountSet> sets) {
        this.mInflater = LayoutInflater.from(context);
        this.sets = sets;
        this.mContext = context;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.set_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int POSITION) {
        String setName = sets.get(POSITION).getName();
        holder.txtSetName.setText(setName);
        int accountNo = sets.get(POSITION).getNoOfAccounts();
        holder.txtAccountNo.setText("Accounts: " + Integer.toString(accountNo));

        holder.imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof ViewSets) {
                    ((ViewSets)mContext).viewSet(POSITION);
                }
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof ViewSets) {
                    ((ViewSets)mContext).deleteSet(POSITION);
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return sets.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSetName;
        TextView txtAccountNo;
        ImageButton imageInfo;
        ImageButton imageDelete;

        ViewHolder(View itemView) {
            super(itemView);
            txtSetName = itemView.findViewById(R.id.textBoxSetName);
            txtAccountNo = itemView.findViewById(R.id.textBoxAccountNumber);
            imageInfo = itemView.findViewById(R.id.getInfo);
            imageInfo.setImageResource(R.mipmap.info);
            imageDelete = itemView.findViewById(R.id.deleteSet);
            imageDelete.setImageResource(R.mipmap.delete);


        }
    }


    // convenience method for getting data at click position
    String getItem(int id) {
        return sets.get(id).getName();
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
