package com.example.h_dj.note.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.h_dj.note.App;
import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.MyMenuAdapter;
import com.example.h_dj.note.bean.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by H_DJ on 2017/5/4.
 */

public class SearchResultActivity<E> extends BaseActivity implements MyMenuAdapter.OnItemClickListener {


    private static final int START_SEARCH = 1;//开始搜索
    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.et_query_text)
    EditText mEtQueryText;
    @BindView(R.id.delete)
    ImageView mDelete;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private MyMenuAdapter mMyMenuAdapter;
    private List<Note> mNotes;
    private App mApp;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_SEARCH:
                    mNotes.clear();
                    String filterWord = (String) msg.obj;
                    mNotes.addAll(filterData(mApp.getNoteList(), filterWord));
                    mMyMenuAdapter.notifyDataSetChanged();
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {
        super.init();
        mApp = (App) this.getApplication();
        setEdit();
        initRecycleView();

    }


    /**
     * 初始化recyclerView
     */
    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mNotes = new ArrayList<>();
        mMyMenuAdapter = new MyMenuAdapter(mNotes);
        mMyMenuAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mMyMenuAdapter);
    }

    /**
     * 设置搜索框监听事件
     */
    private void setEdit() {
        mEtQueryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtil.e(s + ":" + start + ":" + count + ":" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtil.e(s + ":" + start + ":" + count + ":" + before);
                /**
                 * 判断清除按钮是否显示
                 */
                if (!TextUtils.isEmpty(s)) {
                    mDelete.setVisibility(View.VISIBLE);
                } else {
                    mDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                LogUtil.e(s.toString());
                /**
                 * 发消息更新数据
                 */
                Message msg = Message.obtain();
                msg.what = START_SEARCH;
                msg.obj = s.toString().trim().toLowerCase();
                mHandler.sendMessage(msg);
            }
        });
    }

    @OnClick({R.id.back, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.delete:
                mEtQueryText.setText("");
                break;
        }
    }


    @Override
    public void OnClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Note", mNotes.get(position));
        bundle.putInt("position", position);
        goTo(ModifyDataActivity.class, bundle);
    }

    @Override
    public void OnLongClick(View view, int position) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        hiddenSoftwareInput(mEtQueryText);
    }
}
