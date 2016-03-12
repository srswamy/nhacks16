package com.nhacks.share.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nhacks.share.R;

/**
 * Created by SagarkumarDave on 10/19/2015.
 */
public class SampleFragment extends Fragment {
    TextView textView;

    public static SampleFragment getInstance(int position) {
        SampleFragment sampleFragment = new SampleFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        sampleFragment.setArguments(args);
        return sampleFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.sample_fragment, container, false);
        textView = (TextView) layout.findViewById(R.id.tv);
        Bundle bundle = getArguments();

        if (bundle != null) {
            textView.setText("The page selected is " + bundle.getInt("position") + 1);
        }

        return layout;
    }
}
