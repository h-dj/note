package com.example.h_dj.note.utils;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by H_DJ on 2017/5/6.
 * 提供ContentProvider使用的常量类
 */

public class NotesUtils {

    /**
     * 设置authorities
     */
    public final static String AUTHORITIES = "com.example.h_dj.note";

    public static final class Note implements BaseColumns {
        //允许操作的数据列
        public final static String NOTE_ID = "noteId";
        public final static String NOTE_TITLE = "noteTitle";
        public final static String NOTE_TYPE = "noteType";
        public final static String NOTE_MODIFY_TIME = "modifyTime";
        public final static String NOTE_ALARM_TIME = "alarmTime";
        public final static String NOTE_IS_MARK = "isMark";
        public final static String NOTE_CONTENT = "noteContent";

        //定义提供服务的uri
        public final static Uri NOTE_CONTENT_URL = Uri.parse("content://" + AUTHORITIES + "/note");
        public final static Uri NOTES_CONTENT_URL = Uri.parse("content://" + AUTHORITIES + "/notes");
    }
}
