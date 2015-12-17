package com.example.sagar.materialdesigntest;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sagar.materialdesigntest.Fragments.MyFragment;
import com.example.sagar.materialdesigntest.Fragments.NavigationDrawerFragment;
import com.example.sagar.materialdesigntest.Fragments.SampleFragment;
import com.example.sagar.materialdesigntest.Fragments.SlidingTabsFragment;
import com.example.sagar.materialdesigntest.Objects.DrawerRow;
import com.example.sagar.materialdesigntest.ui.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private DrawerListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        names = getResources().getStringArray(R.array.nav_items);
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        mDrawerList = (ListView) findViewById(R.id.drawerList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mAdapter = new DrawerListAdapter(this, R.layout.drawer_row, getData());

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

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
               /* Closing the drawer */
                mDrawerLayout.closeDrawer(mDrawerList);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        /* Replace fragment content */
                        updateFragment(pos);
                    }
                }, 200);
            }
        });

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, SlidingTabsFragment.getInstance(0))
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
                current.iconId = R.mipmap.ic_launcher;
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
