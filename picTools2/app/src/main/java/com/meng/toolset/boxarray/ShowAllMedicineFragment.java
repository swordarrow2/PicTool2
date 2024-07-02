package com.meng.toolset.boxarray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meng.app.BaseFragment;
import com.meng.app.Constant;
import com.meng.app.MainActivity;
import com.meng.tools.AndroidContent;
import com.meng.tools.FileTool;
import com.meng.tools.MaterialDesign.FloatingButton;
import com.meng.tools.MaterialDesign.FloatingMenu;
import com.meng.tools.MaterialDesign.MDEditText;
import com.meng.tools.app.database.DataBaseHelper;
import com.meng.tools.app.database.MedicineDataBase;
import com.meng.toolset.mediatool.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.WeakHashMap;

public class ShowAllMedicineFragment extends BaseFragment {

    private ListView lvMain;
    private ShowAllAdapter adapter;
    private MedicineDataBase medicineDataBase;
    private FloatingMenu menuStar;
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
        lvMain = (ListView) view.findViewById(R.id.m_list);
        menuStar = (FloatingMenu) view.findViewById(R.id.m_menu_star);
        fabAddMedicine = (FloatingButton) view.findViewById(R.id.m_fab_add_medicine);
        fabAddBinding = (FloatingButton) view.findViewById(R.id.m_fab_add_binding);
        fabImport = (FloatingButton) view.findViewById(R.id.m_fab_add_import);
        medicineDataBase = DataBaseHelper.getInstance(MedicineDataBase.class);
        menuStar.setAnimated(true);
        menuStar.hideMenuButton(false);
        menuStar.setClosedOnTouchOutside(true);
        menuStar.setIconAnimated(false);
        menuStar.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
        lvMain.setAdapter(adapter = new ShowAllAdapter(getActivity()));
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent2 = new Intent(getActivity(), ModelEditActivity.class);
//                intent2.putExtra("id", adapter.getItem(position).id);
//                startActivity(intent2);
            }
        });
        fabAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack = new Runnable() {
                            @Override
                            public void run() {
                                select.setText(String.format("当前图片：%s", path));
                            }
                        };
                        selectImage();
                    }
                });
                menuStar.close(true);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menuStar.showMenuButton(true);
            }
        }, 150);
    }

    public static class ShowAllAdapter extends BaseAdapter {

        private List<MedicineDataBase.Medicine> allMedicine;
        private WeakHashMap<Integer, Bitmap> cache = new WeakHashMap<>();
        private Activity activity;

        private ShowAllAdapter(Activity activity) {
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
            Bitmap bitmap = null;// = bmpCache.get(result.mThumbnail);
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
            MainActivity.instance.showToast("取消选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
