package com.example.h_dj.note.presenter.Impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.h_dj.note.Listener.ModifyView;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.ModifyPresenter;
import com.example.h_dj.note.utils.NotesUtils;

/**
 * Created by H_DJ on 2017/5/7.
 */

public class ModifyPresenterImpl implements ModifyPresenter {

    private Context mContext;

    //View层引用
    private ModifyView mModifyView;

    public ModifyPresenterImpl(Context context, ModifyView view) {
        mContext = context;
        mModifyView = view;
    }

    /**
     * 添加数据
     *
     * @param note
     */
    @Override
    public void AddData(Note note) {
        String title = note.getNoteTitle();
        String content = note.getNoteContent();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            //告知View 没有填写内容
            mModifyView.empty("没有内容可添加，是否退出？");
        } else {
            //获取内容解析者
            ContentResolver resolver = mContext.getContentResolver();
            ContentValues values = new ContentValues();
            values.put("noteTitle", note.getNoteTitle());
            values.put("noteType", note.getNoteType());
            values.put("modifyTime", note.getModifyTime());
            values.put("isAlarm", note.isAlarm());
            if (note.isAlarm()) {
                values.put("alarmTime", note.getAlarmTime());
            } else {
                values.putNull("alarmTime");
            }
            values.putNull("isMark");
            values.put("noteContent", note.getNoteContent());
            int num = resolver.update(NotesUtils.Note.NOTES_CONTENT_URL, values, "noteId = ?", new String[]{note.getNoteId()});
            if (num != 0) {
                mModifyView.successfully(num);
            } else {
                Uri insert = resolver.insert(NotesUtils.Note.NOTES_CONTENT_URL, values);
                if (insert != null) {
                    mModifyView.successfully(insert.toString());
                } else {
                    mModifyView.failed();
                }
            }
        }
    }

}
