package com.example.sagar.materialdesigntest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sagar.materialdesigntest.R;
import com.example.sagar.materialdesigntest.Objects.RecyclerViewRow;

import java.util.Collections;
import java.util.List;

/**
 * Created by SagarkumarDave on 10/19/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<RecyclerViewRow> data = Collections.emptyList();
    Context context;

    public RecyclerViewAdapter(Context context, List<RecyclerViewRow> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.recycler_view_row, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        RecyclerViewRow current = data.get(i);
        myViewHolder.tv.setText(current.title);
        myViewHolder.iv.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        ImageView iv;
        ImageView remove;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //On entire row click listener
                }
            });

            tv = (TextView) itemView.findViewById(R.id.textView);
            iv = (ImageView) itemView.findViewById(R.id.image);
            remove = (ImageView) itemView.findViewById(R.id.remove_img);
            remove.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            delete(getPosition());
        }
    }
}
