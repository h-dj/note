package com.example.h_dj.note.widgets;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by H_DJ on 2017/5/3.
 */

public class SlideDeleteView extends ViewGroup {

    private final String TAG = "SlideDeleteView";
    private View mContent; //内容
    private View mDelete;//删除
    private int mContentWidth;
    private int mContentHight;
    private int mDelWidth;
    private ViewDragHelper mHelper;

    private onItemSwipeListener mListener;
    private boolean isShow;

    public SlideDeleteView(Context context) {
        super(context);
    }

    public SlideDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /**
     * 当适配完布局后回掉
     */
    @Override
    protected void onFinishInflate() {
        Log.e(TAG, "onFinishInflate: ");
        super.onFinishInflate();
        mContent = getChildAt(0);
        mDelete = getChildAt(1);
        mHelper = ViewDragHelper.create(this, new MyDragHelper());

    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure: ");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContent.measure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams params = mDelete.getLayoutParams();
        int delWidth = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        int delHight = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        mDelete.measure(delWidth, delHight);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "onLayout: ");
        mContentWidth = mContent.getMeasuredWidth();
        mContentHight = mContent.getMeasuredHeight();
        mContent.layout(0, 0, mContentWidth, mContentHight);// 摆放内容部分的位置
        mDelWidth = mDelete.getMeasuredWidth();
        mDelete.layout(mContentWidth, 0, mContentWidth + mDelWidth, mContentHight); // 摆放删除部分的位置
    }

    private class MyDragHelper extends ViewDragHelper.Callback {
        private final String TAG = MyDragHelper.class.getSimpleName();

        /**
         * 在onTouchDown时使用
         *
         * @param child     指定要动的孩子  （哪个孩子需要动起来）
         * @param pointerId 点的标记
         * @return ViewDragHelper是否继续分析处理 child的相关touch事件
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.e(TAG, "tryCaptureView: " + (mContent == child));

            return mContent == child || mDelete == child;
        }

        /**
         * 捕获了水平方向移动的位移数据
         *
         * @param child 移动的孩子View
         * @param left  父容器的左上角到孩子View的距离
         * @param dx    增量值，其实就是移动的孩子View的左上角距离控件（父亲）的距离，包含正负
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e(TAG, "clampViewPositionHorizontal: " + (mContent == child) + left + ": " + (mDelete == child));
            if (child == mContent) { // 解决内容部分左右拖动的越界问题
                if (left > 0) {
                    return 0;
                } else if (-left > mDelWidth) {
                    return -mDelWidth;
                }
            }
            if (child == mDelete) { // 解决删除部分左右拖动的越界问题
                if (left < mContentWidth - mDelWidth) {
                    return mContentWidth - mDelWidth;
                } else if (left > mContentWidth) {
                    return mContentWidth;
                }
            }
            return left;

        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e(TAG, "clampViewPositionVertical: ");
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * 当View的位置改变时的回调
         *
         * @param changedView 哪个View的位置改变了
         * @param left        changedView的left
         * @param top         changedView的top
         * @param dx          x方向的上的增量值
         * @param dy          y方向上的增量值
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e(TAG, "onViewPositionChanged: " + left);
            invalidate();
            if (changedView == mContent) {
                int tempDelleft = mContentWidth + left;
                int tempDelRight = mContentWidth + left + mDelWidth;
                mDelete.layout(tempDelleft, 0, tempDelRight, mDelWidth);
            } else {
                int tempContentLeft = mContentWidth + left;
                int tempContentRight = left;
                mContent.layout(tempContentLeft, 0, tempContentRight, mContentWidth);
            }
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.e(TAG, "onViewReleased: ");
            int contentLeft = mContent.getLeft();
            /**
             * 释放时位置的归正
             */
            if (-contentLeft > mDelWidth / 2) {
                showDelButton(true);
                if (mListener != null) {
                    mListener.onOpen(SlideDeleteView.this);
                }
            } else {
                showDelButton(false);
                if (mListener != null) {
                    mListener.onClose(SlideDeleteView.this);
                }
            }
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    }

    /**
     * 是否显示删除按钮
     *
     * @param isShow
     */
    public void showDelButton(boolean isShow) {
        Log.e(TAG, "showDelButton: " );
        if (isShow) {
            mContent.layout(-mDelWidth, 0, mContentWidth - mDelWidth, mContentHight);
            mDelete.layout(mContentWidth - mDelWidth, 0, mContentWidth, mContentHight);

            //滑动动画
            mHelper.smoothSlideViewTo(mContent, -mDelWidth, 0);
            mHelper.smoothSlideViewTo(mDelete, mContentWidth - mDelWidth, 0);
        } else {
            mContent.layout(0, 0, mContentWidth, mContentHight);
            mDelete.layout(mContentWidth, 0, mContentWidth + mDelWidth, mContentHight);
            //滑动动画
            mHelper.smoothSlideViewTo(mContent, 0, 0);
            mHelper.smoothSlideViewTo(mDelete, mContentWidth, 0);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;

    }

    /**
     * 控制打开与关闭的接口
     */
    public interface onItemSwipeListener {
        void onOpen(SlideDeleteView view);

        void onClose(SlideDeleteView view);
    }

    public void setItemSwipeListener(onItemSwipeListener listener) {
        mListener = listener;
    }
}
