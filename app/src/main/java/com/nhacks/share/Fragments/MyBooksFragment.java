package com.nhacks.share.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nhacks.share.Adapters.MyBooksRecyclerAdapter;
import com.nhacks.share.AddBookForRentActivity;
import com.nhacks.share.Network.NetworkRequestManager;
import com.nhacks.share.Objects.MyBooksRecyclerRow;
import com.nhacks.share.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sathvik on 3/13/2016.
 */
public class MyBooksFragment extends Fragment {

    private MyBooksRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private List<MyBooksRecyclerRow> mData;
    private List<MyBooksRecyclerRow> initialData;
    private CircularProgressView progressView;
    private TextView emptyTextView;

    private String name = "";
    private String email = "";
    private String facebookId = "";
    private String userId = "";

    public static final String API_PREFIX = "http://52.37.205.141:3001";
    private static final String MY_PREFS_NAME = "MyPrefsFile";

    // TODO: DELETE ME ONCE THE RESOURCE LIST IS MADE
    String[] categories = {"Engineering", "Science", "Accounting", "Arts", "Environment"};

    public static MyBooksFragment getInstance(int position) {
        MyBooksFragment fragment = new MyBooksFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_books, container, false);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        name = sharedpreferences.getString("name_key", "");
        email = sharedpreferences.getString("email_key", "");
        facebookId = sharedpreferences.getString("id_key", "");
        userId = sharedpreferences.getString("user_id", "");

        progressView = (CircularProgressView) layout.findViewById(R.id.progress_view);
        progressView.startAnimation();
        progressView.setVisibility(View.VISIBLE);

        emptyTextView = (TextView) layout.findViewById(R.id.my_books_empty);

        setHasOptionsMenu(true);

        // Set up mData
        mData = new ArrayList<>();

        getData();

        initialData = mData;
        NetworkRequestManager.init(getContext());

        adapter = new MyBooksRecyclerAdapter(getActivity(), mData);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public void getData() {
        String requestUrl = API_PREFIX + "/api/v1/users/" + userId + "/lent_books/";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, requestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getDataFromJson(response);
                            adapter.updateData(mData);
                            adapter.notifyDataSetChanged();
                            progressView.setVisibility(View.GONE);

                            if (mData.isEmpty()) {
                                emptyTextView.setText("You don't have any books out for rental. Post one in the Home page!");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Oops, looks like we ran into an issue!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                public void onErrorResponse(VolleyError error) {
                        progressView.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Oops, ran into an error while grabbing some data!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(getRequest);
    }

    private void getDataFromJson(JSONObject borrowedBooks) throws JSONException {

        mData = new ArrayList<>();

        JSONArray jsonArr = borrowedBooks.getJSONArray("books");

        for(int i = 0; i < jsonArr.length(); i++){
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            int currentStatus = jsonObj.getInt("current_status");
            JSONObject bookObj = jsonObj.getJSONObject("book");

            MyBooksRecyclerRow row = new MyBooksRecyclerRow(bookObj.getString("name"), categories[bookObj.getInt("category_id")-1], currentStatus);

            mData.add(row);
        }

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
            final List<MyBooksRecyclerRow> filteredModelList = filter(initialData, query);
            adapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
            return true;
        }

        private List<MyBooksRecyclerRow> filter(List<MyBooksRecyclerRow> models, String query) {
            query = query.toLowerCase();

            final List<MyBooksRecyclerRow> filteredModelList = new ArrayList<>();
            for (MyBooksRecyclerRow model : models) {
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
