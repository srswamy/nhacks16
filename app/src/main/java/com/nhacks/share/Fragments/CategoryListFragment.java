package com.nhacks.share.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhacks.share.Adapters.CategoryRecyclerViewAdapter;
import com.nhacks.share.AddCategoryDialog;
import com.nhacks.share.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sagar on 3/12/2016.
 */
public class CategoryListFragment extends Fragment {
    private CategoryRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingSaveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.category_list_fragment, container, false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Select Category");

        mFloatingSaveButton = (FloatingActionButton) layout.findViewById(R.id.floatingAddCategoryButton);
        final AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        mFloatingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryDialog.show(getFragmentManager(), "fragment_add_category");
            }
        });

        mAdapter = new CategoryRecyclerViewAdapter(getActivity(), getMuscleGroupData(), getFragmentManager().beginTransaction());
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.categoryListRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AddCategoryDialog.DialogCloseListener closeListener   = new AddCategoryDialog.DialogCloseListener() {

            @Override
            public void handleDialogClose() {
                mAdapter.updateList(getMuscleGroupData());
            }
        };

        addCategoryDialog.DismissListener(closeListener);

        return layout;
    }

    public List<String> getMuscleGroupData() {
        List<String> categories = new ArrayList<>();

        categories.add("Accounting");
        categories.add("Arts");
        categories.add("Engineering");
        categories.add("Science");
        categories.add("Environment");

        return categories;

    }
}
