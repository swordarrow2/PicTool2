package com.meng.toolset.boxarray;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.meng.app.*;
import com.meng.tools.*;
import com.meng.tools.MaterialDesign.*;
import com.meng.tools.app.*;
import com.meng.tools.app.database.*;
import com.meng.toolset.mediatool.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ShowAllMedicineFragment extends BaseFragment implements View.OnClickListener {

    private ListView lvMain;
    private MedicineAdapter adapter;
    private MedicineDataBase medicineDataBase;
    private FloatingMenu floatMenu;
    private FloatingButton fabAddMedicine;
    private FloatingButton fabAddBinding;
    private FloatingButton fabImport;
    private String path;


    private Runnable callBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_all_medicine_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup vg = new ViewGroup(getActivity()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };
//        FragmentManager fragmentManager= getFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.add
        lvMain = (ListView) view.findViewById(R.id.m_list);
        floatMenu = (FloatingMenu) view.findViewById(R.id.m_menu_star);
        fabAddMedicine = (FloatingButton) view.findViewById(R.id.m_fab_add_medicine);
        fabAddBinding = (FloatingButton) view.findViewById(R.id.m_fab_add_binding);
        fabImport = (FloatingButton) view.findViewById(R.id.m_fab_add_import);
        medicineDataBase = DataBaseHelper.getInstance(MedicineDataBase.class);
        floatMenu.setAnimated(true);
        floatMenu.hideMenuButton(false);
        floatMenu.setClosedOnTouchOutside(true);
        floatMenu.setIconAnimated(false);
        floatMenu.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
        lvMain.setAdapter(adapter = new MedicineAdapter(getActivity()));
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo item view
                ImageView iv = new ImageView(getActivity());
                byte[] picture = adapter.getItem(position).picture;
                if (picture != null) {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.length));
                    new AlertDialog.Builder(MainActivity.instance).setTitle("添加药品").setView(iv).show();
                }
            }
        });
        fabAddBinding.setOnClickListener(this);
        fabAddMedicine.setOnClickListener(this);
        fabImport.setOnClickListener(this);
        ThreadPool.executeAfterTime(new Runnable() {
            @Override
            public void run() {
                floatMenu.showMenuButton(true);
            }
        }, 100, TimeUnit.MILLISECONDS);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.m_fab_add_medicine:
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_medicine, null);
                final AlertDialog ad = new AlertDialog.Builder(MainActivity.instance).setTitle("添加药品").setView(view).show();
                final MDEditText name = (MDEditText) view.findViewById(R.id.add_medicine_EditText_medicine_name);
                final MDEditText describe = (MDEditText) view.findViewById(R.id.add_medicine_EditText_describe);
                final MDEditText slotId = (MDEditText) view.findViewById(R.id.add_medicine_EditText_slot_id);
                final Button select = (Button) view.findViewById(R.id.add_medicine_Button_select);
                final Button ok = (Button) view.findViewById(R.id.add_medicine_Button_ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            medicineDataBase.addMedicine(name.getString(), describe.getString(), slotId.getInt(), FileTool.readBytes(path));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        medicineDataBase.bindMachine(Calendar.getInstance().getTime().toString());
                        ad.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
                select.setOnClickListener(this);
                floatMenu.close(true);
                break;
            case R.id.m_fab_add_binding:
//                final MengBarcodeScanView view1 = new MengBarcodeScanView(getActivity());
                final EditText et = new EditText(getContext());
                final AlertDialog ad2 = new AlertDialog.Builder(MainActivity.instance).setTitle("输入序列号").setView(et)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                medicineDataBase.bindMachine(et.getText().toString());
                                floatMenu.close(true);
                            }
                        }).setNegativeButton(getResources().getString(R.string.cancel), null).show();
//                view1.setOnResultAction(new BiConsumer<String, String>() {
//                    @Override
//                    public void action(String v1, String v2) {
//                        AndroidContent.copyToClipboard(v2);
//                        ad2.dismiss();
//                        view1.onDesdroy();
//                    }
//                });
                break;
            case R.id.add_medicine_Button_select:
                callBack = new Runnable() {
                    @Override
                    public void run() {
                        ((Button) v).setText(String.format("当前图片：%s", path));
                    }
                };
                selectImage();
                break;
            case R.id.m_fab_add_import:
                callBack = new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.instance.showToast(path);
                    }
                };
                selectFile();
                break;
        }
    }

    public static class MedicineAdapter extends BaseAdapter {

        private List<MedicineDataBase.Medicine> allMedicine;
        private WeakHashMap<Integer, Bitmap> cache = new WeakHashMap<>();
        private Activity activity;

        private MedicineAdapter(Activity activity) {
            this.activity = activity;
            this.allMedicine = DataBaseHelper.getInstance(MedicineDataBase.class).getAllMedicine();
        }

        @Override
        public void notifyDataSetChanged() {
            List<MedicineDataBase.Medicine> newMe = DataBaseHelper.getInstance(MedicineDataBase.class).getAllMedicine();
            for (MedicineDataBase.Medicine medicine : newMe) {
                if (allMedicine.contains(medicine)) {
                    continue;
                }
                allMedicine.add(medicine);
            }
            super.notifyDataSetChanged();
        }

        public int getCount() {
            return allMedicine.size();
        }

        public MedicineDataBase.Medicine getItem(int position) {
            return allMedicine.get(position);
        }

        public long getItemId(int position) {
            return allMedicine.get(position).hashCode();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = activity.getLayoutInflater().inflate(R.layout.list_item_medicine, null);
                holder = new ViewHolder();
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_medicine_thumbnail);
                holder.name = (TextView) convertView.findViewById(R.id.list_item_medicine_name);
                holder.inSlot = (TextView) convertView.findViewById(R.id.list_item_medicine_in_slot);
                holder.describe = (TextView) convertView.findViewById(R.id.list_item_medicine_describe);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MedicineDataBase.Medicine medicine = allMedicine.get(position);

            holder.name.setText(medicine.name);
            holder.inSlot.setText(String.valueOf(medicine.slotId));
            holder.describe.setText(medicine.describe);
            Bitmap bitmap = null;
            if (medicine.picture != null) {
                bitmap = cache.get(medicine.id);
                if (bitmap == null) {
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(medicine.picture, 0, medicine.picture.length);
                    int nwidth = 256;    //h/w=nh/nw   h*nw/w=nh
                    int nheight = bitmap1.getHeight() * nwidth / bitmap1.getWidth();
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap1, nwidth, nheight);
                    cache.put(medicine.id, bitmap);
                }
            }
            holder.thumbnail.setImageBitmap(bitmap);

            return convertView;
        }

        private class ViewHolder {
            private ImageView thumbnail;
            private TextView name;
            private TextView inSlot;
            private TextView describe;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.SELECT_FILE_REQUEST_CODE && data.getData() != null) {
                path = AndroidContent.absolutePathFromUri(getActivity().getApplicationContext(), data.getData());
                if (callBack != null) {
                    callBack.run();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            MainActivity.instance.showToast("取消选择文件");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
