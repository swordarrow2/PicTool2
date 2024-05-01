package com.meng.mediatool;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.meng.ai.*;
import com.meng.eleTool.calculate.*;
import com.meng.mediatool.*;
import com.meng.mediatool.picture.*;
import com.meng.mediatool.picture.barcode.*;
import com.meng.mediatool.picture.gif.*;
import com.meng.mediatool.picture.pixiv.*;
import com.meng.mediatool.picture.saucenao.*;
import com.meng.mediatool.task.*;
import com.meng.mediatool.tools.*;
import com.meng.mediatool.wallpaper.*;
import java.io.*;

import android.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import com.meng.mediatool.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity instance;
	private DrawerLayout mDrawerLayout;
    public LinearLayout mainLinearLayout;

    public boolean onWifi = false;

    public ListView rightList;
	private boolean firstOpened = false;
    private ActionBarDrawerToggle toggle;
    public int theme = 0;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instance = this;
        FileFormat.init();
        BackgroundTaskAdapter.getInstance().init(this);
        FileTool.init(this);
		ExceptionCatcher.getInstance().init(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 321);
        } else {
			init();
		}
	}

	private void init() {
		DataBaseHelper.init(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        rightList = (ListView) findViewById(R.id.right_list);
        mDrawerLayout.setDrawerListener(toggle);
        rightList.setAdapter(BackgroundTaskAdapter.getInstance());
        TextView tv = new TextView(this);
        tv.setText("后台任务");
        rightList.addHeaderView(tv);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateMenu(R.menu.menu_drawer);
        navigationView.inflateHeaderView(R.layout.drawer_header);
        ColorStateList csl = ColorStateList.valueOf(0xff000000);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        MFragmentManager.getInstance().init(this);
        MFragmentManager.getInstance().showFragment(Welcome.class);
        onWifi = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        navigationView.getHeaderView(0).setVisibility(SharedPreferenceHelper.isShowSJF() ? View.VISIBLE : View.GONE);
        File logF = new File(Environment.getExternalStorageDirectory() + "/log.txt");
        File logFe = new File(Environment.getExternalStorageDirectory() + "/loge.txt");
        if (SharedPreferenceHelper.isSaveDebugLog()) {
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(logF));
                System.setOut(ps);
                PrintStream pse = new PrintStream(new FileOutputStream(logFe));
                System.setErr(pse);
            } catch (FileNotFoundException e) {
                showToast("log文件创建失败");
            }
        } else {
            logF.delete();
            logFe.delete();
        }
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

    private AlertDialog ad;
    private AlertDialog dcdcDialog;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerLayout.closeDrawer(GravityCompat.END);

        new TestTask()
            .setTitle("goto genshin")
            .setStatus("自动下载原神中").start();

        ListView dcdcList = new ListView(MainActivity.this);
        dcdcList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.dcdc_cal_type)));

        switch (item.getItemId()) {
            case R.id.barcode:
                ListView lv = new ListView(MainActivity.this);
                lv.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.create_type)));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                            ad.dismiss();
                            mDrawerLayout.openDrawer(GravityCompat.START);
                            switch (p3) {
                                case 0:
                                    MFragmentManager.getInstance().showFragment(BarcodeNormal.class);      
                                    break;
                                case 1:
                                    MFragmentManager.getInstance().showFragment(BarcodeAwesome.class);
                                    break;
                                case 2:
                                    MFragmentManager.getInstance().showFragment(BarcodeAwesomeArb.class);      
                                    break;
                                case 3:
                                    MFragmentManager.getInstance().showFragment(BarcodeAwesomeGif.class);
                                    break;
                                case 4:
                                    MFragmentManager.getInstance().showFragment(BarcodeAwesomeArbGif.class);
                                    break;
                                case 5:
                                    MFragmentManager.getInstance().showFragment(BarcodeReaderGallery.class);
                                    break;
                            }
                        }
                    });
                ad = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("选择操作")
                    .setView(lv)
                    .show(); 
                break;
            case R.id.crypt:
                MFragmentManager.getInstance().showFragment(PictureCrypt.class);
                break;
            case R.id.gray_pic:
                MFragmentManager.getInstance().showFragment(GrayImage.class);
                break;
            case R.id.encode_gif:
                MFragmentManager.getInstance().showFragment(GIFCreator.class);
                break;
            case R.id.sauce_nao:
                MFragmentManager.getInstance().showFragment(SauceNaoMain.class);
                break;
