package com.example.h_dj.note.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.h_dj.note.R;
import com.example.h_dj.note.ui.fragment.AllNoteFragment;
import com.example.h_dj.note.ui.fragment.DailyFragment;
import com.example.h_dj.note.ui.fragment.EntertainmentFragment;
import com.example.h_dj.note.ui.fragment.StudyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.main_fl)
    FrameLayout mMainFl;
    @BindView(R.id.main_fab)
    FloatingActionButton mMainFab;
    @BindView(R.id.nav)
    NavigationView mNav;
    @BindView(R.id.activity_main)
    DrawerLayout mActivityMain;

    private static final int ALL = 0; //全部便签
    private static final int DAILY = 1; //日常便签
    private static final int STUDY = 2;
    private static final int ENTERTAINMENT = 3;


    private List<Fragment> mFragments;
    private Fragment currentFragment;//当前显示的fragment
    private int position = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void init() {
        super.init();
        mFragments = new ArrayList<Fragment>();
        initToolbar();
        initleftNav();
        initFragment();
        setFragment();
    }

    /**
     * 初始化fragement布局
     */
    private void initFragment() {
        mFragments.clear();
        mFragments.add(new AllNoteFragment());//0
        mFragments.add(new DailyFragment());//1
        mFragments.add(new StudyFragment());//2
        mFragments.add(new EntertainmentFragment());//3

    }

    /**
     * 初始化左侧导航菜单
     */
    private void initleftNav() {
        //设置切换按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mActivityMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivityMain.setDrawerListener(toggle);
        toggle.syncState();

        ///设置菜单的选中监听事件
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.daily:
                        position = DAILY;
                        break;
                    case R.id.study:
                        position = STUDY;
                        break;
                    case R.id.entertainment:
                        position = ENTERTAINMENT;
                        break;
                    default:
                        position = ALL;
                        break;
                }
                //设置fragment
                setFragment();
                //关闭菜单
                mActivityMain.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //  //通过NavigationView 获取头部布局来获取里面的控件
        RelativeLayout headerView = (RelativeLayout) mNav.getHeaderView(R.id.header);
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
    public void onBackPressed() {
        boolean drawerOpen = mActivityMain.isDrawerOpen(GravityCompat.START);
        if (drawerOpen) {
            mActivityMain.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivityMain.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                goTo(SearchResultActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * 设置fragment 页面
     */
    private void setFragment() {

        //1.获取fragmentManager
        FragmentManager manager = getSupportFragmentManager();
        //2.开始事务
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        //3. 替换事务
        Fragment fragment = mFragments.get(position);
        //如果当前的fragment为空；新添加一个
        if (currentFragment == null) {
            fragmentTransaction.add(R.id.main_fl, fragment).commitAllowingStateLoss();
            currentFragment = fragment;
        } else if (currentFragment != fragment) {
            //如果currentFragment不等于要添加的fragment;则隐藏currentFragment；显示fragment
            //如果被添加过；则隐藏currentFragment；显示fragment
            if (fragment.isAdded()) {
                fragmentTransaction.hide(currentFragment).show(fragment).commitAllowingStateLoss();
            } else {
                fragmentTransaction.hide(currentFragment).add(R.id.main_fl, fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    @OnClick(R.id.main_fab)
    public void onViewClicked() {
        goTo(ModifyDataActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragments != null) {
            mFragments = null;
        }
    }
}
