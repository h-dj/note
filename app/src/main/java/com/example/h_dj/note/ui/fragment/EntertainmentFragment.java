package com.example.h_dj.note.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.MyAdapter;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.view.OnEditListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class EntertainmentFragment extends BaseFragment implements OnEditListener{

    private final static String filterWord = "娱乐";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private List<Note> mList;
    private MyAdapter mMyAdapter;//适配器


    @Override
    public int getLayoutId() {
        return R.layout.fragment_;
    }

    @Override
    public void init() {
        super.init();
        mList = new ArrayList<>();
        mMyAdapter = new MyAdapter(mContext, R.layout.item_main, mList,this);
        initRecyclerView(mRecyclerView, mMyAdapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        RefreshData();
    }

    /**
     * 刷新数据
     */
    private void RefreshData() {
        mList.clear();
        mList.addAll(filterData(mNotes, filterWord));
        mMyAdapter.notifyDataSetChanged();
    }


    @Override
    public void delete(int position) {

    }

    @Override
    public void edit(int position) {

    }
}
