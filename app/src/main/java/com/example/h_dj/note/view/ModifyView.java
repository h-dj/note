package com.example.h_dj.note.view;

import com.example.h_dj.note.bean.Note;

import java.util.List;

/**
 * Created by H_DJ on 2017/5/7.
 */

public interface ModifyView {
    void empty(String s);

    void successfully();

    void failed();

    void setData(List<Note> notes);
}
