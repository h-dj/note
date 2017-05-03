package com.example.h_dj.note.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by H_DJ on 2017/5/3.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Context mContext;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getContext();
        rootView = LayoutInflater.from(mContext).inflate(getLayouId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;

    }

    public void init() {

    }


    public abstract int getLayouId();

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
