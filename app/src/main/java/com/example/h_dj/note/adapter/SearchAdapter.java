package com.example.h_dj.note.adapter;

import android.content.Context;

import com.example.h_dj.note.R;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/4.
 */

public class SearchAdapter extends BaseRecycleViewAdapter {


    public SearchAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(MyViewHolder holder, Object o,int position) {
        holder.setText(R.id.item_title, (String) o);
    }
}
