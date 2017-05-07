package com.example.h_dj.note.presenter.Impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.DailyPresenter;
import com.example.h_dj.note.utils.NotesUtils;
import com.example.h_dj.note.view.DailyFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H_DJ on 2017/5/7.
 */

public class DailyPresenterImpl implements DailyPresenter {

    private Context mContext;
    private DailyFragmentView mView;

    public DailyPresenterImpl(Context context, DailyFragmentView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void loadDailyData() {
        //获取内容解析者
        ContentResolver resolver = mContext.getContentResolver();
        Cursor query = resolver.query(NotesUtils.Note.NOTES_CONTENT_URL, null, NotesUtils.Note.NOTE_TYPE + " like ?", new String[]{"日常"}, null);
        List<Note> notes = converCursorToList(query);
        if (notes.size() > 0) {
            mView.loadDataSuccess(notes);
        } else {
            mView.failed();
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
