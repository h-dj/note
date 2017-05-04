package com.example.h_dj.note.ui.activity;

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

import com.example.h_dj.note.R;
import com.example.h_dj.note.adapter.SearchAdapter;
import com.example.h_dj.note.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by H_DJ on 2017/5/4.
 */

public class SearchResultActivity<E> extends BaseActivity {


    private static final int START_SEARCH = 1;//开始搜索
    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.et_query_text)
    EditText mEtQueryText;
    @BindView(R.id.delete)
    ImageView mDelete;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private SearchAdapter mAdapter;
    private List<String> mStringList;
    private List<String> mStrings;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_SEARCH:
                    mStrings.clear();
                    String filterWord = (String) msg.obj;
                    mStrings.addAll(filterData(mStringList, filterWord));
                    LogUtil.e("mStrings.size()" + mStrings.size());
                    mAdapter.notifyDataSetChanged();
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
        setEdit();
        initRecycleView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mStringList = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            mStringList.add("测试数据" + i);
        }
    }

    /**
     * 初始化recyclerView
     */
    private void initRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStrings = new ArrayList<>();
        mAdapter = new SearchAdapter(this, R.layout.item_content, mStrings);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 设置搜索框监听事件
     */
    private void setEdit() {
        mEtQueryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.e(s + ":" + start + ":" + count + ":" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.e(s + ":" + start + ":" + count + ":" + before);
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
                LogUtil.e(s.toString());
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


    /**
     * 筛选数据
     *
     * @param datas
     * @param filterWord
     * @return
     */
    public List<String> filterData(List<String> datas, String filterWord) {

        List<String> filterList = new ArrayList<>();
        if (!TextUtils.isEmpty(filterWord)) {
            ListIterator<String> iterator = datas.listIterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (next.contains(filterWord)) {
                    filterList.add(next);
                    LogUtil.e("filterList.size()" + filterList.size());
                }
            }
        }

        return filterList;
    }

}