//            case R.id.ocr:  疼殉已弃用该API
//                MFragmentManager.getInstance().showFragment(OcrMain.class);
//                break;
            case R.id.pic_format_convert:
                MFragmentManager.getInstance().showFragment(FfmpegFragment.class);
                break;
            case R.id.push_rtmp:
                MFragmentManager.getInstance().showFragment(RtmpFragment.class);
                break;
            case R.id.settings:
                MFragmentManager.getInstance().showSettingFragment();
                break;
            case R.id.pixiv_download:
                MFragmentManager.getInstance().showFragment(PixivDownloadMain.class);
                break;
            case R.id.videowallpaper:
                MFragmentManager.getInstance().showFragment(WallpaperMain.class);
                break;
            case R.id.semiee:
                MFragmentManager.getInstance().showFragment(SearchSemieeFragment.class);
                break;
            case R.id.boost:

                dcdcDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("选择操作")
                    .setView(dcdcList)
                    .show(); 
                dcdcList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                            dcdcDialog.dismiss(); 
                            switch (p3) {
                                case 0:
                                    MFragmentManager.getInstance().showFragment(BoostInductSelect.class);
                                    break;
                                case 1:
                                    MFragmentManager.getInstance().showFragment(BoostInductorCurrent.class);
                                    break;
                                case 2:
                                    MFragmentManager.getInstance().showFragment(BoostInputCapacitanceChoose.class);
                                    break;
                                case 3:
                                    MFragmentManager.getInstance().showFragment(BoostOutputCapacitanceChoose.class);
                                    break;
                            }
                        }
                    });
                break;
            case R.id.buck:
                dcdcDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("选择操作")
                    .setView(dcdcList)
                    .show(); 
                dcdcList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                            dcdcDialog.dismiss(); 
                            switch (p3) {
                                case 0:
                                    MFragmentManager.getInstance().showFragment(BuckInductSelect.class);
                                    break;
                                case 1:
                                    MFragmentManager.getInstance().showFragment(BuckInductorCurrent.class);
                                    break;
                                case 2:
                                    MFragmentManager.getInstance().showFragment(BuckInputCapacitanceChoose.class);
                                    break;
                                case 3:
                                    MFragmentManager.getInstance().showFragment(BuckOutputCapacitanceChoose.class);
                                    break;
                            }
                        }
                    });

                break;
            case R.id.farad:
                MFragmentManager.getInstance().showFragment(FaradCapacitanceCalculate.class);
                break;
//                case R.id.search_semiee:
//                    showFragment(SearchSemieeFragment.class);
//                    break;
//                case R.id.local_datasheet:
//
//                    break;
            case R.id.vits:
                MFragmentManager.getInstance().showFragment(VitsConnectFragment.class);
                break;
            case R.id.androidtts:
                MFragmentManager.getInstance().showFragment(TtsFragment.class);
                break;
            case R.id.exit:
                exit();
                break;	
        }
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
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                exit();
			} else {
                mDrawerLayout.openDrawer(GravityCompat.START);
			}
            return true;
		}
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
			} else {
                mDrawerLayout.openDrawer(GravityCompat.START);
			}
            return true;
		}

        if (MFragmentManager.getInstance().getCurrent().onKeyDown(keyCode, event)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
	}

    public void doVibrate(long time) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(time);
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
						.setAction("查看全文", msgOrigin.trim().length() == 0 ?null: new View.OnClickListener(){

									   @Override
									   public void onClick(View v) {
										   new AlertDialog.Builder(MainActivity.this)
											   .setIcon(R.mipmap.ic_launcher)
											   .setTitle("全文")
											   .setMessage(msgOrigin)
											   .setNegativeButton("复制", new DialogInterface.OnClickListener(){

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
						.setAction("查看全文", getLines(msg) < 2 && msg.length() < 40 ?null: new View.OnClickListener(){

									   @Override
									   public void onClick(View v) {
										   new AlertDialog.Builder(MainActivity.this)
											   .setIcon(R.mipmap.ic_launcher)
											   .setTitle("全文")
											   .setMessage(msg)
											   .setNegativeButton("复制", new DialogInterface.OnClickListener(){

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
		int l=0;
		for (char c:s.toCharArray()) {
			if (c == '\n') {
				++l;
			}
		}
		return l;
	}
}
