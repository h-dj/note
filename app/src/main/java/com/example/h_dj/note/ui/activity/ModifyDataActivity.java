package com.example.h_dj.note.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h_dj.note.R;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.Impl.ModifyPresenterImpl;
import com.example.h_dj.note.presenter.ModifyPresenter;
import com.example.h_dj.note.utils.DateUtils;
import com.example.h_dj.note.utils.EditTextUtils;
import com.example.h_dj.note.utils.LogUtil;
import com.example.h_dj.note.view.ModifyView;
import com.example.h_dj.note.widgets.CustomDialog;
import com.example.h_dj.note.widgets.PickerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by H_DJ on 2017/5/5.
 */

public class ModifyDataActivity extends BaseActivity implements View.OnFocusChangeListener, ModifyView {
    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.et_modify_content)
    EditText mEtModifyContent;
    @BindView(R.id.tv_video)
    TextView mTvVideo;
    @BindView(R.id.tv_photo)
    TextView mTvPhoto;
    @BindView(R.id.tv_voice)
    TextView mTvVoice;
    @BindView(R.id.tv_doodle)
    TextView mTvDoodle;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.tv_modify_time)
    TextView mTvModifyTime;
    @BindView(R.id.tv_alarmClock_time)
    TextView mTvAlarmClockTime;
    @BindView(R.id.sp_type)
    Spinner mSpType;
    @BindView(R.id.sll)
    ScrollView mSll;

    private static final int TAKE_PICTURE = 1; //选择图片的requestCode
    /**
     * Note类型
     */
    private List<String> noteTypes;
    private Note mNote;

    private CustomDialog mCustomDialog;//自定义对话框
    private PickerFragment mPickerFragment;//日期时间选择器
    private EditTextUtils mEditTextUtils;//使EditText显示图片的utils

    //P层引用
    private ModifyPresenter mPresenter;
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    @Override
    public void init() {
        super.init();
        mIntent = getIntent();
        mNote = new Note();
        //设置EditText获取焦点；软键盘显示或隐藏问题
        mEtModifyContent.setOnFocusChangeListener(this);
        mEtTitle.setOnFocusChangeListener(this);
        mEditTextUtils = EditTextUtils.getInstance();
        mPresenter = new ModifyPresenterImpl(getApplicationContext(), this);
        if (mIntent != null) {
            int noteId = mIntent.getIntExtra("noteId", -1);
            mPresenter.queryData(noteId);
        }
        initToolbar();
        initNoteTypesData();
        initSpanner();
        initModifyTime();
        initPickerFragment();
        initDialog();
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
                Toast.makeText(ModifyDataActivity.this, "" + noteTypes.get(position).toString(), Toast.LENGTH_SHORT).show();
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
        String noteNontent = mEtModifyContent.getText().toString();
        mNote.setNoteContent(noteNontent);

        mPresenter.AddData(mNote);
        LogUtil.e(mNote.toString());
    }

    @OnClick({R.id.tv_video, R.id.tv_photo, R.id.tv_voice, R.id.tv_doodle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_video:
                break;
            case R.id.tv_photo:
                //选择图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, TAKE_PICTURE);
                break;
            case R.id.tv_voice:
                break;
            case R.id.tv_doodle:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
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
                        CharSequence drawableStr = mEditTextUtils.with(this)
                                .to(mEtModifyContent)
                                .getDrawableStr(picPath);
                        mEtModifyContent.append("\n\n");
                        mEtModifyContent.append(drawableStr);
                        mEtModifyContent.append("\n\n");
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
        super.onDestroy();

        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //显示软键盘
            showSoftwareInput(v);
        } else {
            //隐藏软键盘
            hiddenSoftwareInput(v);
        }
    }


    @Override
    public void empty(String s) {
        Toast.makeText(ModifyDataActivity.this, "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successfully() {
        Toast.makeText(ModifyDataActivity.this, "成功", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void failed() {
        Toast.makeText(ModifyDataActivity.this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(List<Note> notes) {
        mNote = notes.get(0);
        mEtTitle.setText(mNote.getNoteTitle());
        mEditTextUtils.updataContent(mEtModifyContent, mNote.getNoteContent());
        mSpType.setPrompt(mNote.getNoteType());
        showAlarmTime(mNote.getAlarmTime(), mNote.isAlarm());
    }
}
