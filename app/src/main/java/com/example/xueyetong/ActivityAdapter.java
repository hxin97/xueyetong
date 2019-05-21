package com.example.xueyetong;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xyt.entity.Activity_info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<Activity_info> mActivityList;

    //普通布局
    private final int TYPE_ITEM = 0;

    //脚布局
    private final int TYPE_FOOTER = 1;

    //当前加载状态，默认为加载完成
    private int loadState = 1;

    //正在加载
    public final int LOADING = 0;

    //加载完成
    public final int LOADING_COMPLETE = 1;

    //加载到底
    public final int LOADING_END = 2;

    private class RecyclerViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title;
        TextView date;
        TextView site;

        public RecyclerViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            title = cardView.findViewById(R.id.activity_title);
            date = cardView.findViewById(R.id.activity_date);
            site = cardView.findViewById(R.id.activity_site);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder{
        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        public FootViewHolder(View view){
            super(view);
            pbLoading = view.findViewById(R.id.pb_loading);
            tvLoading = view.findViewById(R.id.tv_loading);
            llEnd = view.findViewById(R.id.ll_end);
        }
    }

    public ActivityAdapter(ArrayList<Activity_info> activityList){
        mActivityList = activityList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }

        if (i == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_item,viewGroup,false);
            return new RecyclerViewHolder(view);
        } else if (i == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_item,viewGroup,false);
            return new FootViewHolder(view);
        }

        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
            Activity_info activity_info = mActivityList.get(i);
            recyclerViewHolder.title.setText(activity_info.getTitle());
            recyclerViewHolder.date.setText((activity_info.getStartTime().substring(5,16)));
            recyclerViewHolder.site.setText(activity_info.getSite());
        } else if (viewHolder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            switch (loadState) {
                case LOADING:  //  正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE:  //  加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END:  //加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mActivityList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个item设置为FooterView
        if (position + 1 == getItemCount()){
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * 设置上划到底加载状态
     *
     * @param  loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState){
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
