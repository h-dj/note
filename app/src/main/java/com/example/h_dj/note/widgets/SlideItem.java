package com.example.h_dj.note.widgets;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by H_DJ on 2017/5/4.
 */

public class SlideItem extends ViewGroup {


    private View mContent;
    private View mButton;

    private int mContentWidth;
    private int mContentHight;
    private int mButtonWidth;
    private int mButtonHight;

    //view的拖拽动画帮助类
    private ViewDragHelper mViewDragHelper;

    public SlideItem(Context context) {
        this(context, null);
    }

    public SlideItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);
    }

    /**
     * 配置完布局后回掉
     */
    @Override
    protected void onFinishInflate() {
//        super.onFinishInflate();
        mContent = getChildAt(0);
        mButton = getChildAt(1);
//        /**
//         * 设置侧滑菜单的单击事件
//         */
//        for (int i = 0; i < ((ViewGroup) mButton).getChildCount(); i++) {
//            ((ViewGroup) mButton).getChildAt(i).setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        mListener.onClick(v);
//                    }
//
//                }
//            });
//        }

        mViewDragHelper = ViewDragHelper.create(this, new MyViewDragHelperCall());
    }

    /**
     * 测量控件的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContent.measure(widthMeasureSpec, heightMeasureSpec);

        LayoutParams params = mButton.getLayoutParams();
        mButtonWidth = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        mButtonHight = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        mButton.measure(mButtonWidth, mButtonHight);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 摆放控件
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContentWidth = mContent.getMeasuredWidth();
        mContentHight = mContent.getMeasuredHeight();
        mContent.layout(0, 0, mContentWidth, mContentHight);

        mButtonWidth = mButton.getMeasuredWidth();
        mButtonHight = mButton.getMeasuredHeight();
        mButton.layout(mContentWidth, 0, mContentWidth + mButtonWidth, mButtonHight);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
        super.computeScroll();
    }

    /**
     * ViewDragHelper的内部类
     */
    private class MyViewDragHelperCall extends ViewDragHelper.Callback {

        /**
         * 当用户触摸要被拖拽的View时调用（即在OnTouch()Down事件后回调）
         * 用于捕捉view
         *
         * @param child     被拖拽的view
         * @param pointerId 标志
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
//            Log.e(this.getClass().getSimpleName(), "tryCaptureView: ");
            return child == mContent || child == mButton;
        }

        /**
         * 当用户离开被拖拽的View时调用（在OnTouch()Up事件后回调）
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            Log.e(this.getClass().getSimpleName(), "onViewReleased: ");
            //位置归正的left
            int left = mContent.getLeft();
            //获取content
            if (-left > mButtonWidth / 2) {
//                mContent.layout(-mButtonWidth, 0, mContentWidth - mButtonWidth, mContentHight);
//                mButton.layout(mContentWidth - mButtonWidth, 0, mContentWidth, mButtonHight);
                //采用ViewDragHelper的 smoothSlideViewTo 方法让移动变得顺滑自然，不会太生硬
                //smoothSlideViewTo只是模拟了数据，但是不会真正的动起来，动起来需要调用 invalidate
                // 而 invalidate 通过调用draw()等方法之后最后还是还是会调用 computeScroll 这个方法
                // 所以，使用 smoothSlideViewTo 做过渡动画需要结合  invalidate方法 和 computeScroll方法
                // smoothSlideViewTo的动画执行时间没有暴露的参数可以设置，但是这个时间是google给我们经过大量计算给出合理时间
//                mViewDragHelper.smoothSlideViewTo(mContent, -mButtonWidth, 0);
//                mViewDragHelper.smoothSlideViewTo(mButton, mContentWidth - mButtonWidth, 0);
                closeItem(true);
                if (mCloseListener != null) {
                    mCloseListener.open(SlideItem.this);
                }
            } else {
//                mContent.layout(0, 0, mContentWidth, mContentHight);
//                mButton.layout(mContentWidth, 0, mContentWidth + mButtonWidth, mButtonHight);
//                mViewDragHelper.smoothSlideViewTo(mContent, 0, 0);
//                mViewDragHelper.smoothSlideViewTo(mButton, mContentWidth, 0);
                closeItem(false);
                if (mCloseListener != null) {
                    mCloseListener.close(SlideItem.this);
                }
            }
            invalidate();
            super.onViewReleased(releasedChild, xvel, yvel);
        }


        /**
         * 限制被拖拽的View只能在水平方向
         *
         * @param child 被拖拽的view
         * @param left  被拖拽的view距离父用器左边的距离
         * @param dx    拖拽速率
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            Log.e(this.getClass().getSimpleName(), "clampViewPositionHorizontal: " + left + ":" + mButtonWidth);
            //处理越界问题
            if (child == mContent) {
                if (left > 0) {
                    return 0;
                } else if (-left > mButtonWidth) {
                    return -mButtonWidth;
                }
            }
            if (child == mButton) {
                if (left > mContentWidth) {
                    return mContentWidth;
                } else if (left < mContentWidth - mButtonWidth) {
                    return mContentWidth - mButtonWidth;
                }
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * 当被拖拽的view的位置改变时调用
         *
         * @param changedView 被拖拽的view
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            Log.e(this.getClass().getSimpleName(), "onViewPositionChanged: " + left);
            invalidate();
            //如果被捕捉的view是Content;则对Button的位置进行调整；
            if (changedView == mContent) {
                int tempDelLeft = mContentWidth + left;
                int tempDelRight = mContentWidth + left + mButtonWidth;
                mButton.layout(tempDelLeft, 0, tempDelRight, mButtonWidth);
            } else {
                int tempContentLeft = left - mContentWidth;
                int tempContentRight = left;
                mContent.layout(tempContentLeft, 0, tempContentRight, mContentHight);
            }

            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

    }

    /**
     * 自动关闭item
     *
     * @param b
     */
    public void closeItem(boolean b) {
        if (b) {
            mViewDragHelper.smoothSlideViewTo(mContent, -mButtonWidth, 0);
            mViewDragHelper.smoothSlideViewTo(mButton, mContentWidth - mButtonWidth, 0);
        } else {
            mViewDragHelper.smoothSlideViewTo(mContent, 0, 0);
            mViewDragHelper.smoothSlideViewTo(mButton, mContentWidth, 0);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 在截取父控件的onTouchEvent事件后需要返回true; 不然事件会被分发下去；不能拖拽
         */
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

//    /**
//     * 设置侧滑菜单的单击接口
//     */
//    private OnItemMenuClickListener mListener;
//
//    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
//        mListener = listener;
//    }
//
//    public interface OnItemMenuClickListener {
//        void onClick(View view);
//    }


    /**
     * 监听item关闭的接口
     */
    private OnItemCloseListener mCloseListener;

    public void setOnItemCloseListener(OnItemCloseListener listener) {
        mCloseListener = listener;
    }

    public interface OnItemCloseListener {
        void open(SlideItem slideItem);

        void close(SlideItem slideItem);
    }


}
