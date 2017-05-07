package com.example.h_dj.note.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.MyAdapter;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.AllNotePresenter;
import com.example.h_dj.note.presenter.Impl.AllNotePresenterImpl;
import com.example.h_dj.note.ui.activity.ModifyDataActivity;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.view.AllNoteFragmentView;
import com.example.h_dj.note.view.OnEditListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by H_DJ on 2017/5/7.
 */
// TODO: 2017/5/7
public class AllNoteFragment extends BaseFragment implements AllNoteFragmentView, OnEditListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private MyAdapter mMyAdapter;//适配器
    //P层引用
    private AllNotePresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_;
    }


    @Override
    public void init() {
        mPresenter = new AllNotePresenterImpl(mContext, this);
        mMyAdapter = new MyAdapter(mContext, R.layout.item_main, mNotes, this);
        initRecyclerView(mRecyclerView, mMyAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e("DailyFragment：加载数据");
        mPresenter.loadDailyData();
    }

    @Override
    public void loadDataSuccess(List<Note> notes) {
//        LogUtil.e(notes.toString());
        mNotes.clear();
        mNotes.addAll(notes);
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void failed() {
        Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delSuccess() {
        mPresenter.loadDailyData();
    }


    @Override
    public void delete(int position) {
//        Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
        String noteId = mNotes.get(position).getNoteId();
        mPresenter.del(noteId);
    }

    @Override
    public void edit(int position) {
//        Toast.makeText(mContext, "修改", Toast.LENGTH_SHORT).show();
        String noteId = mNotes.get(position).getNoteId();
        goTo(ModifyDataActivity.class, Integer.parseInt(noteId));
    }

}
