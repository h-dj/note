package com.example.h_dj.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by H_DJ on 2017/5/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    protected static final int RECYCLE = 1; //垃圾箱
    protected static final int DAILY = 2; //日常便签
    protected static final int STUDY = 3;
    protected static final int ENTERTAINMENT = 4;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        init();
    }

    public void init() {
    }


    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();


    public BaseActivity() {
        super();
    }


    @Override
    protected void onDestroy() {
        LogUtil.e(this.getClass().getSimpleName() + " onDestroy");
        unbinder.unbind();
        super.onDestroy();
    }


    /**
     * activity之间的跳转
     *
     * @param mClass
     */
    public void goTo(Class mClass) {
        goTo(mClass, null);
    }

    /**
     * activity之间的跳转带数据
     *
     * @param mClass
     * @param data   数据
     */
    public void goTo(Class mClass, Bundle data) {
        goTo(mClass, data, false);
    }

    /**
     * activity之间的跳转带数据且finish
     *
     * @param mClass
     * @param data     数据
     * @param isFinish 是否finish
     */
    public void goTo(Class mClass, Bundle data, boolean isFinish) {
        Intent intent = new Intent(this, mClass);
        if (data != null) {
            intent.putExtra("data", data);
        }
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * @param datas   全部数据
     * @param daoType 筛选类别
     * @return
     */
    public List<Note> filterData(List<Note> datas, int daoType) {

        List<Note> filterList = new ArrayList<>();
        ListIterator<Note> iterator = datas.listIterator();
        while (iterator.hasNext()) {
            Note next = iterator.next();
            switch (daoType) {
                case DAILY:
                    if (next != null && next.getIsDel() == 0 && next.getNoteType().contains("日常")) {
                        filterList.add(next);
                    }
                    break;
                case STUDY:
                    if (next != null && next.getIsDel() == 0 && next.getNoteType().contains("学习")) {
                        filterList.add(next);
                    }
                    break;
                case ENTERTAINMENT:
                    if (next != null && next.getIsDel() == 0 && next.getNoteType().contains("娱乐")) {
                        filterList.add(next);
                    }
                    break;
                case RECYCLE:
                    if (next != null && next.getIsDel() == 1) {
                        filterList.add(next);
                    }
                    break;
                default:
                    if (next != null && next.getIsDel() == 0) {
                        filterList.add(next);
                    }
                    break;
            }
        }
        LogUtil.e("filterList.size()" + filterList.size());
        return filterList;
    }


    /**
     * 用于搜索
     *
     * @param datas
     * @param filterWord
     * @return
     */
    public List<Note> filterData(List<Note> datas, String filterWord) {

        List<Note> filterList = new ArrayList<>();
        if (!TextUtils.isEmpty(filterWord)) {
            ListIterator<Note> iterator = datas.listIterator();
            while (iterator.hasNext()) {
                Note next = iterator.next();
                if (next != null && next.getIsDel() == 0 && next.getNoteTitle().contains(filterWord)) {
                    filterList.add(next);
                }
            }
        }
//        LogUtil.e("filterList.size()" + filterList.size());
        return filterList;
    }

}
