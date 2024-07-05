package com.meng.app;

import android.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import com.meng.app.menu.*;
import com.meng.app.task.*;
import com.meng.tools.*;
import com.meng.tools.app.*;
import com.meng.tools.app.database.*;
import com.meng.toolset.mediatool.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity instance;
    public static boolean isDebugMode = false;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mainLinearLayout;

    public boolean onWifi = false;

    public ListView rightList;
    private boolean firstOpened = false;
    private ActionBarDrawerToggle toggle;
    public int theme = 0;

    public void openLeftDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    public void openRightDrawer() {
        mDrawerLayout.openDrawer(Gravity.END);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionCatcher.getInstance().init(this);
        setContentView(com.meng.toolset.mediatool.R.layout.main_activity);
        instance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 321);
        } else {
            init();
        }
        onWifi = SystemStatus.isUsingWifi();
        isDebugMode = SharedPreferenceHelper.isDebugMode();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        rightList = (ListView) findViewById(R.id.right_list);
        mDrawerLayout.setDrawerListener(toggle);
        rightList.setAdapter(BackgroundTaskAdapter.getInstance());
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateMenu(R.menu.menu_dummy);
        MenuManager.getInstance().init(navigationView.getMenu());
        navigationView.inflateHeaderView(R.layout.drawer_header);
        ColorStateList csl = ColorStateList.valueOf(0xff000000);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        MFragmentManager.getInstance().init(this);
        MFragmentManager.getInstance().showFragment(Welcome.class);
        navigationView.getHeaderView(0).setVisibility(SharedPreferenceHelper.isShowSJF() ? View.VISIBLE : View.GONE);
        Debuger.initLogFile();
        DataBaseHelper.getInstance(MedicineDataBase.class).init(this);
        BackgroundTaskAdapter.getInstance().init(this);
        FileTool.init(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (SharedPreferenceHelper.isOpenDrawer() && hasFocus && !firstOpened) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            firstOpened = true;
        } else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerLayout.closeDrawer(GravityCompat.END);

        new TestTask()
                .setTitle("goto genshin")
                .setStatus("自动下载原神中").start();
        FunctionName.values()[item.getItemId()].doAction();
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toggle != null) {
            toggle.syncState();
        }
    }

    @Override
    public void setTheme(int resid) {
        SharedPreferenceHelper.init(this, "main");
        switch (SharedPreferenceHelper.getTheme()) {
            case "芳":
                super.setTheme(theme = R.style.green);
                break;
            case "红":
                super.setTheme(theme = R.style.red);
                break;
            case "黑":
                super.setTheme(theme = R.style.black);
                break;
            case "紫":
                super.setTheme(theme = R.style.purple);
                break;
            case "蓝":
                super.setTheme(theme = R.style.blue);
                break;
            default:
                super.setTheme(theme = R.style.green);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                mDrawerLayout.closeDrawer(Gravity.END);
            } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                exit();
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return MFragmentManager.getInstance().getCurrent().onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);

    }

    public void doVibrate(long time) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(time);
        }
    }

    public void exit() {
        if (SharedPreferenceHelper.isExit0()) {
            System.exit(0);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("缺失权限会使应用工作不正常");
                } else {
                    init();
                }
            }
        }
    }

    public void showToast(final String msgAbbr, final String msgOrigin) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Snackbar.make(mainLinearLayout, msgAbbr, 5000)
                        .setAction("查看全文", msgOrigin.trim().length() == 0 ? null : new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("全文")
                                        .setMessage(msgOrigin)
                                        .setNegativeButton("复制", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface p1, int p2) {
                                                AndroidContent.copyToClipboard(msgOrigin);
                                                showToast("复制成功");
                                            }
                                        })
                                        .setPositiveButton("确定", null).show();
                            }
                        }).show();
            }
        });
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Snackbar.make(mainLinearLayout, msg, 5000)
                        .setAction("查看全文", getLines(msg) < 2 && msg.length() < 40 ? null : new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setTitle("全文")
                                        .setMessage(msg)
                                        .setNegativeButton("复制", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface p1, int p2) {
                                                AndroidContent.copyToClipboard(msg);
                                                showToast("复制成功");
                                            }
                                        })
                                        .setPositiveButton("确定", null).show();
                            }
                        }).show();
            }
        });
    }

    private int getLines(String s) {
        int l = 0;
        for (char c : s.toCharArray()) {
            if (c == '\n') {
                ++l;
            }
        }
        return l;
    }
}
