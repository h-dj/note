package com.example.h_dj.note.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.h_dj.note.App;
import com.example.h_dj.note.Listener.IListener;
import com.example.h_dj.note.Listener.ListenerManager;
import com.example.h_dj.note.R;
import com.example.h_dj.note.Listener.MainView;
import com.example.h_dj.note.adapter.MyMenuAdapter;
import com.example.h_dj.note.bean.Note;
import com.example.h_dj.note.presenter.Impl.MainPresenterImpl;
import com.example.h_dj.note.presenter.MainPresenter;
import com.example.h_dj.note.utils.LogUtil;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements MainView, MyMenuAdapter.OnItemClickListener, IListener {

    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.main_fab)
    FloatingActionButton mMainFab;
    @BindView(R.id.nav)
    NavigationView mNav;
    @BindView(R.id.activity_main)
    DrawerLayout mActivityMain;
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView mRecyclerView;


    private App mApplication;


    private MyMenuAdapter mMyAdapter;//适配器
    protected List<Note> mNotes;
    private int pos = -1;//那个类型
    //P层引用
    private MainPresenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void init() {
        super.init();
        ListenerManager.getInstance().registerListtener(this);

        mApplication = (App) this.getApplication();
        initToolbar();
        initleftNav();
        mNotes = new ArrayList<>();
        initRecyclerView();
        mPresenter = new MainPresenterImpl(this, this);
        mPresenter.loadDailyData();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LogUtil.e("initRecyclerView");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置菜单创建器。
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        mMyAdapter = new MyMenuAdapter(mNotes);
        mMyAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mMyAdapter);
    }


    /**
     * item菜单菜单点击事件
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            LogUtil.e("第:" + adapterPosition);
            if (menuPosition == 0) {
                String noteId = mNotes.get(adapterPosition).getNoteId();
                mPresenter.del(adapterPosition, noteId);
            }

        }
    };
    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {


        @Override
        public void onCreateMenu(SwipeMenu swipeRightMenu, SwipeMenu swipeLeftMenu, int viewType) {
            int size = getResources().getDimensionPixelSize(R.dimen.item_height);

            SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this)
                    .setBackgroundDrawable(R.drawable.del_selector)// 点击的背景。
                    .setText("删除") // 文字。
                    .setWidth(size) // 宽度。
                    .setHeight(size); // 高度。
            swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。
        }
    };

    /**
     * 初始化左侧导航菜单
     */
    private void initleftNav() {
        //设置切换按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mActivityMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivityMain.setDrawerListener(toggle);
        toggle.syncState();
        mNav.setCheckedItem(R.id.all);
        ///设置菜单的选中监听事件
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.daily:
                        changeData(DAILY);
                        pos = DAILY;
                        break;
                    case R.id.study:
                        changeData(STUDY);
                        pos = STUDY;
                        break;
                    case R.id.entertainment:
                        changeData(ENTERTAINMENT);
                        pos = ENTERTAINMENT;
                        break;
                    case R.id.recycle:
                        changeData(RECYCLE);
                        pos = RECYCLE;
                        break;
                    default:
                        changeData(-1);
                        pos = -1;
                        break;
                }
                mMyAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.main_fab)
    public void onViewClicked() {
        goTo(ModifyDataActivity.class);
    }

    @Override
    public void loadDataSuccess(List<Note> notes) {
        mApplication.setNoteList(notes);
        LogUtil.e("mApplication" + mApplication.getNoteList().size() + ":" + mApplication.getNoteList().get(0).getIsDel());
        changeData(pos);
        mMyAdapter.notifyDataSetChanged();
    }

    /**
     * 数据改变
     */
    private void changeData(int type) {
        mNotes.clear();
        switch (type) {
            case DAILY:
                mNotes.addAll(filterData(mApplication.getNoteList(), DAILY));
                break;
            case ENTERTAINMENT:
                mNotes.addAll(filterData(mApplication.getNoteList(), ENTERTAINMENT));
                break;
            case STUDY:
                mNotes.addAll(filterData(mApplication.getNoteList(), STUDY));
                break;
            case RECYCLE:
                mNotes.addAll(filterData(mApplication.getNoteList(), RECYCLE));
                break;
            default:
                mNotes.addAll(filterData(mApplication.getNoteList(), -1));
                break;
        }

    }


    @Override
    public void failed() {
        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delSuccess(int position) {
        LogUtil.e(position + "条");
        mNotes.get(position).setIsDel(1);
        changeData(pos);
        mMyAdapter.notifyDataSetChanged();
    }


    @Override
    public void OnClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Note", mNotes.get(position));
        bundle.putInt("position", position);
        goTo(ModifyDataActivity.class, bundle);
    }

    @Override
    public void OnLongClick(View view, int position) {
        Toast.makeText(MainActivity.this, "fds", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        ListenerManager.getInstance().unRegisterListener(this);
        if (ListenerManager.listenerManager != null) {
            ListenerManager.listenerManager = null;
        }

        if (mPresenter != null) {
            mPresenter = null;
        }
        super.onDestroy();

    }

    @Override
    public void notifyAllActivity(Object o) {
        mPresenter.loadDailyData();
        Toast.makeText(MainActivity.this, "" + o.toString(), Toast.LENGTH_SHORT).show();
    }
}
