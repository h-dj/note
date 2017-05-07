package com.example.h_dj.note.adapter;

import android.content.Context;
import android.view.View;

import com.example.h_dj.note.R;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.view.OnEditListener;
import com.example.h_dj.note.widgets.SlideItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class MyAdapter extends BaseRecycleViewAdapter<Note> implements SlideItem.OnItemCloseListener {


    private List<SlideItem> mSlideDeleteViews;
    private OnEditListener mOnEditListener;

    public MyAdapter(Context context, int layoutId, List<Note> datas, OnEditListener listener) {
        super(context, layoutId, datas);
        mSlideDeleteViews = new ArrayList<>();
        mOnEditListener = listener;
    }

    @Override
    protected void convert(final MyViewHolder holder, final Note note, final int position) {
        //item的点击事件
        holder.getView(R.id.item_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEditListener.edit(position);
            }
        });
        //设置标题
        holder.setText(R.id.item_title, note.getNoteTitle());
        //设置修改时间
        holder.setText(R.id.item_modifyTime, note.getModifyTime());
        //设置提醒时间
        if (note.isAlarm()) {
            holder.getView(R.id.item_alarmClock).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.item_alarmClock).setVisibility(View.GONE);
        }
        //设置添加星标点击事件
        holder.getView(R.id.item_mark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMark = !note.isMark();
                note.setMark(isMark);
                v.setBackgroundResource(note.isMark() ? R.drawable.mark_select : R.drawable.mark);
                LogUtil.e("onClick");
            }
        });

        //设置关闭事件
        ((SlideItem) holder.mConveryView).setOnItemCloseListener(this);

        //设置侧滑menu的单击事件
        ((SlideItem) holder.mConveryView).setOnItemMenuClickListener(new SlideItem.OnItemMenuClickListener() {
            @Override
            public void onClick(View view) {
                closeToAll();
                switch (view.getId()) {
                    case R.id.tv_del:
//                        Toast.makeText(mContext, "删除按钮被单击", Toast.LENGTH_SHORT).show();
                        mOnEditListener.delete(position);
                        break;
                }
            }
        });

    }

    /**
     * 关闭所有打开的item侧滑菜单
     */
    private void closeToAll() {
        ListIterator<SlideItem> iterator = mSlideDeleteViews.listIterator();
        while (iterator.hasNext()) {
            SlideItem next = iterator.next();
            next.closeItem(false);
        }
        mSlideDeleteViews.clear();
    }


    @Override
    public void open(SlideItem slideItem) {
        closeToAll();
        mSlideDeleteViews.add(slideItem);
    }

    @Override
    public void close(SlideItem slideItem) {
        mSlideDeleteViews.remove(slideItem);
    }

}
