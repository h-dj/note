package com.example.h_dj.note;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.example.h_dj.note.bean.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;

/**
 * Created by H_DJ on 2017/5/9.
 */

public class App extends Application {


    private List<Note> mNoteList;

    @Override
    public void onCreate() {
        super.onCreate();
        mNoteList = new ArrayList<>();
        initSmallVideo(this);
    }

    public List<Note> getNoteList() {
        return mNoteList;
    }

    public void setNoteList(List<Note> noteList) {
        mNoteList = noteList;
    }

    /**
     * 初始化小视频录制：
     *
     * @param context
     */
    public static void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }



    public  List<Activity> activities = new ArrayList<Activity>();

    public  void addActivity(Activity activity) {
        activities.add(activity);
    }

    public  void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public  void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
