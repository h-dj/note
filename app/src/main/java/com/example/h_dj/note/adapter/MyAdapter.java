package com.example.h_dj.note.adapter;

import android.content.Context;

import com.example.h_dj.note.R;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.widgets.SlideDeleteView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class MyAdapter extends BaseRecycleViewAdapter {


    private List<SlideDeleteView> mSlideDeleteViews;

    public MyAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        mSlideDeleteViews = new ArrayList<>();
    }

    @Override
    protected void convert(MyViewHolder holder, Object o) {
        holder.setText(R.id.item_title, (String) o);
        ((SlideDeleteView) holder.mConveryView).setItemSwipeListener(new SlideDeleteView.onItemSwipeListener() {
            @Override
            public void onOpen(SlideDeleteView view) {
                closeToAll();
                mSlideDeleteViews.add(view);
                LogUtil.e("onOpen");
            }

            @Override
            public void onClose(SlideDeleteView view) {
                mSlideDeleteViews.remove(view);
                LogUtil.e("onClose");
            }
        });
    }

    /**
     * 关闭所有打开的item侧滑菜单
     */
    private void closeToAll() {
        ListIterator<SlideDeleteView> iterator = mSlideDeleteViews.listIterator();
        while (iterator.hasNext()) {
            SlideDeleteView next = iterator.next();
            next.showDelButton(false);
        }
        mSlideDeleteViews.clear();
    }
}
