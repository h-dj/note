package com.example.h_dj.note.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.h_dj.note.adapter.MyAdapter;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.ui.activity.ModifyDataActivity;
import com.example.h_dj.note.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by H_DJ on 2017/5/3.
 */

public abstract class BaseFragment extends Fragment {


    protected View rootView;
    protected Context mContext;
    private Unbinder unbinder;

    protected static List<Note> mNotes = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        rootView = LayoutInflater.from(mContext).inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;

    }

    public void init() {
        LogUtil.e("init()");
    }

    /**
     * 初始化recyclerView
     */
    protected void initRecyclerView(RecyclerView mRecyclerView, MyAdapter mMyAdapter) {
        LogUtil.e("initRecyclerView");
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        manager.supportsPredictiveItemAnimations();
        mRecyclerView.setLayoutManager(manager);
        //设置下划线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMyAdapter);
    }


    public abstract int getLayoutId();


    @Override
    public void onPause() {
        super.onPause();
        LogUtil.e("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.e("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e("onResume");
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }


    protected void goTo(Class mClass, int noteId) {
        Intent intent = new Intent(mContext, ModifyDataActivity.class);
        intent.putExtra("noteId", noteId);
        mContext.startActivity(intent);
    }

    /**
     * 筛选数据
     *
     * @param datas
     * @param filterWord
     * @return
     */
    public List<Note> filterData(List<Note> datas, String filterWord) {
        LogUtil.e("filterData");
        List<Note> filterList = new ArrayList<>();
        if (!TextUtils.isEmpty(filterWord)) {

            Iterator<Note> iterator = datas.iterator();
//            LogUtil.e("filterData2:"+iterator.hasNext());
            while (iterator.hasNext()) {
                Note next = iterator.next();
//                LogUtil.e("filterData3"+next.getNoteType());
                if (next.getNoteType().equals(filterWord)) {
                    filterList.add(next);
                }
            }
        }

        return filterList;
    }
}
