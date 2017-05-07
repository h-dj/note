package com.example.h_dj.note.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by H_DJ on 2017/5/6.
 * 向EditText添加图片工具类
 */

public class EditTextUtils implements View.OnFocusChangeListener, View.OnClickListener {
    /**
     * SpannableString
     * ImageSpan
     */
    private static EditTextUtils utils;
    private static final String TAG = "EditTextUtils";
    private int picWidth = 600;//显示图片的宽
    private int picHeight = 800;//显示图片的高

    private String imgStartTag = "<img src=\"";//显示图片标签的开始标签
    private String imgEndTag = "\"/>"; //显示图片标签的结束标签


    private OnClickableSpanListener onClickableSpanListener;

    private EditText mEditText;

    private Context mContext;
    private boolean isImageSpan;//判断是否为图片

    public EditTextUtils with(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public static EditTextUtils getInstance() {
        if (utils == null) {
            synchronized (EditTextUtils.class) {
                if (utils == null) {
                    utils = new EditTextUtils();
                }
            }
        }
        return utils;
    }

    /**
     * 设置图片宽高
     *
     * @param width
     * @param height
     */
    public EditTextUtils setPicBounds(int width, int height) {
        picWidth = width;
        picHeight = height;
        return this;
    }

    /**
     * 向EditText中添加图片
     *
     * @param picPath 图片路径
     * @return 返回显示图片的CharSequence
     */
    public CharSequence getDrawableStr(String picPath) {
        InputStream is = null;
        String str = "<img src=\"" + picPath + "\"/>";
        try {
            is = new FileInputStream(picPath);
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inTempStorage = new byte[100 * 1024];
            ops.inPreferredConfig = Bitmap.Config.RGB_565;
            ops.inSampleSize = 2;
            ops.inInputShareable = true;
            ops.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, ops);

            SpannableString ss = new SpannableString(str);
            //定义插入图片
            Drawable drawable = new BitmapDrawable(bitmap);
            Log.e(TAG, "getDrawableStr: " + picWidth + ": " + picHeight);
            drawable.setBounds(0, 0, picWidth, picHeight);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return ss;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setClick(v);
    }

    @Override
    public void onClick(View v) {
        setClick(v);
    }


    /**
     * 监听EditText内容；获取内容的点击事件
     */
    private void setClick(View v) {
        isImageSpan = false;
        Log.e(TAG, "setClick: ");
        if (mContext == null) {
            return;
        }
        if (mEditText == null) {
            return;
        }

        if (onClickableSpanListener == null) {
            return;
        }
        Log.e(TAG, "setClick: 2");
        //关闭输入盘
        View view = ((Activity) mContext).getWindow().peekDecorView();
        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //获取EditText中的SpannableString
        Spanned st = mEditText.getText();
        //获取EditText显示图片的ImageSpan
        ImageSpan[] spans = st.getSpans(0, st.length(), ImageSpan.class);
        //获取光标的位置
        int selectionStart = mEditText.getSelectionStart();
        for (ImageSpan span : spans) {
            //获取每个ImageSpan的位置
            int spanStart = st.getSpanStart(span);
            int spanEnd = st.getSpanEnd(span);
            //判断光标是否在ImageSpan内
            if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                isImageSpan = true;
                onClickableSpanListener.onClick(v);
                break;
            }
        }
        if (!isImageSpan) {
            manager.showSoftInput(mEditText, 0);
        }

        mEditText.setAlpha(1);
    }

    /**
     * 监听图片被点击接口
     */
    public interface OnClickableSpanListener {
        void onClick(View v);
    }

    public EditTextUtils setOnClickableSpanListener(OnClickableSpanListener listener) {
        onClickableSpanListener = listener;
        return this;
    }

    /**
     * 点击事件作用于的EditText
     *
     * @param et
     * @return
     */
    public EditTextUtils to(EditText et) {
        mEditText = et;

        mEditText.setOnClickListener(this);
        mEditText.setOnFocusChangeListener(this);
        return this;
    }


    /**
     * 解析字符串，把字符串中的图片CharSequence显示出来
     *
     * @param editText EditText
     * @param content  字符串内容
     */
    public void updataContent(EditText editText, String content) {
        //如果content解析完就退出
        if (TextUtils.isEmpty(content)) {
            return;
        }
        //普通字符的开始；结束位置
        int startIndex = 0;
        int endIndex = 0;
        //定位到显示图片的开始标签位置
        int imgStartIndex = content.lastIndexOf(imgStartTag);
        if (imgStartIndex < 0) {
            //如果图片开始标签没找到；则没有图片;可以直接添加到EditText中
            endIndex = content.length();
        } else {
            endIndex = imgStartIndex;
        }
        //把普通字符分割出来；添加到EditText中
        String str = content.substring(startIndex, endIndex);
        editText.append(str);
        //把已添加到EditText中的字符删除
        content = content.substring(endIndex, content.length());

        //如果content解析完就退出
        if (TextUtils.isEmpty(content)) {
            return;
        }

        //解析出显示图片的路径
        int imgEndIndex = content.lastIndexOf(imgEndTag);
        str = content.substring(imgStartTag.length(), imgEndIndex);
        editText.append(getDrawableStr(str));

        content = content.substring(imgEndIndex + imgEndTag.length(), content.length());

        updataContent(editText, content);
    }

    /**
     * 获取内容中的图片路径
     *
     * @param view
     * @param content
     * @return
     */
    public String setBitmapPath(ImageView view, String content) {
        //解析出显示图片的路径
        int imgStartIndex = content.lastIndexOf(imgStartTag);
        int imgEndIndex = content.lastIndexOf(imgEndTag);
        if (imgStartIndex < 0) {
            return null;
        }
        String str = content.substring(imgStartTag.length(), imgEndIndex);
        return str;
    }

}
