package com.nhacks.share.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhacks.share.Objects.MyBooksRecyclerRow;
import com.nhacks.share.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SagarkumarDave on 10/19/2015.
 */
public class MyBooksRecyclerAdapter extends RecyclerView.Adapter<MyBooksRecyclerAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<MyBooksRecyclerRow> data = Collections.emptyList();
    Context context;

    public MyBooksRecyclerAdapter(Context context, List<MyBooksRecyclerRow> data) {
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
        MyBooksRecyclerRow current = data.get(i);
        myViewHolder.tv.setText(current.getTitle());
        myViewHolder.category.setText(current.getCategory());
        //myViewHolder.iv.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<MyBooksRecyclerRow> viewModels) {
        data.clear();
        data.addAll(viewModels);
        notifyDataSetChanged();
    }

    public MyBooksRecyclerRow removeItem(int position) {
        final MyBooksRecyclerRow model = data.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, MyBooksRecyclerRow model) {
        data.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final MyBooksRecyclerRow model = data.remove(fromPosition);
        data.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<MyBooksRecyclerRow> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<MyBooksRecyclerRow> newModels) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final MyBooksRecyclerRow model = data.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<MyBooksRecyclerRow> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final MyBooksRecyclerRow model = newModels.get(i);
            if (!data.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<MyBooksRecyclerRow> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final MyBooksRecyclerRow model = newModels.get(toPosition);
            final int fromPosition = data.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        TextView category;
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
            category = (TextView) itemView.findViewById(R.id.category);
            //iv = (ImageView) itemView.findViewById(R.id.image);
            remove = (ImageView) itemView.findViewById(R.id.remove_img);
            remove.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            delete(getPosition());
        }
    }
}
