package com.nhacks.share.Adapters;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nhacks.share.Fragments.AllBooksFragment;
import com.nhacks.share.Fragments.MyBooksFragment;
import com.nhacks.share.Fragments.MyFragment;
import com.nhacks.share.Fragments.SampleFragment;
import com.nhacks.share.Fragments.SlidingTabsFragment;
import com.nhacks.share.Objects.DrawerRow;
import com.nhacks.share.R;

import java.util.List;

/**
 * Created by Sagar on 3/12/2016.
 */
public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.MyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

   List<DrawerRow> rows;
    private String name;
    private int profile;
    private String email;
    FragmentManager fManager;
    ActionBar aBar;
    String[] names;
    DrawerLayout dLayout;

    public DrawerRecyclerViewAdapter(List<DrawerRow> rows, String Name, String Email, int Profile, FragmentManager fragmentManager,ActionBar  aBar, String[] names, DrawerLayout drawerLayout) {
        this.rows = rows;
        name = Name;
        email = Email;
        profile = Profile;
        this.fManager = fragmentManager;
        this.aBar = aBar;
        this.names = names;
        dLayout = drawerLayout;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.name);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                Holderid = 1;
                itemView.setOnClickListener(this);

            } else {

                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.image_view);
                Holderid = 0;
            }
        }

        public void updateFragment(int pos) {

            Fragment newFragment;
            newFragment = AllBooksFragment.getInstance(0);
            switch(pos){

                case 0:
                    newFragment = AllBooksFragment.getInstance(0);
                    break;
                case 1:
                    newFragment = MyBooksFragment.getInstance(0);
                    break;
                case 2:
                    newFragment = AllBooksFragment.getInstance(0);
                    break;
            }
            aBar.setTitle(names[pos-2]);

            fManager.beginTransaction()
                    .replace(R.id.fragment, newFragment)
                    .commit();
        }

        @Override
        public void onClick(View v) {
            dLayout.closeDrawers();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                        /* Replace fragment content */
                    updateFragment(getPosition() + 1);
                }
            }, 200);
        }
    }

    @Override
    public DrawerRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);

            MyViewHolder vhItem = new MyViewHolder(v, viewType);

            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);

            MyViewHolder vhHeader = new MyViewHolder(v, viewType);

            return vhHeader;


        }
        return null;

    }

    @Override
    public void onBindViewHolder(DrawerRecyclerViewAdapter.MyViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(rows.get(position - 1).name);
            holder.imageView.setImageResource(rows.get(position - 1).iconId);
        } else {

            holder.Name.setText(name);
            holder.email.setText(email);
            String c = String.valueOf(name.charAt(0));
            TextDrawable drawable1 = TextDrawable.builder()
                    .buildRound(c, Color.GRAY); // radius in px
            holder.profile.setImageDrawable(drawable1);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
