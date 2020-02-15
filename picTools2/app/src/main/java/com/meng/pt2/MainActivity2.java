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
import com.meng.pt2.libAndHelper.*;
import com.meng.pt2.ocr.*;
import com.meng.pt2.pixivPictureDownloader.*;
import com.meng.pt2.qrCode.creator.*;
import com.meng.pt2.qrCode.reader.*;
import com.meng.pt2.sauceNao.*;
import com.meng.pt2.upgrade.*;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;


public class MainActivity2 extends AppCompatActivity {

    public static MainActivity2 instence;
    private DrawerLayout mDrawerLayout;
    public LinearLayout mainLinearLayout;

    public boolean onWifi = false;

    public FragmentManager manager;

    private Welcome welcomeFragment;
    private LogoQRCreator logoCreatorFragment;
    public AwesomeCreator awesomeCreatorFragment;
    public CameraQRReader cameraReaderFragment;
    public GalleryQRReader galleryReaderFragment;
    private AnimGIFAwesomeQr gifAwesomeFragment;
    private ArbAwesomeCreator arbAwesomeFragment;
    public GIFCreator gifCreatorFragment;
    private SettingsPreference settingsFragment;
    private AnimGIFArbAwesome gifArbAwesomeFragment;
    private pictureEncry pictureEncryFragment;
    private pictureDecry pictureDecryFragment;
    public PixivDownloadMain pixivDownloadMainFragment;
    public SauceNaoMain sauceNaoMain;
    private OcrMain ocrMain;

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
		showAwesomeFragment(false);
        showWelcome(true);
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
			new UpgradeClient().connect();
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
					showWelcome(true);
					break;
                case R.id.read_barcode:
					new AlertDialog.Builder(MainActivity2.this)
						.setTitle("读取方式")
						.setPositiveButton("从相册", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								showGalleryReaderFragment(true);
							}
						}).setNegativeButton("从相机", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showCameraReaderFragment(true);
								cameraReaderFragment.onResume();
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
									showLogoCreatorFragment(true);
								} else {
									if (rbArb.isChecked()) {
										if (rbAnim.isChecked()) {
											showGifArbAwesomeFragment(true);
										} else {
											showArbFragmentFragment(true);
										}
									} else {
										if (rbAnim.isChecked()) {
											showGifAwesomeFragment(true);
										} else {
											showAwesomeFragment(true);
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
								showPicEncryFragment(true);
							}
						}).setNegativeButton("解密", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showPicDecryFragment(true);
							}
						}).show();
					break;
                case R.id.encode_gif:
					showGifFragment(true);
					break;
                case R.id.sauce_nao:
					showSauceNaoMainFragment(true);
					break;
                case R.id.ocr:
					showOCRMainFragment(true);
					break;
                case R.id.settings:
					showSettingsFragment(true);
					break;
                case R.id.pixiv_download:
					showPixivDownloadFragment(true);
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

    private void showWelcome(boolean showNow) {
        FragmentTransaction transactionWelcome = manager.beginTransaction();
        if (welcomeFragment == null) {
            welcomeFragment = new Welcome();
            transactionWelcome.add(R.id.fragment, welcomeFragment);
		}
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(welcomeFragment);
		}
        transactionWelcome.commit();
	}

    private void showGalleryReaderFragment(boolean showNow) {
        FragmentTransaction transactionGalleryReaderFragment = manager.beginTransaction();
        if (galleryReaderFragment == null) {
            galleryReaderFragment = new GalleryQRReader();
            transactionGalleryReaderFragment.add(R.id.fragment, galleryReaderFragment);
		}
        hideFragment(transactionGalleryReaderFragment);
        if (showNow) {
            transactionGalleryReaderFragment.show(galleryReaderFragment);
		}
        transactionGalleryReaderFragment.commit();
	}

    private void showCameraReaderFragment(boolean showNow) {
        FragmentTransaction transactionCameraReaderFragment = manager.beginTransaction();
        if (cameraReaderFragment == null) {
            cameraReaderFragment = new CameraQRReader();
            transactionCameraReaderFragment.add(R.id.fragment, cameraReaderFragment);
		}
        hideFragment(transactionCameraReaderFragment);
        if (showNow) {
            transactionCameraReaderFragment.show(cameraReaderFragment);
		}
        transactionCameraReaderFragment.commit();
	}

    private void showLogoCreatorFragment(boolean showNow) {
        FragmentTransaction transactionLogoCreatorFragment = manager.beginTransaction();
        if (logoCreatorFragment == null) {
            logoCreatorFragment = new LogoQRCreator();
            transactionLogoCreatorFragment.add(R.id.fragment, logoCreatorFragment);
		}
        hideFragment(transactionLogoCreatorFragment);
        if (showNow) {
            transactionLogoCreatorFragment.show(logoCreatorFragment);
		}
        transactionLogoCreatorFragment.commit();
	}

    public void showAwesomeFragment(boolean showNow) {
        FragmentTransaction transactionAwesomeCreatorFragment = manager.beginTransaction();
        if (awesomeCreatorFragment == null) {
            awesomeCreatorFragment = new AwesomeCreator();
            transactionAwesomeCreatorFragment.add(R.id.fragment, awesomeCreatorFragment);
		}
        hideFragment(transactionAwesomeCreatorFragment);
        if (showNow) {
            transactionAwesomeCreatorFragment.show(awesomeCreatorFragment);
		}
        transactionAwesomeCreatorFragment.commit();
	}

    private void showGifAwesomeFragment(boolean showNow) {
        FragmentTransaction transactionGifAwesomeCreatorFragment = manager.beginTransaction();
        if (gifAwesomeFragment == null) {
            gifAwesomeFragment = new AnimGIFAwesomeQr();
            transactionGifAwesomeCreatorFragment.add(R.id.fragment, gifAwesomeFragment);
		}
        hideFragment(transactionGifAwesomeCreatorFragment);
        if (showNow) {
            transactionGifAwesomeCreatorFragment.show(gifAwesomeFragment);
		}
        transactionGifAwesomeCreatorFragment.commit();
	}

    private void showArbFragmentFragment(boolean showNow) {
        FragmentTransaction transactionTestFragment = manager.beginTransaction();
        if (arbAwesomeFragment == null) {
            arbAwesomeFragment = new ArbAwesomeCreator();
            transactionTestFragment.add(R.id.fragment, arbAwesomeFragment);
		}
        hideFragment(transactionTestFragment);
        if (showNow) {
            transactionTestFragment.show(arbAwesomeFragment);
		}
        transactionTestFragment.commit();
	}

    private void showGifArbAwesomeFragment(boolean showNow) {
        FragmentTransaction transactionGifArbAwesomeFragment = manager.beginTransaction();
        if (gifArbAwesomeFragment == null) {
            gifArbAwesomeFragment = new AnimGIFArbAwesome();
            transactionGifArbAwesomeFragment.add(R.id.fragment, gifArbAwesomeFragment);
		}
        hideFragment(transactionGifArbAwesomeFragment);
        if (showNow) {
            transactionGifArbAwesomeFragment.show(gifArbAwesomeFragment);
		}
        transactionGifArbAwesomeFragment.commit();
	}

    private void showGifFragment(boolean showNow) {
        FragmentTransaction transactionGifFragment = manager.beginTransaction();
        if (gifCreatorFragment == null) {
            gifCreatorFragment = new GIFCreator();
            transactionGifFragment.add(R.id.fragment, gifCreatorFragment);
		}
        hideFragment(transactionGifFragment);
        if (showNow) {
            transactionGifFragment.show(gifCreatorFragment);
		}
        transactionGifFragment.commit();
	}

    private void showSettingsFragment(boolean showNow) {
        FragmentTransaction transactionsettings = manager.beginTransaction();
        if (settingsFragment == null) {
            settingsFragment = new SettingsPreference();
            transactionsettings.add(R.id.fragment, settingsFragment);
		}
        hideFragment(transactionsettings);
        if (showNow) {
            transactionsettings.show(settingsFragment);
		}
        transactionsettings.commit();
	}

    private void showPicEncryFragment(boolean showNow) {
        FragmentTransaction transactionBus = manager.beginTransaction();
        if (pictureEncryFragment == null) {
            pictureEncryFragment = new pictureEncry();
            transactionBus.add(R.id.fragment, pictureEncryFragment);
		}
        hideFragment(transactionBus);
        if (showNow) {
            transactionBus.show(pictureEncryFragment);
		}
        transactionBus.commit();
	}

    public void showPixivDownloadFragment(boolean showNow) {
        FragmentTransaction transactionBusR = manager.beginTransaction();
        if (pixivDownloadMainFragment == null) {
            pixivDownloadMainFragment = new PixivDownloadMain();
            transactionBusR.add(R.id.fragment, pixivDownloadMainFragment);
		}
        hideFragment(transactionBusR);
        if (showNow) {
            transactionBusR.show(pixivDownloadMainFragment);
		}
        transactionBusR.commit();
	}

    private void showPicDecryFragment(boolean showNow) {
        FragmentTransaction transactionBusR = manager.beginTransaction();
        if (pictureDecryFragment == null) {
            pictureDecryFragment = new pictureDecry();
            transactionBusR.add(R.id.fragment, pictureDecryFragment);
		}
        hideFragment(transactionBusR);
        if (showNow) {
            transactionBusR.show(pictureDecryFragment);
		}
        transactionBusR.commit();
	}

    private void showSauceNaoMainFragment(boolean showNow) {
        FragmentTransaction transactionBusR = manager.beginTransaction();
        if (sauceNaoMain == null) {
            sauceNaoMain = new SauceNaoMain();
            transactionBusR.add(R.id.fragment, sauceNaoMain);
		}
        hideFragment(transactionBusR);
        if (showNow) {
            transactionBusR.show(sauceNaoMain);
		}
        transactionBusR.commit();
	}

    private void showOCRMainFragment(boolean showNow) {
        FragmentTransaction transactionBusR = manager.beginTransaction();
        if (ocrMain == null) {
            ocrMain = new OcrMain();
            transactionBusR.add(R.id.fragment, ocrMain);
		}
        hideFragment(transactionBusR);
        if (showNow) {
            transactionBusR.show(ocrMain);
		}
        transactionBusR.commit();
	}


    public void hideFragment(FragmentTransaction transaction) {
        Fragment fs[] = {
			welcomeFragment,
			logoCreatorFragment,
			awesomeCreatorFragment,
			gifAwesomeFragment,
			cameraReaderFragment,
			galleryReaderFragment,
			arbAwesomeFragment,
			gifCreatorFragment,
			gifArbAwesomeFragment,
			settingsFragment,
			pictureEncryFragment,
			pictureDecryFragment,
			pixivDownloadMainFragment,
			sauceNaoMain,
			ocrMain
		};
        for (Fragment f : fs) {
            if (f != null) {
                transaction.hide(f);
                if (f instanceof CameraQRReader) {
                    f.onPause();
				}
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
        if (arbAwesomeFragment != null && arbAwesomeFragment.isVisible()) {
            arbAwesomeFragment.onKeyDown(keyCode, event);
            return true;
		}
        if (gifArbAwesomeFragment != null && gifArbAwesomeFragment.isVisible()) {
            gifArbAwesomeFragment.onKeyDown(keyCode, event);
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

