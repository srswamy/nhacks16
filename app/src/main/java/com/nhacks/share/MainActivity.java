package com.nhacks.share;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nhacks.share.Adapters.DrawerRecyclerViewAdapter;
import com.nhacks.share.Adapters.RecyclerViewAdapter;
import com.nhacks.share.Fragments.AllBooksFragment;
import com.nhacks.share.Fragments.MyFragment;
import com.nhacks.share.Fragments.SampleFragment;
import com.nhacks.share.Fragments.SlidingTabsFragment;
import com.nhacks.share.Network.NetworkRequestBuilder;
import com.nhacks.share.Network.NetworkRequestManager;
import com.nhacks.share.Objects.DrawerRow;
import com.nhacks.share.ui.SlidingTabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    RecyclerView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerRecyclerViewAdapter mAdapter;
    DrawerLayout Drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] names;
    String name = "";
    String email = "";
    String facebookId = "";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        names = getResources().getStringArray(R.array.nav_items);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            facebookId = extras.getString("user_facebook_id");
            email = extras.getString("user_email");
            name = extras.getString("user_name");

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest myReq = new StringRequest(Request.Method.POST,"http://52.37.205.141:3000/api/v1/users", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String t = "";
                    //mPostCommentResponse.requestCompleted();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String g = "";
                    //mPostCommentResponse.requestEndedWithError(error);
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("facebook_id", facebookId);
                    params.put("school", "University of Waterloo");
                    params.put("email", email);
                    params.put("location", "Waterloo");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(myReq);
        }
        else{
            name = sharedpreferences.getString("name_key", "");
            email = sharedpreferences.getString("email_key", "");
            facebookId = sharedpreferences.getString("id_key", "");
        }


        mDrawerList = (RecyclerView) findViewById(R.id.drawerList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mAdapter = new DrawerRecyclerViewAdapter(getData(), name, email, 0, getSupportFragmentManager(), getSupportActionBar(), names, mDrawerLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);

        mDrawerList.setAdapter(mAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, AllBooksFragment.getInstance(0))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter{
        String[] tabNames;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabNames = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return MyFragment.getInstance(i);
            }
            return SampleFragment.getInstance(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public List<DrawerRow> getData(){
        List<DrawerRow> data = new ArrayList();


        for(int i = 0; i < names.length; i++){
            DrawerRow current = new DrawerRow();
            current.name = names[i];
            if(current.name.equals("Home")){
                current.iconId =R.mipmap.ic_launcher;
            }
            else{
                current.iconId = R.mipmap.ic_launcher;
            }
            data.add(current);
        }

        return data;
    }

    public void updateFragment(int pos) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newFragment = SampleFragment.getInstance(0);
        newFragment = SlidingTabsFragment.getInstance(0);
        switch(pos){

            case 0:
                newFragment = SlidingTabsFragment.getInstance(0);
                break;
            case 1:
                newFragment = MyFragment.getInstance(0);
                break;
            case 2:
                newFragment = MyFragment.getInstance(0);
                break;
            case 3:
                newFragment = MyFragment.getInstance(0);
                break;
            case 4:
                newFragment = SampleFragment.getInstance(0);
                break;
        }

        getSupportActionBar().setTitle(names[pos]);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment, newFragment)
                .commit();
    }
}
