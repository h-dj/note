package com.example.h_dj.note.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.h_dj.note.Listener.IListener;
import com.example.h_dj.note.Listener.ListenerManager;
import com.example.h_dj.note.Listener.ModifyView;
import com.example.h_dj.note.R;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.Impl.ModifyPresenterImpl;
import com.example.h_dj.note.presenter.ModifyPresenter;
import com.example.h_dj.note.utils.DateUtils;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.widgets.CustomDialog;
import com.example.h_dj.note.widgets.PickerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by H_DJ on 2017/5/5.
 */

public class ModifyDataActivity extends BaseActivity implements IListener, ModifyView {


    private static final int TAKE_PICTURE = 1; //选择图片的requestCode
    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.sp_type)
    Spinner mSpType;
    @BindView(R.id.tv_modify_time)
    TextView mTvModifyTime;
    @BindView(R.id.tv_alarmClock_time)
    TextView mTvAlarmClockTime;
    @BindView(R.id.ll_info)
    LinearLayout mLlInfo;
    @BindView(R.id.et_modify_content)
    RichEditor mEtModifyContent;

    @BindView(R.id.action_undo)
    ImageButton mActionUndo;
    @BindView(R.id.action_redo)
    ImageButton mActionRedo;
    @BindView(R.id.action_bold)
    ImageButton mActionBold;
    @BindView(R.id.action_italic)
    ImageButton mActionItalic;
    @BindView(R.id.action_subscript)
    ImageButton mActionSubscript;
    @BindView(R.id.action_superscript)
    ImageButton mActionSuperscript;
    @BindView(R.id.action_strikethrough)
    ImageButton mActionStrikethrough;
    @BindView(R.id.action_underline)
    ImageButton mActionUnderline;
    @BindView(R.id.action_heading1)
    ImageButton mActionHeading1;
    @BindView(R.id.action_heading2)
    ImageButton mActionHeading2;
    @BindView(R.id.action_heading3)
    ImageButton mActionHeading3;
    @BindView(R.id.action_heading4)
    ImageButton mActionHeading4;
    @BindView(R.id.action_heading5)
    ImageButton mActionHeading5;
    @BindView(R.id.action_heading6)
    ImageButton mActionHeading6;
    @BindView(R.id.action_txt_color)
    ImageButton mActionTxtColor;
    @BindView(R.id.action_bg_color)
    ImageButton mActionBgColor;
    @BindView(R.id.action_indent)
    ImageButton mActionIndent;
    @BindView(R.id.action_outdent)
    ImageButton mActionOutdent;
    @BindView(R.id.action_align_left)
    ImageButton mActionAlignLeft;
    @BindView(R.id.action_align_center)
    ImageButton mActionAlignCenter;
    @BindView(R.id.action_align_right)
    ImageButton mActionAlignRight;

    @BindView(R.id.action_insert_image)
    ImageButton mActionInsertImage;
    @BindView(R.id.action_insert_link)
    ImageButton mActionInsertLink;

    @BindView(R.id.action_insert_video)
    ImageButton mActionInsertVideo;
    @BindView(R.id.action_insert_voice)
    ImageButton mActionInsertVoice;
    @BindView(R.id.action_insert_doodle)
    ImageButton mActionInsertDoodle;
    /**
     * Note类型
     */
    private List<String> noteTypes;
    private Note mNote;

    private CustomDialog mCustomDialog;//自定义对话框
    private PickerFragment mPickerFragment;//日期时间选择器

    //P层引用
    private ModifyPresenter mPresenter;
    private Intent mIntent;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    @Override
    public void init() {
        super.init();
        ListenerManager.getInstance().registerListtener(this);
        mIntent = getIntent();
        mNote = new Note();
        mPresenter = new ModifyPresenterImpl(getApplicationContext(), this);
        setData();
        initToolbar();
        initNoteTypesData();
        initSpanner();
        initModifyTime();
        initPickerFragment();
        initDialog();
        initRichEdit();
    }

    /**
     * 初始化富文本
     */
    private void initRichEdit() {
        mEtModifyContent.setEditorFontSize(22);
        mEtModifyContent.setAlignLeft();
        mEtModifyContent.setPadding(10, 10, 10, 10);
    }

    /**
     * 初始化数据
     */
    private void setData() {

        if (mIntent != null) {
            Bundle data = mIntent.getBundleExtra("data");
            if (data != null) {
                position = data.getInt("position");
                mNote = (Note) data.get("Note");
                mEtTitle.setText(mNote.getNoteTitle());
                mEtModifyContent.setHtml(mNote.getNoteContent());
                mSpType.setPrompt(mNote.getNoteType());
                showAlarmTime(mNote.getAlarmTime(), mNote.isAlarm());
            }

        }


    }


    /**
     * 初始化
     */
    private void initPickerFragment() {
        mPickerFragment = new PickerFragment();
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        mCustomDialog = new CustomDialog(this, R.style.MyDialog);
        mCustomDialog.setTitle("设置提醒时间")
                .setContentViewClickListener(new CustomDialog.OnContentViewClickListener() {
                    @Override
                    public void onClick(final View view) {
                        switch (view.getId()) {
                            case R.id.date:
                                showDateTimePicker((TextView) view, PickerFragment.DATE_PICKER);
                                break;
                            case R.id.time:
                                showDateTimePicker((TextView) view, PickerFragment.TIME_PICKER);
                                break;
                        }
                    }
                })
                .setOkClickListener("保存", new CustomDialog.OnOkClickListener() {
                    @Override
                    public void click(View v, String date, String time) {
                        showAlarmTime(date + " " + time, true);
                        mCustomDialog.dismiss();
                    }
                })
                .setNoClickListener("取消", new CustomDialog.OnNoClickListener() {
                    @Override
                    public void click(View v) {
                        showAlarmTime(null, false);
                        mCustomDialog.dismiss();
                    }
                });
    }

    /**
     * 是否显示提醒时间
     *
     * @param time
     * @param isShow
     */
    private void showAlarmTime(String time, boolean isShow) {
        if (isShow) {
            //显示体醒时间TextView
            mTvAlarmClockTime.setVisibility(View.VISIBLE);
            mTvAlarmClockTime.setText(time);
            mNote.setAlarm(true);
        } else {
            mTvAlarmClockTime.setVisibility(View.GONE);
            mNote.setAlarm(false);
        }
    }

    /**
     * 显示时间日期对话框
     *
     * @param view
     * @param flag
     */
    private void showDateTimePicker(final TextView view, int flag) {
        if (flag == PickerFragment.DATE_PICKER) {
            mPickerFragment.setFlag(PickerFragment.DATE_PICKER);
        } else {
            mPickerFragment.setFlag(PickerFragment.TIME_PICKER);
        }
        mPickerFragment.setOnTimeSetListener(new PickerFragment.OnSetTimeListener() {
            @Override
            public void getTimeSet(int hourOfDay, int minute, String A_PM) {
                view.setText(formatTime(hourOfDay) + ":" + formatTime(minute));
            }

            @Override
            public void getDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                view.setText(year + "-" + formatTime(month) + "-" + formatTime(dayOfMonth));
            }
        }).show(getSupportFragmentManager(), "DatePicker");
    }

    /**
     * 处理时间；使其两位数显示
     *
     * @param temp
     * @return
     */
    public String formatTime(int temp) {
        String str = null;
        if (temp < 10) {
            str = "0" + temp;
        } else {
            str = "" + temp;
        }
        return str;
    }

    /**
     * 初始化Note修改时间
     */
    private void initModifyTime() {
        //获取当前时间
        if (TextUtils.isEmpty(mNote.getModifyTime())) {
            String string = DateUtils.Date2String(new Date(), "yyyy-MM-dd HH:mm");
            mNote.setModifyTime(string);
        }
        mTvModifyTime.setText(mNote.getModifyTime());
    }

    /**
     * 初始化Note类型数据
     */
    private void initNoteTypesData() {
        noteTypes = new ArrayList<>();
        noteTypes.add("全部");
        noteTypes.add("日常");
        noteTypes.add("学习");
        noteTypes.add("娱乐");
    }

    /**
     * 初始化下拉列表spanner
     */
    private void initSpanner() {

        mSpType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteTypes));
        mSpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ModifyDataActivity.this, "" + noteTypes.get(position).toString(), Toast.LENGTH_SHORT).show();
                //设置便签类别
                mNote.setNoteType(noteTypes.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(mMainToolbar);
        mMainToolbar.setTitle(getString(R.string.toolbar_title));//设置标题
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarmClock:
                //显示选择时间对话框
                mCustomDialog.show();
//                Toast.makeText(ModifyDataActivity.this, "提醒", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                this.finish();
                break;
            case R.id.save:
                fillNoteData();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    /**
     * 填充Note数据
     */
    public void fillNoteData() {
        //设置标题
        mNote.setNoteTitle(mEtTitle.getText().toString().trim());
        //设置提醒时间
        String AlarmTime = mTvAlarmClockTime.getText().toString().trim();
        if (mNote.isAlarm()) {
            mNote.setAlarmTime(AlarmTime);
        } else {
            mNote.setAlarmTime(null);
        }
        String noteNontent = mEtModifyContent.getHtml().toString();
        mNote.setNoteContent(noteNontent);

        mPresenter.AddData(mNote);
        LogUtil.e(mNote.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    Uri uri = data.getData();//获取全部图片的uri
                    String[] pj = new String[]{MediaStore.Images.Media.DATA};
                    Cursor query = getContentResolver().query(uri, pj, null, null, null);
                    query.moveToFirst();
                    int columnIndex = query.getColumnIndex(pj[0]);
                    String picPath = query.getString(columnIndex);
                    LogUtil.e(picPath);
                    if (!TextUtils.isEmpty(picPath)) {
                        mEtModifyContent.insertImage(picPath, "images");
                    }
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        ListenerManager.getInstance().unRegisterListener(this);
        if (mPresenter != null) {
            mPresenter = null;
        }
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }

        super.onDestroy();

    }


    @Override
    public void notifyAllActivity(Object o) {

    }

    @Override
    public void successfully(Object o) {
        if (o instanceof Integer) {
            ListenerManager.getInstance().sendBroadCast(position);
        } else if (o instanceof String) {
            ListenerManager.getInstance().sendBroadCast(o.toString());
        }
        this.finish();
    }

    @Override
    public void failed() {

    }

    @Override
    public void empty(String s) {

    }


    @OnClick(R.id.action_undo)
    public void onMActionUndoClicked() {
        mEtModifyContent.undo();
    }

    @OnClick(R.id.action_redo)
    public void onMActionRedoClicked() {
        mEtModifyContent.redo();//恢复
    }

    @OnClick(R.id.action_bold)
    public void onMActionBoldClicked() {
        mEtModifyContent.setBold();//加粗
    }

    @OnClick(R.id.action_italic)
    public void onMActionItalicClicked() {
        mEtModifyContent.setItalic();
    }

    @OnClick(R.id.action_subscript)
    public void onMActionSubscriptClicked() {
        mEtModifyContent.setSubscript();

    }

    @OnClick(R.id.action_superscript)
    public void onMActionSuperscriptClicked() {
        mEtModifyContent.setSuperscript();
    }

    @OnClick(R.id.action_strikethrough)
    public void onMActionStrikethroughClicked() {
        mEtModifyContent.setStrikeThrough();
    }

    @OnClick(R.id.action_underline)
    public void onMActionUnderlineClicked() {
        mEtModifyContent.setUnderline();
    }

    @OnClick(R.id.action_heading1)
    public void onMActionHeading1Clicked() {
        mEtModifyContent.setHeading(1);
    }

    @OnClick(R.id.action_heading2)
    public void onMActionHeading2Clicked() {
        mEtModifyContent.setHeading(2);

    }

    @OnClick(R.id.action_heading3)
    public void onMActionHeading3Clicked() {
        mEtModifyContent.setHeading(3);

    }

    @OnClick(R.id.action_heading4)
    public void onMActionHeading4Clicked() {
        mEtModifyContent.setHeading(4);

    }

    @OnClick(R.id.action_heading5)
    public void onMActionHeading5Clicked() {
        mEtModifyContent.setHeading(5);

    }

    @OnClick(R.id.action_heading6)
    public void onMActionHeading6Clicked() {
        mEtModifyContent.setHeading(6);

    }

    @OnClick(R.id.action_txt_color)
    public void onMActionTxtColorClicked() {
        mEtModifyContent.setTextColor(Color.RED);
    }

    @OnClick(R.id.action_bg_color)
    public void onMActionBgColorClicked() {

    }

    @OnClick(R.id.action_indent)
    public void onMActionIndentClicked() {
        mEtModifyContent.setIndent();
    }

    @OnClick(R.id.action_outdent)
    public void onMActionOutdentClicked() {
        mEtModifyContent.setOutdent();
    }

    @OnClick(R.id.action_align_left)
    public void onMActionAlignLeftClicked() {
        mEtModifyContent.setAlignLeft();
    }

    @OnClick(R.id.action_align_center)
    public void onMActionAlignCenterClicked() {
        mEtModifyContent.setAlignCenter();

    }

    @OnClick(R.id.action_align_right)
    public void onMActionAlignRightClicked() {
        mEtModifyContent.setAlignRight();

    }


    @OnClick(R.id.action_insert_image)
    public void onMActionInsertImageClicked() {
        //插入图片
        //选择图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @OnClick(R.id.action_insert_link)
    public void onMActionInsertLinkClicked() {
        //插入链接
//        mEtModifyContent.insertLink();
    }
}
