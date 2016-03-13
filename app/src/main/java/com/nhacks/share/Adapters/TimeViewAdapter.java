package com.nhacks.share.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhacks.share.Fragments.AddBookDetailsFragment;
import com.nhacks.share.R;

import java.util.List;

/**
 * Created by Sagar on 3/12/2016.
 */
public class TimeViewAdapter extends RecyclerView.Adapter<TimeViewAdapter.MyViewHolder> {
    List<String> timeData;
    private LayoutInflater inflater;
    Context context;
    public TimeViewAdapter(FragmentActivity activity, List<String> timeData) {
        super();
        this.timeData = timeData;
        this.context = activity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.l_time_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void updateList(List<String> timeData){
        this.timeData.clear();
        this.timeData = timeData;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String current = timeData.get(position);
        holder.tv.setText(current);
    }

    @Override
    public int getItemCount() {
        return timeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.name);
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AddBookDetailsFragment myFragment = new AddBookDetailsFragment();

            Bundle args = new Bundle();
            args.putString("category", timeData.get(getPosition()));
            myFragment.setArguments(args);
        }
    }
}