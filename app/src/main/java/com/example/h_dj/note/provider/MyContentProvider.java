package com.example.h_dj.note.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.h_dj.note.db.DBHelper;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.utils.NotesUtils;

/**
 * Created by H_DJ on 2017/5/6.
 */

public class MyContentProvider extends ContentProvider {

    /**
     * 1. 先定义一个数据帮助类
     */
    private DBHelper mDBHelper;

    /**
     * 2. 注册访问的uri
     */
    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int NOTE = 1;
    private static final int NOTES = 2;

    static {
        mUriMatcher.addURI(NotesUtils.AUTHORITIES, "note/#", NOTE);
        mUriMatcher.addURI(NotesUtils.AUTHORITIES, "notes/", NOTES);
    }

    /**
     * 在getContentReceiver()后调用
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        LogUtil.e("onCreate");
        //实例化数据库帮助类
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case NOTES:
                return db.query(mDBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case NOTE:
                //解析出查询的记录ID
                long id = ContentUris.parseId(uri);
                String whereClause = NotesUtils.Note.NOTE_ID + "=" + id;
                if (!TextUtils.isEmpty(whereClause)) {
                    whereClause = whereClause + " and " + selection;
                }
                return db.query(mDBHelper.TABLE_NAME, projection, whereClause, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("未知uri: " + uri);
        }
    }

    /**
     * 返回指定的uri
     *
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case NOTE:
                //单条数据的处理
                return "vnd.android.cursor.item/" + NotesUtils.AUTHORITIES;
            case NOTES:
                //多条数据的处理
                return "vnd.android.cursor.dir/" + NotesUtils.AUTHORITIES;
            default:
                throw new IllegalArgumentException("不支持的URI:" + uri);
        }
    }

    /**
     * 插入数据
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case NOTES:
                long rowId = db.insert(mDBHelper.TABLE_NAME, NotesUtils.Note.NOTE_ID, values);
                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(uri, rowId);
                    //通知数据已改变
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的URI:" + uri);
        }
        return null;
    }

    /**
     * 删除数据
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        //记录修改的记录数据
        int num = 0;
        switch (mUriMatcher.match(uri)) {
            case NOTES:
                num = db.delete(mDBHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTE:
                //解析出查询的记录ID
                long id = ContentUris.parseId(uri);
                String whereClause = NotesUtils.Note.NOTE_ID + "=" + id;
                if (!TextUtils.isEmpty(whereClause)) {
                    whereClause = whereClause + " and " + selection;
                }
                num = db.delete(mDBHelper.TABLE_NAME, whereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("不支持的URI:" + uri);
        }
        //通知数据已改变
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    /**
     * 更新数据
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        //记录修改的记录数据
        int num = 0;
        switch (mUriMatcher.match(uri)) {
            case NOTES:
                num = db.update(mDBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTE:
                //解析出查询的记录ID
                long id = ContentUris.parseId(uri);
                String whereClause = NotesUtils.Note.NOTE_ID + "=" + id;
                if (!TextUtils.isEmpty(whereClause)) {
                    whereClause = whereClause + " and " + selection;
                }
                num = db.update(mDBHelper.TABLE_NAME, values, whereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("不支持的URI:" + uri);
        }
        //通知数据已改变
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }
}
