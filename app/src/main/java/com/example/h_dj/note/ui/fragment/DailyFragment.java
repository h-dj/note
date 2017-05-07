package com.example.h_dj.note.ui.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.MyAdapter;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.DailyPresenter;
import com.example.h_dj.note.presenter.Impl.DailyPresenterImpl;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.view.DailyFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class DailyFragment extends BaseFragment implements DailyFragmentView {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private MyAdapter mMyAdapter;//适配器
    private List<Note> mNotes;

    //P层引用
    private DailyPresenter mPresenter;

    @Override
    public int getLayouId() {
        return R.layout.fragment_daily;
    }

    @Override
    public void init() {
        super.init();
        mNotes = new ArrayList<>();
        mPresenter = new DailyPresenterImpl(mContext, this);
        mMyAdapter = new MyAdapter(mContext, R.layout.item_main, mNotes);
        loadData();
        initRecyclerView();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        LogUtil.e("DailyFragment：加载数据");
        mPresenter.loadDailyData();
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        LogUtil.e("initRecyclerView");
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //设置下划线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //设置适配器
        mRecyclerView.setAdapter(mMyAdapter);
    }


    @Override
    public void loadDataSuccess(List<Note> notes) {
        LogUtil.e(notes.toString());
        mNotes.addAll(notes);
        mMyAdapter.notifyItemRangeInserted(0, mNotes.size());
    }

    @Override
    public void failed() {
        Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
    }
}
