package com.nhacks.share.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nhacks.share.Adapters.RecyclerViewAdapter;
import com.nhacks.share.AddBookForRentActivity;
import com.nhacks.share.Objects.RecyclerViewRow;
import com.nhacks.share.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 3/12/2016.
 */
public class AllBooksFragment extends Fragment {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<RecyclerViewRow> mData;
    private List<RecyclerViewRow> initialData;
    private FloatingActionButton mFloatingButton;

    public static AllBooksFragment getInstance(int position) {
        AllBooksFragment myFragment = new AllBooksFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);
        mData = getData();
        initialData = getData();
        adapter = new RecyclerViewAdapter(getActivity(), mData);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFloatingButton = (FloatingActionButton) layout.findViewById(R.id.floatingAddBookToRentButton);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddBookForRentActivity.class);

                startActivity(intent);
            }
        });

        return layout;
    }

    public static List<RecyclerViewRow> getData() {
        List<RecyclerViewRow> data = new ArrayList();
        //int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        String[] books = {"Algorithms and Data Structures", "Communication Systems", "Three", "Four", "Five", "Six", "Seven"};
        String[] categories = {"Engineering", "Engineering", "Arts", "Accounting", "Arts", "Accounting", "Arts"};

        for (int i = 0; i < books.length; i++) {
            RecyclerViewRow current = new RecyclerViewRow();
            current.setTitle(books[i]);
            current.setCategory(categories[i]);
            //current.iconId = icons[i % 3];

            data.add(current);
        }

        return data;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(searchListener);
    }

    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String query) {
            final List<RecyclerViewRow> filteredModelList = filter(initialData, query);
            adapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
            return true;
        }

        private List<RecyclerViewRow> filter(List<RecyclerViewRow> models, String query) {
            query = query.toLowerCase();

            final List<RecyclerViewRow> filteredModelList = new ArrayList<>();
            for (RecyclerViewRow model : models) {
                final String text = model.getTitle().toLowerCase();
                final String category = model.getCategory().toLowerCase();

                if (text.contains(query) || category.contains(query)) {
                    filteredModelList.add(model);
                }
            }
            return filteredModelList;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };


}
