package com.example.h_dj.note.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.h_dj.note.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by H_DJ on 2017/5/5.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {


    private TextView date;
    private TextView time;
    private TextView tv_title;//标题
    private TextView tv_msg;//信息
    private TextView tv_cancel;//取消按钮
    private TextView tv_ok;//确认按钮

    private String title;//标题内容
    private String msg; //信息内容
    private String ok;//确认按钮的文本
    private String cancel;//取消按钮的文本
    private OnOkClickListener listener;
    private OnNoClickListener noListener;
    private OnContentViewClickListener mOnContentViewClickListener;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog); //设置对话框的样式
        setCanceledOnTouchOutside(false);
        initView();//findView
        initData();//初始化数据;设置标题或信息
        initEvent();//初始化按钮的点击事件
        setCurrentTime();
    }

    /**
     * 初始化按钮的点击事件
     */
    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);

    }

    /**
     * 设置当前时间
     */
    private void setCurrentTime() {
        //希望格式化时间为12小时制的，则使用hh:mm:ss 如果希望格式化时间为24小时制的，则使用HH:mm:ss
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String[] split = currentTime.split("\\s");
        date.setText(split[0] + "");
        time.setText(split[1] + "");

    }

    /**
     * 设置OK按钮的接口
     *
     * @param str
     * @param listener
     */
    public CustomDialog setOkClickListener(String str, OnOkClickListener listener) {
        if (str != null) {
            ok = str;
        }
        this.listener = listener;
        return this;
    }

    /**
     * 设置cancel按钮的接口
     *
     * @param str
     * @param listener
     * @return
     */
    public CustomDialog setNoClickListener(String str, OnNoClickListener listener) {
        if (str != null) {
            cancel = str;
        }
        this.noListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if (listener != null) {
                    String dateResult = date.getText().toString().trim();
                    String timeResult = time.getText().toString().trim();
                    listener.click(v, dateResult, timeResult);
                }
                break;
            case R.id.tv_cancel:
                if (noListener != null) {
                    noListener.click(v);
                }
                break;
            case R.id.date:
            case R.id.time:
                if (mOnContentViewClickListener != null) {
                    mOnContentViewClickListener.onClick(v);
                }
                break;
        }
    }

    public interface OnOkClickListener {
        void click(View v, String date, String time);
    }

    /**
     *
     */
    public interface OnNoClickListener {
        void click(View v);
    }

    public CustomDialog setContentViewClickListener(OnContentViewClickListener viewClickListener) {
        mOnContentViewClickListener = viewClickListener;
        return this;
    }

    /**
     * 内容点击接口
     */
    public interface OnContentViewClickListener {
        void onClick(View view);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //设置标题
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }

        //设置信息
        if (!TextUtils.isEmpty(msg)) {
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(msg);
        }
        //设置取消按钮文本
        if (!TextUtils.isEmpty(cancel)) {
            tv_cancel.setText(cancel);
        }

        //设置确认按钮文本
        if (!TextUtils.isEmpty(ok)) {
            tv_ok.setText(ok);
        }


    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
    }

    /**
     * 暴露给外界设置标题
     *
     * @param title
     * @return
     */
    public CustomDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomDialog setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
