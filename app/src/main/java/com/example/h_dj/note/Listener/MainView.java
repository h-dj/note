package com.example.h_dj.note.Listener;

import com.example.h_dj.note.bean.Note;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/7.
 */

public interface MainView {
    void loadDataSuccess(List<Note> notes);

    void failed();

    void delSuccess(int position);
}
