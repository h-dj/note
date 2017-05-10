package com.example.h_dj.note;

import android.app.Application;

import com.example.h_dj.note.bean.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H_DJ on 2017/5/9.
 */

public class App extends Application {



    private List<Note> mNoteList;

    @Override
    public void onCreate() {
        super.onCreate();
        mNoteList = new ArrayList<>();
    }

    public List<Note> getNoteList() {
        return mNoteList;
    }

    public void setNoteList(List<Note> noteList) {
        mNoteList = noteList;
    }
}
