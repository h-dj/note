package com.example.h_dj.note.presenter.Impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.AllNotePresenter;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.utils.NotesUtils;
import com.example.h_dj.note.view.AllNoteFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H_DJ on 2017/5/7.
 */

public class AllNotePresenterImpl implements AllNotePresenter {

    private Context mContext;
    private AllNoteFragmentView mView;

    public AllNotePresenterImpl(Context context, AllNoteFragmentView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void loadDailyData() {
        //获取内容解析者
        ContentResolver resolver = mContext.getContentResolver();
        Cursor query = resolver.query(NotesUtils.Note.NOTES_CONTENT_URL, null, null, null, null);
        List<Note> notes = converCursorToList(query);
        if (notes.size() > 0) {
            mView.loadDataSuccess(notes);
        } else {
            mView.failed();
        }

    }

    @Override
    public void del(String noteId) {
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = Uri.parse(NotesUtils.Note.NOTE_CONTENT_URL.toString() + "/" + noteId);
        int delete = resolver.delete(uri, "noteId = ?", new String[]{noteId});
        if (delete > 0) {
            mView.delSuccess();
        } else {
            LogUtil.e(delete + ":" + noteId);
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
