package com.example.h_dj.note.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h_dj.note.R;
import com.example.h_dj.note.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by H_DJ on 2017/5/5.
 */

public class ModifyDataActivity extends BaseActivity {


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


    /**
     * Note类型
     */
    private List<String> noteTypes;

    private boolean isAlarmClock = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify;
    }

    @Override
    public void init() {
        super.init();
        initToolbar();
        initNoteTypesData();
        initSpanner();
        initModifyTime();
    }

    /**
     * 初始化Note修改时间
     */
    private void initModifyTime() {
        //获取当前时间
        String format = new SimpleDateFormat("yyyy/MM/dd hh:mm").format(new Date());
        mTvModifyTime.setText(format);
    }

    /**
     * 初始化Note类型数据
     */
    private void initNoteTypesData() {
        noteTypes = new ArrayList<>();
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
                if (isAlarmClock) {
                    //获取当前时间
                    String format = new SimpleDateFormat("yyyy/MM/dd hh:mm").format(new Date());
                    mTvAlarmClockTime.setText(format + "");
                    //显示体醒时间TextView
                    mTvAlarmClockTime.setVisibility(View.VISIBLE);
                } else {
                    mTvAlarmClockTime.setVisibility(View.GONE);
                }
                isAlarmClock = !isAlarmClock;
                Toast.makeText(ModifyDataActivity.this, "提醒", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                this.finish();
                break;
            case R.id.save:

                String string = mEtModifyContent.getText().toString();
                LogUtil.e(string + "");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    @OnClick({R.id.tv_video, R.id.tv_photo, R.id.tv_voice, R.id.tv_doodle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_video:
                break;
            case R.id.tv_photo:
                break;
            case R.id.tv_voice:
                break;
            case R.id.tv_doodle:
                break;
        }
    }


}
