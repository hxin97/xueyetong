package com.example.xueyetong;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xyt.entity.Activity_info;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<Activity_info> mActivityList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title;
        TextView date;
        TextView site;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            title = cardView.findViewById(R.id.activity_title);
            date = cardView.findViewById(R.id.activity_date);
            site = cardView.findViewById(R.id.activity_site);
        }
    }

    public ActivityAdapter(ArrayList<Activity_info> activityList){
        mActivityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Activity_info activity_info = mActivityList.get(i);
        viewHolder.title.setText(activity_info.getTitle());
        viewHolder.site.setText(activity_info.getSite());
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }


}
