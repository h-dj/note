package com.example.h_dj.note.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.h_dj.note.utils.LogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by H_DJ on 2017/5/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化方法
     */
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
    protected void onStart() {
        LogUtil.e(this.getClass().getSimpleName()  + "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        LogUtil.e(this.getClass().getSimpleName() + "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        LogUtil.w(this.getClass().getSimpleName()  + "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        LogUtil.e(this.getClass().getSimpleName()  + "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        LogUtil.e(this.getClass().getSimpleName()  + "onDestroy");
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
}
