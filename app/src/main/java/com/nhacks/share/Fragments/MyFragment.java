package com.nhacks.share.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhacks.share.Adapters.RecyclerViewAdapter;
import com.nhacks.share.Objects.RecyclerViewRow;
import com.nhacks.share.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 10/17/2015.
 */
public class MyFragment extends Fragment {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    public static MyFragment getInstance(int position){
        MyFragment myFragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        Bundle bundle = getArguments();

        adapter = new RecyclerViewAdapter(getActivity(), getData());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public static List<RecyclerViewRow> getData(){
        List<RecyclerViewRow> data = new ArrayList();
        int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        String[] titles = {"One", "Two", "Three", "Four"};

        for(int i = 0; i < titles.length * 3; i++){
            RecyclerViewRow current = new RecyclerViewRow();
            current.title = titles[i%3];
            current.iconId = icons[i%3];

            data.add(current);
        }

        return data;
    }

}
