package com.example.h_dj.note.Listener;

/**
 * Created by H_DJ on 2017/5/7.
 */

public interface ModifyView <T>{

    void successfully(T t);

    void failed();

    void empty(String s);
}
