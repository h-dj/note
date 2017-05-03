package com.example.h_dj.note.ui.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class DailyFragment extends BaseFragment {

    private final String TAG = "DailyFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private MyAdapter mMyAdapter;
    private List<String> mStrings;

    @Override
    public int getLayouId() {
        return R.layout.fragment_daily;
    }

    @Override
    public void init() {
        super.init();
        mStrings = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            mStrings.add("测试数据" + i);
        }
        initRecyclerView();
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        Log.e(TAG, "initRecyclerView: ");
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //设置下划线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //设置适配器
        mMyAdapter = new MyAdapter(mContext, R.layout.item_main, mStrings);
        mRecyclerView.setAdapter(mMyAdapter);
    }


}
