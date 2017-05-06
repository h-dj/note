package com.example.h_dj.note.widgets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by H_DJ on 2017/5/5.
 */

public class PickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private Calendar calendar;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    private OnSetTimeListener mOnTimeSetListener;

    private TimePickerDialog mTimePicker;
    private DatePickerDialog mDatePicker;
    public final static int TIME_PICKER = 1; //时间选择器
    public final static int DATE_PICKER = 2; //日期选择器
    private int flag = 1; //标志是哪中选择器


    public PickerFragment setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);

        /**
         * 第一个参数为：上下文
         * 第二个参数为 ：OnTimeSetListener
         * 第三个参数为：小时hour
         * 第四个参数为： 分
         * 第五个参数为：是否显示为24小时制
         */
        mTimePicker = new TimePickerDialog(getContext(), this, hour, minute, true);
        /**
         * 第一个参数为：上下文
         * 第二个参数为 ：OnDateSetListener
         * 第三个参数为：year年
         * 第四个参数为： month月
         * 第五个参数为：day日
         */
        mDatePicker = new DatePickerDialog(getContext(), this, year, month, day);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onCreateDialog: " + hour + ":" + minute);
        switch (flag) {
            case TIME_PICKER:
                return mTimePicker;
            case DATE_PICKER:
                return mDatePicker;
            default:
                return mTimePicker;
        }
    }

    /**
     * 当选择完时间后会回掉
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i(this.getClass().getSimpleName(), "onDateSet: " + hourOfDay + ":" + minute);
        if (mOnTimeSetListener != null) {
            //解决下午或上午问题
            String A_PM = null;
            if (hourOfDay < 12) {
                A_PM = "AM";
            } else {
                A_PM = "PM";
            }

            mOnTimeSetListener.getTimeSet(hourOfDay, minute, "");
        }

    }

    /**
     * 设置接口
     *
     * @param listener
     * @return
     */
    public PickerFragment setOnTimeSetListener(OnSetTimeListener listener) {
        mOnTimeSetListener = listener;
        return this;
    }

    /**
     * 当选择完日期后回调
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i(this.getClass().getSimpleName(), "onDateSet: " + year + "/" + month + "/" + dayOfMonth);
        if (mOnTimeSetListener != null) {
            //月份是从0开始算的
            mOnTimeSetListener.getDateSet(view, year, month + 1, dayOfMonth);
        }

    }

    /**
     * 暴露数据给外界的接口
     */
    public interface OnSetTimeListener {
        void getTimeSet(int hourOfDay, int minute, String apm);

        void getDateSet(DatePicker view, int year, int month, int dayOfMonth);
    }
}
