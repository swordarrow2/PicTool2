package com.meng.pt2;

import android.app.*;
import android.content.*;
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
import com.meng.pt2.encryAndDecry.*;
import com.meng.pt2.gif.*;
import com.meng.pt2.ocr.*;
import com.meng.pt2.pixivPictureDownloader.*;
import com.meng.pt2.qrCode.creator.*;
import com.meng.pt2.qrCode.reader.*;
import com.meng.pt2.sanaeConnect.*;
import com.meng.pt2.sauceNao.*;
import com.meng.pt2.tools.*;
import java.lang.reflect.*;
import java.util.*;

import android.app.AlertDialog;
import android.support.v7.widget.Toolbar;


public class MainActivity2 extends AppCompatActivity {

    public static MainActivity2 instence;
	public SanaeConnect sanaeConnect;
    private DrawerLayout mDrawerLayout;
    public LinearLayout mainLinearLayout;

    public boolean onWifi = false;

    public FragmentManager manager;

	private HashMap<String,Fragment> fragments=new HashMap<>();

    public final int CROP_REQUEST_CODE = 3;
    public final int SELECT_FILE_REQUEST_CODE = 822;
    public TextView rightText;

    private ActionBarDrawerToggle toggle;
    public int theme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instence = this;
        DataBaseHelper.init(this);
        FileHelper.init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        manager = getFragmentManager();
        rightText = (TextView) findViewById(R.id.main_activityTextViewRight);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        ColorStateList csl;
        switch (theme) {
            case R.style.green:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_green);
				break;
            case R.style.red:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_red);
				break;
            case R.style.blue:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_blue);
				break;
            case R.style.black:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_black);
				break;
            case R.style.purple:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_purple);
				break;
            default:
				csl = getResources().getColorStateList(R.color.navigation_menu_item_color_green);
				break;
		}
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
		FragmentTransaction trans = manager.beginTransaction();
		AwesomeCreator frag=new AwesomeCreator();
		fragments.put(AwesomeCreator.class.getName(), frag);
		trans.add(R.id.fragment, frag);
		trans.commit();
        showFragment(Welcome.class);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        onWifi = wifiNetworkInfo.isConnected();
        mDrawerLayout.openDrawer(GravityCompat.START);
        navigationView.setCheckedItem(R.id.first_page);
        if (SharedPreferenceHelper.getBoolean("opendraw", true)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
		} else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
		}
        navigationView.getHeaderView(0).setVisibility(SharedPreferenceHelper.getBoolean("showSJF", false) ? View.VISIBLE : View.GONE);
		try {
			sanaeConnect = new SanaeConnect();
			sanaeConnect.connect();
			new Thread(new Runnable(){

					@Override
					public void run() {
						while (true) {
							try {
								Thread.sleep(30000);
								sanaeConnect.send(BotDataPack.encode(-2).getData());
							} catch (InterruptedException e) {

							}
						}
					}
				}).start();
		} catch (Exception e) {
			LogTool.e(e);
		}
	}

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.closeDrawer(GravityCompat.END);
            switch (item.getItemId()) {
                case R.id.first_page:
					showFragment(Welcome.class);
					break;
                case R.id.read_barcode:
					new AlertDialog.Builder(MainActivity2.this)
						.setTitle("读取方式")
						.setPositiveButton("从相册", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								showFragment(GalleryQRReader.class);
							}
						}).setNegativeButton("从相机", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showFragment(CameraQRReader.class);
								fragments.get(CameraQRReader.class).onResume();
							}
						}).show();
					break;
                case R.id.create_barcode:
					final View view1 = getLayoutInflater().inflate(R.layout.select_qr_function, null);
					RadioGroup r1 = (RadioGroup) view1.findViewById(R.id.select_qr_function_g1);
					final RadioGroup r2 = (RadioGroup) view1.findViewById(R.id.select_qr_function_g2);
					final RadioGroup r3 = (RadioGroup) view1.findViewById(R.id.select_qr_function_g3);
					r2.setEnabled(false);
					r3.setEnabled(false);
					r1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								if (group.getCheckedRadioButtonId() == R.id.select_qr_function_normal_qr) {
									r2.setVisibility(View.GONE);
									r3.setVisibility(View.GONE);
								} else {
									r2.setVisibility(View.VISIBLE);
									r3.setVisibility(View.VISIBLE);
								}
							}
						});
					final RadioButton rbNormal = (RadioButton) view1.findViewById(R.id.select_qr_function_normal_qr);
					final RadioButton rbAnim = (RadioButton) view1.findViewById(R.id.select_qr_function_anim);
					final RadioButton rbArb = (RadioButton) view1.findViewById(R.id.select_qr_function_arb);
					new AlertDialog.Builder(MainActivity2.this)
						.setTitle("选择二维码类型")
						.setView(view1)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								if (rbNormal.isChecked()) {
									showFragment(LogoQRCreator.class);
								} else {
									if (rbArb.isChecked()) {
										if (rbAnim.isChecked()) {
											showFragment(AnimGIFArbAwesome.class);
										} else {
											showFragment(ArbAwesomeCreator.class);
										}
									} else {
										if (rbAnim.isChecked()) {
											showFragment(AnimGIFAwesomeQr.class);
										} else {
											showFragment(AwesomeCreator.class);
										}
									}
								}
							}
						}).setNegativeButton("返回", null).show();
					break;
                case R.id.encry_and_decry:
					new AlertDialog.Builder(MainActivity2.this)
						.setTitle("图片加密解密")
						.setPositiveButton("加密", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								showFragment(pictureEncry.class);
							}
						}).setNegativeButton("解密", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showFragment(pictureDecry.class);
							}
						}).show();
					break;
                case R.id.encode_gif:
					showFragment(GIFCreator.class);
					break;
                case R.id.sauce_nao:
					showFragment(SauceNaoMain.class);
					break;
                case R.id.ocr:
					showFragment(OcrMain.class);
					break;
				case R.id.crash:
					showFragment(CrashUploadFragment.class);
					break;
                case R.id.settings:
					showFragment(SettingsPreference.class);
					break;
                case R.id.pixiv_download:
					showFragment(PixivDownloadMain.class);
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
		return (T)fragments.get(c.getName());
	}

	public <T extends Fragment> void showFragment(Class<T> c) {
		FragmentTransaction transactionWelcome = manager.beginTransaction();
		Fragment frag=fragments.get(c.getName());
		if (frag == null) {
			try {
				Class<?> cls = Class.forName(c.getName());
				frag = (Fragment) cls.newInstance();
				fragments.put(c.getName(), frag);
				transactionWelcome.add(R.id.fragment, frag);
			} catch (Exception e) {
				throw new RuntimeException("反射爆炸");
			}
		}
        hideFragment(transactionWelcome);
		transactionWelcome.show(frag);
        transactionWelcome.commit();
	}

    public void hideFragment(FragmentTransaction transaction) {
        for (Fragment f : fragments.values()) {
			transaction.hide(f);
			if (f instanceof CameraQRReader) {
				f.onPause();
			}
		}
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
	}

    @Override
    public void setTheme(int resid) {
        switch (SharedPreferenceHelper.getValue("color", "芳")) {
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
        if (getFragment(ArbAwesomeCreator.class) != null && getFragment(ArbAwesomeCreator.class).isVisible()) {
            getFragment(ArbAwesomeCreator.class).onKeyDown(keyCode, event);
            return true;
		}
        if (getFragment(AnimGIFArbAwesome.class) != null && getFragment(AnimGIFArbAwesome.class).isVisible()) {
            getFragment(AnimGIFArbAwesome.class).onKeyDown(keyCode, event);
            return true;
		}
        return super.onKeyDown(keyCode, event);
	}

    public void doVibrate(long time) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(time);
	}

    public void exit() {
        if (SharedPreferenceHelper.getBoolean("exitsettings")) {
            System.exit(0);
		} else {
            finish();
		}
	}
}

