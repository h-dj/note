package com.example.h_dj.note.view;

import com.example.h_dj.note.bean.Note;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/7.
 */

public interface AllNoteFragmentView {
    void loadDataSuccess(List<Note> notes);

    void failed();

    void delSuccess();
}
