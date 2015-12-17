package com.example.sagar.materialdesigntest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sagar.materialdesigntest.Objects.DrawerRow;

import java.util.List;

/**
 * Created by SagarkumarDave on 12/17/2015.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerRow> {
    Context context;
    List<DrawerRow> listItems;

    public DrawerListAdapter(Context context, int resource, List<DrawerRow> listItems) {
        super(context, resource);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_row, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.name);
        ImageView iv = (ImageView) convertView.findViewById(R.id.image);

        tv.setText(listItems.get(position).name);
        iv.setImageResource(listItems.get(position).iconId);

        return convertView;
    }

}

