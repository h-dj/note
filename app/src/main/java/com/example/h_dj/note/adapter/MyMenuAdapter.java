package com.example.h_dj.note.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.h_dj.note.R;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.utils.LogUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/8.
 */

public class MyMenuAdapter extends SwipeMenuAdapter<MyMenuAdapter.MyViewHolder> {


    private List<Note> mNotes;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void OnClick(View view, int position);

        void OnLongClick(View view, int position);
    }

    /**
     * 设置item单击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MyMenuAdapter(List<Note> notes) {
        mNotes = notes;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false);
        return inflate;
    }

    @Override
    public MyMenuAdapter.MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MyViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(final MyMenuAdapter.MyViewHolder holder, final int position) {
        final Note note = mNotes.get(position);
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
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private SparseArray<View> mViewSparseArray; //用来保存View;以免多次findViewById
        protected View mConveryView;  //用来复用itemView
        private final String TAG = "MyViewHolder";

        //初始化参数
        public MyViewHolder(View itemView) {
            super(itemView);
            mViewSparseArray = new SparseArray<View>();
            mConveryView = itemView;
            mConveryView.setOnClickListener(this);
            mConveryView.setOnLongClickListener(this);
        }


        /**
         * //通过ViewId来获取控件;利用泛型
         *
         * @param viewId
         * @param <T>
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViewSparseArray.get(viewId);
            if (view == null) {
                view = mConveryView.findViewById(viewId);
                mViewSparseArray.put(viewId, view);
            }
            return (T) view;
        }


        /**
         * //常用控件方法；设置文本
         *
         * @param ViewId
         * @param content
         */
        public void setText(int ViewId, String content) {
//            Log.i(TAG, "setText: " + ViewId);
            TextView tv = getView(ViewId);
            tv.setText(content);
        }


        /**
         * //设置ItemView 中单独子view的单击监听
         *
         * @param viewId
         * @param listener
         */
        public void setOnClikListener(int viewId, View.OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.OnClick(v, getAdapterPosition());
            }
        }
    }


}
