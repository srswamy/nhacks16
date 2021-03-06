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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nhacks.share.Adapters.RecyclerViewAdapter;
import com.nhacks.share.AddBookForRentActivity;
import com.nhacks.share.Network.NetworkRequestBuilder;
import com.nhacks.share.Network.NetworkRequestManager;
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
 * Created by Sagar on 3/13/2016.
 */
public class BorrowedBooksFragment extends Fragment {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<RecyclerViewRow> mData;
    private List<RecyclerViewRow> initialData;
    private FloatingActionButton mFloatingButton;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String userId;
    String[] categories;

    public static BorrowedBooksFragment getInstance(int position) {
        BorrowedBooksFragment myFragment = new BorrowedBooksFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.l_borrowed_fragment, container, false);
        Bundle bundle = getArguments();
        NetworkRequestManager.init(getContext());
        setHasOptionsMenu(true);
        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        userId = sharedpreferences.getString("user_id", "");
        categories = getResources().getStringArray(R.array.categories);

        mData = getData();
        initialData = new ArrayList<>();
        NetworkRequestManager.init(getContext());

        adapter = new RecyclerViewAdapter(getActivity(), mData, getFragmentManager());
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public List<RecyclerViewRow> getData() {

        List<RecyclerViewRow> data = getBorrowedBooksResponse("http://52.37.205.141:3001/api/v1/users/" + userId +"/borrowed");

        return data;
    }

    public List<RecyclerViewRow> getBorrowedBooksResponse(String url) {
        final List<RecyclerViewRow> data = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String t = "";
                JSONObject obj;
                try {
                    obj = new JSONObject(response);
                    String available_books = obj.getString("books");
                    JSONArray jArray = new JSONArray(available_books);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject object = new JSONObject(jArray.get(i).toString());
                        //JSONObject bookObject = new JSONObject(object.getString("book"));
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String category_id = object.getString("category_id");
                        String edition = object.getString("edition");
                        RecyclerViewRow current = new RecyclerViewRow();
                        current.setEdition(edition);
                        current.setTitle(name);
                        current.setCategory(categories[Integer.valueOf(category_id)-1]);
                        //current.iconId = icons[i % 3];

                        data.add(current);
                        initialData.add(current);
                    }
                    adapter.update(data);
                }  catch (JSONException e) {
                    e.printStackTrace();
                }

                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String g = "";
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
        return data;
    }

    private List<RecyclerViewRow> getDataFromJson(JSONArray courseArr) throws JSONException {

        List<RecyclerViewRow> recyclerViewRowList = new ArrayList<>();

        for(int i = 0; i < courseArr.length(); i++){
            JSONObject courseObj = courseArr.getJSONObject(i);
            Gson gson = new Gson();
            RecyclerViewRow course;
            course = gson.fromJson(String.valueOf(courseObj), RecyclerViewRow.class);

            recyclerViewRowList.add(course);
        }

        return recyclerViewRowList;
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
