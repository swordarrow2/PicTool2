package com.meng.mediatool;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.mediatool.picture.*;
import com.meng.mediatool.picture.gif.*;
import com.meng.mediatool.picture.ocr.*;
import com.meng.mediatool.picture.pixivPictureDownloader.*;
import com.meng.mediatool.picture.qrCode.*;
import com.meng.mediatool.picture.sauceNao.*;
import com.meng.mediatool.tools.*;
import java.util.*;
import java.util.concurrent.*;

import android.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import com.meng.mediatool.R;


public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;
	private DrawerLayout mDrawerLayout;
    public LinearLayout mainLinearLayout;

    public boolean onWifi = false;

    public FragmentManager manager;

	private HashMap<String,Fragment> fragments=new HashMap<>();

    public final int CROP_REQUEST_CODE = 3;
    public final int SELECT_FILE_REQUEST_CODE = 822;
    public TextView rightText;
	private boolean firstOpened = false;
    private ActionBarDrawerToggle toggle;
    public int theme = 0;
	public ExecutorService threadPool = Executors.newFixedThreadPool(20);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instance = this;FileHelper.init(this);
		//ExceptionCatcher.getInstance().init(this);
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
        manager = getFragmentManager();
        rightText = (TextView) findViewById(R.id.main_activityTextViewRight);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        ColorStateList csl = ColorStateList.valueOf(0xff000000);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
		FragmentTransaction trans = manager.beginTransaction();
		BarcodeAwesome frag=new BarcodeAwesome();
		fragments.put(BarcodeAwesome.class.getName(), frag);
		trans.add(R.id.fragment, frag);
		trans.commit();
        showFragment(Welcome.class);
        onWifi = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        navigationView.getHeaderView(0).setVisibility(SharedPreferenceHelper.getBoolean("showSJF", false) ? View.VISIBLE : View.GONE);
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

		AlertDialog ad;

		private void setView(View ll, int bmpId, String text) {
			Bitmap bmp;
			if (bmpId == 0) {
				bmp = null;
			} else {
				bmp = BitmapFactory.decodeResource(getResources(), bmpId);
			}
			ImageView im = (ImageView) ((LinearLayout)ll).getChildAt(0);
			TextView tv = (TextView) ((LinearLayout)ll).getChildAt(1);
			im.setImageBitmap(bmp);
			tv.setText(text);
			ll.setOnClickListener(ocl);
		}

		View.OnClickListener ocl = new View.OnClickListener(){

			@Override
			public void onClick(View p1) {
				switch (p1.getId()) {
					case R.id.qr_op_selectLinearLayout_normal:
						//break;
						//case R.id.qr_op_selectLinearLayout_in_pic:
						//break;
					case R.id.qr_op_selectLinearLayout_normal_pic:
						showFragment(BarcodeNormal.class);		
						break;
					case R.id.qr_op_selectLinearLayout_awesome:
						showFragment(BarcodeAwesome.class);
						break;
					case R.id.qr_op_selectLinearLayout_awesome_arb:
						showFragment(BarcodeAwesomeArb.class);		
						break;
					case R.id.qr_op_selectLinearLayout_awesome_gif:
						showFragment(BarcodeAwesomeGif.class);
						break;
					case R.id.qr_op_selectLinearLayout_awesome_arb_gif:
						showFragment(BarcodeAwesomeArbGif.class);
						break;
					case R.id.qr_op_selectLinearLayout_read:
						showFragment(GalleryQRReader.class);
						break;
				}
				ad.dismiss();
			}
		};

		@Override
        public boolean onNavigationItemSelected(MenuItem item) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.closeDrawer(GravityCompat.END);
            switch (item.getItemId()) {
				case R.id.barcode:
					View view1 = getLayoutInflater().inflate(R.layout.qr_op_select, null);
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_normal), R.drawable.barcode_normal, "普通");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_normal_pic), R.drawable.barcode_normal_pic, "普通");
					//setView(view1.findViewById(R.id.qr_op_selectLinearLayout_in_pic), null, "在图片中");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_awesome), R.drawable.barcode_awesome, "普通图");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_awesome_arb), R.drawable.barcode_awesome_arb, "普通图");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_awesome_gif), R.drawable.barcode_awesome, "动态图");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_awesome_arb_gif), R.drawable.barcode_awesome_arb, "动态图");
					setView(view1.findViewById(R.id.qr_op_selectLinearLayout_read), 0, "读取二维码");
					//setView(view1.findViewById(R.id.qr_op_selectLinearLayout_read_camera), 0, "从相机读取");
					ad = new AlertDialog.Builder(MainActivity.this).setTitle("选择二维码类型").setView(view1).show();
					break;
                case R.id.crypt:
					showFragment(PictureCrypt.class);
					break;
				case R.id.gray_pic:
					showFragment(BinImg.class);
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
        hideFragment();
		transactionWelcome.show(frag);
        transactionWelcome.commit();
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
        if (getFragment(BarcodeAwesomeArb.class) != null && getFragment(BarcodeAwesomeArb.class).isVisible()) {
            getFragment(BarcodeAwesomeArb.class).onKeyDown(keyCode, event);
            return true;
		}
        if (getFragment(BarcodeAwesomeArbGif.class) != null && getFragment(BarcodeAwesomeArbGif.class).isVisible()) {
            getFragment(BarcodeAwesomeArbGif.class).onKeyDown(keyCode, event);
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
