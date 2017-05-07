package com.example.h_dj.note.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.h_dj.note.R;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.widgets.SlideItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class MyAdapter extends BaseRecycleViewAdapter<Note> implements SlideItem.OnItemCloseListener, SlideItem.OnItemMenuClickListener {


    private List<SlideItem> mSlideDeleteViews;

    public MyAdapter(Context context, int layoutId, List<Note> datas) {
        super(context, layoutId, datas);
        mSlideDeleteViews = new ArrayList<>();
    }

    @Override
    protected void convert(MyViewHolder holder, Note note) {
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

        if (note.isMark()) {
            holder.getView(R.id.item_mark).setBackgroundResource(R.drawable.mark_select);
        }else {
            holder.getView(R.id.item_mark).setBackgroundResource(R.drawable.mark);
        }
        //设置关闭事件
        ((SlideItem) holder.mConveryView).setOnItemCloseListener(this);

        //设置侧滑menu的单击事件
        ((SlideItem) holder.mConveryView).setOnItemMenuClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                Toast.makeText(mContext, "编辑按钮被单击", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_del:
                Toast.makeText(mContext, "删除按钮被单击", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
