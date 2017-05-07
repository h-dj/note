package com.example.h_dj.note.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 通用的Adapter
 * Created by H_DJ on 2017/3/24.
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewAdapter.MyViewHolder> {


    protected Context mContext;
    protected List<T> mList; //数据集
    protected int layoutId; //布局id
    protected OnItemClickListener mOnItemClickListener;

    public BaseRecycleViewAdapter(Context context, int layoutId, List<T> datas) {
        this.layoutId = layoutId;
        mList = datas;
        mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MyViewHolder.getViewHolder(mContext, parent, layoutId);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        convert(holder, mList.get(position), position);

        //设置ItemView单击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.mConveryView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.mConveryView, position);
                    return true;
                }
            });
        }

    }

    /**
     * 交给调用者绑定数据
     *
     * @param holder
     * @param t
     */
    protected abstract void convert(MyViewHolder holder, T t, int position);  //绑定数据


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViewSparseArray; //用来保存View;以免多次findViewById
        protected View mConveryView;  //用来复用itemView
        private Context mContext;
        private final String TAG = "MyViewHolder";

        //初始化参数
        public MyViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mViewSparseArray = new SparseArray<View>();
            mConveryView = itemView;
        }

        /**
         * 获取ViewHolder；通过布局id来构建ViewHolder
         */
        public static MyViewHolder getViewHolder(Context context, ViewGroup root, int layoutId) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, root, false);
            MyViewHolder holder = new MyViewHolder(context, itemView);
            return holder;
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
        public void setOnClikListener(int viewId, OnClickListener listener) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
        }
    }


    //定义一个接口用来ItemView的点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    //设置接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


}

