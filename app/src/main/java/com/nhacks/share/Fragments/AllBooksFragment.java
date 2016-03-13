package com.nhacks.share.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.nhacks.share.Adapters.MyBooksRecyclerAdapter;
import com.nhacks.share.Adapters.RecyclerViewAdapter;
import com.nhacks.share.AddBookForRentActivity;
import com.nhacks.share.Network.NetworkRequestManager;
import com.nhacks.share.Objects.MyBooksRecyclerRow;
import com.nhacks.share.Objects.RecyclerViewRow;
import com.nhacks.share.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sagar on 3/12/2016.
 */
public class AllBooksFragment extends Fragment {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<RecyclerViewRow> mData;
    private List<RecyclerViewRow> initialData;
    private FloatingActionButton mFloatingButton;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String userId;
    String[] categories;
    CircularProgressView progressView;
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

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
        mData = new ArrayList<>();
        initialData = new ArrayList<>();
        progressView = (CircularProgressView) layout.findViewById(R.id.progress_view);
        progressView.startAnimation();
        progressView.setVisibility(View.VISIBLE);
        NetworkRequestManager.init(getContext());

        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        categories = getResources().getStringArray(R.array.categories);
        //userId = sharedpreferences.getString("user_id", "");
        userId = "10";
        getData();
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

    public void getData() {
        final List<RecyclerViewRow> data = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.GET, "http://52.37.205.141:3001/api/v1/books/available?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String t = "";
                JSONObject obj;
                try {
                    obj = new JSONObject(response);
                    String available_books = obj.getString("available_books");
                    JSONArray jArray = new JSONArray(available_books);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = new JSONObject(jArray.get(i).toString());
                        JSONObject bookObject = new JSONObject(object.getString("book"));
                        String user_book_id = object.getString("user_book_id");
                        String id = bookObject.getString("id");
                        String name = bookObject.getString("name");
                        String category_id = bookObject.getString("category_id");
                        String edition = bookObject.getString("edition");
                        RecyclerViewRow current = new RecyclerViewRow();
                        current.setEdition(edition);
                        current.setUserBookId(user_book_id);
                        current.setTitle(name);
                        current.setCategory(categories[Integer.valueOf(category_id)-1]);
                        //current.iconId = icons[i % 3];

                        data.add(current);
                        initialData.add(current);
                    }
                    progressView.setVisibility(View.GONE);
                    adapter.update(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String g = "";
                progressView.setVisibility(View.GONE);
                //mPostCommentResponse.requestEndedWithError(error);

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(myReq);
    }

    private List<MyBooksRecyclerRow> getDataFromJson(JSONArray courseArr) throws JSONException {

        List<MyBooksRecyclerRow> myBooksRecyclerRowList = new ArrayList<>();

        for (int i = 0; i < courseArr.length(); i++) {
            JSONObject courseObj = courseArr.getJSONObject(i);
            Gson gson = new Gson();
            MyBooksRecyclerRow course;
            course = gson.fromJson(String.valueOf(courseObj), MyBooksRecyclerRow.class);

            myBooksRecyclerRowList.add(course);
        }

        return myBooksRecyclerRowList;
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
