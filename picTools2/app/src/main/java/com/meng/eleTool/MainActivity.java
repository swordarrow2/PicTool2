package com.meng.eleTool;

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
import com.meng.eleTool.calculate.*;
import com.meng.mediatool.*;
import com.meng.mediatool.tools.*;
import java.util.*;
import java.util.concurrent.*;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import com.meng.mediatool.R;

public class MainActivity extends AppCompatActivity {

	public static MainActivity instance;
	private DrawerLayout mDrawerLayout;
	public LinearLayout mainLinearLayout;

	public boolean onWifi = false;

	public FragmentManager manager;

	private HashMap<String, Fragment> fragments = new HashMap<>();

	public final int CROP_REQUEST_CODE = 3;
	public final int SELECT_FILE_REQUEST_CODE = 822;
	private boolean firstOpened = false;
	private ActionBarDrawerToggle toggle;
	public int theme = 0;
	public ExecutorService threadPool = Executors.newFixedThreadPool(20);
    // private DexClassLoader classLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		instance = this;
		ExceptionCatcher.getInstance().init(this);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 321);
		}
		init();

	}

	private void init() {
//        classLoader = new DexClassLoader("/storage/emulated/0/AppProjects/android/SJF_calculator/HotFix/app/build/bin/app.apk",
//                                         getDataDir().getAbsolutePath(),
//                                         null,
//                                         getClassLoader());

		FileTool.init(this);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                                           R.string.navigation_drawer_close);
		manager = getFragmentManager();
        mDrawerLayout.setDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

       	ColorStateList csl = ColorStateList.valueOf(0xff111111);
		navigationView.setItemTextColor(csl);
		navigationView.setItemIconTintList(csl);

//        Menu menu = navigationView.getMenu();
//        MenuItem item = menu.add(11,11,11,"搜索半导小芯");
//        item.setIcon(R.drawable.ic_menu);

		showFragment(Welcome.class);
		onWifi = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		navigationView.getHeaderView(0)
            .setVisibility(SharedPreferenceHelper.getBoolean("showSJF", false) ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (SharedPreferenceHelper.getBoolean("opendraw", true) && hasFocus && !firstOpened) {
			mDrawerLayout.openDrawer(GravityCompat.START);
			firstOpened = true;
		} else {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		}
	}

	NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
		private AlertDialog dcdcDialog;

		@Override
		public boolean onNavigationItemSelected(MenuItem item) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
			mDrawerLayout.closeDrawer(GravityCompat.END);
            String[] items = {
                "电感值选择",
                "电感电流计算",
                "输入滤波电容选择",
                "输出滤波电容选择"
            };
            ListView dcdcList = new ListView(MainActivity.this);
            dcdcList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items));


			switch (item.getItemId()) {
//                case R.id.add_model_history:
//                    intent = new Intent(MainActivity.this, RecordEditActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.model_history:
//                    showFragment(ShowAllRecordFragment.class);
//                    break;
//                case R.id.add_model_kind:
//                    intent = new Intent(MainActivity.this, ModelEditActivity.class);
//                    startActivity(intent);
//                    break;
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
                                        showFragment(BoostInductSelect.class);
                                        break;
                                    case 1:
                                        showFragment(BoostInductorCurrent.class);
                                        break;
                                    case 2:
                                        showFragment(BoostInputCapacitanceChoose.class);
                                        break;
                                    case 3:
                                        showFragment(BoostOutputCapacitanceChoose.class);
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
                                        showFragment(BuckInductSelect.class);
                                        break;
                                    case 1:
                                        showFragment(BuckInductorCurrent.class);
                                        break;
                                    case 2:
                                        showFragment(BuckInputCapacitanceChoose.class);
                                        break;
                                    case 3:
                                        showFragment(BuckOutputCapacitanceChoose.class);
                                        break;
                                }
                            }
                        });
                    break;
                case R.id.farad:
                    showFragment(FaradCapacitanceCalculate.class);
                    break;
//                case R.id.search_semiee:
//                    showFragment(SearchSemieeFragment.class);
//                    break;
//                case R.id.local_datasheet:
//
//                    break;
                case R.id.settings:
                    showFragment(SettingsPreference.class);
                    break;
                case R.id.exit:
                    exit();
                    break;
			}
			return true;
		}
	};

	public void selectImage(Fragment f) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		f.startActivityForResult(intent, SELECT_FILE_REQUEST_CODE);
	}

	public <T extends Fragment> T getFragment(Class<T> c) {
		return (T) fragments.get(c.getName());
	}

	public <T extends Fragment> Fragment showFragment(Class<T> c) {
		FragmentTransaction transactionWelcome = manager.beginTransaction();
		Fragment frag = fragments.get(c.getName());
		if (frag == null) {
			try {
//				Class<?> cls = classLoader.loadClass(c.getName());
                Class<?> cls = getClassLoader().loadClass(c.getName());			                
                frag = (Fragment) cls.newInstance();
				fragments.put(c.getName(), frag);
				transactionWelcome.add(R.id.fragment, frag);
            } catch (Exception e) {
                try {
                    Class<?> cls = Class.forName(c.getName());
                    frag = (Fragment) cls.newInstance();
                    fragments.put(c.getName(), frag);
                    transactionWelcome.add(R.id.fragment, frag);
                } catch (Exception e1) {
                    throw new RuntimeException("反射爆炸");
                }
            }
        }
		hideFragment();
		transactionWelcome.show(frag);
		transactionWelcome.commit();
        return frag;
    }

	public void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
		for (Fragment f : fragments.values()) {
            ft.hide(f);
        }
		ft.commit();
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
		switch (SharedPreferenceHelper.getString("color", "芳")) {
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
		return super.onKeyDown(keyCode, event);
    }

	public void doVibrate(long time) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
    }

	public void exit() {
        if (SharedPreferenceHelper.getBoolean("exitsettings")) {
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
                                           new AlertDialog.Builder(MainActivity.this).setIcon(R.mipmap.ic_launcher).setTitle("全文")
                                               .setMessage(msgOrigin)
                                               .setNegativeButton("复制", new DialogInterface.OnClickListener() {

                                                   @Override
                                                   public void onClick(DialogInterface p1, int p2) {
                                                       AndroidContent.copyToClipboard(msgOrigin);
                                                       showToast("复制成功");
                                                   }
                                               }).setPositiveButton("确定", null).show();
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
                                           new AlertDialog.Builder(MainActivity.this).setIcon(R.mipmap.ic_launcher).setTitle("全文")
                                               .setMessage(msg).setNegativeButton("复制", new DialogInterface.OnClickListener() {

                                                   @Override
                                                   public void onClick(DialogInterface p1, int p2) {
                                                       AndroidContent.copyToClipboard(msg);
                                                       showToast("复制成功");
                                                   }
                                               }).setPositiveButton("确定", null).show();
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
