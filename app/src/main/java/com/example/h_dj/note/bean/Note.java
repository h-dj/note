package com.example.h_dj.note.bean;

/**
 * Created by H_DJ on 2017/5/6.
 */

public class Note {

    private String noteId;//便签id
    private String noteTitle;//便签标题
    private String noteType;//类型
    private String modifyTime;//修改时间
    private String alarmTime;//提醒时间；可以为空
    private boolean isAlarm;//是否设置提醒
    private boolean isMark;//是否添加类星号 可以为空
    private String noteContent;//内容

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public boolean isMark() {
        return isMark;
    }

    public void setMark(boolean mark) {
        isMark = mark;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    @Override
    public String toString() {
        return "Note{" +
                "noteId='" + noteId + '\'' +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteType='" + noteType + '\'' +
                ", modifyTime=" + modifyTime +
                ", alarmTime=" + alarmTime +
                ", isAlarm=" + isAlarm +
                ", isMark=" + isMark +
                ", noteContent='" + noteContent + '\'' +
                '}';
    }
}
