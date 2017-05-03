package com.example.h_dj.note.adapter;

import android.content.Context;

import com.example.h_dj.note.R;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class MyAdapter extends BaseRecycleViewAdapter {


    public MyAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(MyViewHolder holder, Object o) {
        holder.setText(R.id.item_title, (String) o);
    }
}
