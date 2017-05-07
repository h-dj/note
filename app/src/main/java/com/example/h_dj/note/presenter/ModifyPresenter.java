package com.example.h_dj.note.presenter;

import com.example.h_dj.note.bean.Note;

/**
 * Created by H_DJ on 2017/5/7.
 */

public interface ModifyPresenter {
    void AddData(Note note);

    void queryData(int noteId);
}
