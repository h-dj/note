package com.example.h_dj.note.presenter.Impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.ModifyPresenter;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.utils.NotesUtils;
import com.example.h_dj.note.view.ModifyView;

import java.util.ArrayList;
import java.util.List;

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
            Uri insert = resolver.insert(NotesUtils.Note.NOTES_CONTENT_URL, values);
            if (insert != null) {
                mModifyView.successfully();
            } else {
                mModifyView.failed();
            }
        }
    }

    /**
     * 查询数据
     *
     * @param noteId
     */
    @Override
    public void queryData(int noteId) {
        if (noteId != -1) {
            ContentResolver resolver = mContext.getContentResolver();
            Uri uri = Uri.parse(NotesUtils.Note.NOTE_CONTENT_URL.toString() + "/" + noteId);
            LogUtil.e(uri.toString());
            Cursor query = resolver.query(uri, null, "noteId = ?", new String[]{"" + noteId}, null);
            List<Note> notes = converCursorToList(query);
            if (notes.size() > 0) {
                mModifyView.setData(notes);
            } else {
                mModifyView.failed();
            }
        }

    }

    /**
     * 把游标转换为List
     *
     * @param query 游标
     * @return List
     */
    public List<Note> converCursorToList(Cursor query) {
        List<Note> notes = new ArrayList<>();
        while (query.moveToNext()) {
            Note note = new Note();
            note.setNoteId(query.getString(query.getColumnIndex("noteId")));
            note.setNoteTitle(query.getString(query.getColumnIndex("noteTitle")));
            note.setNoteType(query.getString(query.getColumnIndex("noteType")));
            note.setModifyTime(query.getString(query.getColumnIndex("modifyTime")));
            note.setAlarmTime(query.getString(query.getColumnIndex("alarmTime")));
            note.setAlarm(String.valueOf(query.getString(query.getColumnIndex("isAlarm")) + "").equals("1"));
            note.setMark(String.valueOf(query.getString(query.getColumnIndex("isMark")) + "").equals("1"));
            note.setNoteContent(query.getString(query.getColumnIndex("noteContent")));
            notes.add(note);
        }
        return notes;
    }
}
