package com.meng.eleTool;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
 

import java.util.ArrayList;
import java.util.List;

import com.meng.eleTool.dao.GlobalData;
import com.meng.eleTool.model.Model;
import com.meng.eleTool.model.Record;

public class ShowAllRecordFragment extends Fragment {

    private TabHost tabHost;

    private List<Record> listAll = new ArrayList<Record>();
    private List<Record> listIn = new ArrayList<Record>();
    private List<Record> listOut = new ArrayList<Record>();

    private ShowAllAdapter adapterAll = new ShowAllAdapter(listAll);
    private ShowAllAdapter adapterIn = new ShowAllAdapter(listIn);
    private ShowAllAdapter adapterOut = new ShowAllAdapter(listOut);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabhost, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabHost = (TabHost) view.findViewById(R.id.mtabhost);
        tabHost.setup();
        LayoutInflater.from(getActivity()).inflate(R.layout.listview, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("全部", null).setContent(R.id.listviewListView));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("入库", null).setContent(R.id.listviewListView2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("出库", null).setContent(R.id.listviewListView3));

        ((ListView) view.findViewById(R.id.listviewListView)).setAdapter(adapterAll);
        ((ListView) view.findViewById(R.id.listviewListView2)).setAdapter(adapterIn);
        ((ListView) view.findViewById(R.id.listviewListView3)).setAdapter(adapterOut);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Record> list = GlobalData.getRecordDao().getAll();

        listAll.clear();
        listIn.clear();
        listOut.clear();
        listAll.addAll(list);
        for (Record r : list) {
            if (r.io.equals("i")) {
                listIn.add(r);
            } else if (r.io.equals("o")) {
                listOut.add(r);
            } else {
                throw new RuntimeException("not in or out");
            }
        }
        adapterAll.notifyDataSetChanged();
        adapterIn.notifyDataSetChanged();
        adapterOut.notifyDataSetChanged();

    }

    private class ShowAllAdapter extends BaseAdapter {

        private List<Record> records;

        public ShowAllAdapter(List<Record> records) {
            this.records = records;
        }

        public int getCount() {
            return records.size();
        }

        public Record getItem(int position) {
            return records.get(position);
        }

        public long getItemId(int position) {
            return records.get(position).hashCode();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.element_list_item, null);

                holder = new ViewHolder();
                holder.tvId = (TextView) convertView.findViewById(R.id.element_list_itemTextView_id);
                holder.tvModel = (TextView) convertView.findViewById(R.id.element_list_itemTextView_model);
                holder.tvPack = (TextView) convertView.findViewById(R.id.element_list_itemTextView_pack);
                holder.tvPrint = (TextView) convertView.findViewById(R.id.element_list_itemTextView_print);
                holder.tvType = (TextView) convertView.findViewById(R.id.element_list_itemTextView_type);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.element_list_itemImageView);
                holder.tvNote = (TextView) convertView.findViewById(R.id.element_list_itemTextView_note);
                holder.tvCount = (TextView) convertView.findViewById(R.id.element_list_itemTextView_count);
                holder.tvDate = (TextView) convertView.findViewById(R.id.element_list_itemTextView_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Record record = records.get(position);
            Model element = GlobalData.getModelDao().get(record.model_id);
            holder.tvId.setText(record._id + "");
            holder.tvModel.setText("型号:" + element.name);
            holder.tvPack.setText("封装" + element.pack);
            holder.tvPrint.setText("数量:" + record.nums);
            holder.tvType.setText(element.type);
            holder.tvCount.setText("");
            holder.tvDate.setText(record.date);
            if (record.io.equals("i")) {
                holder.tvNote.setText("入库");
                holder.ivHead.setImageResource(android.R.drawable.arrow_down_float);
            } else {
                holder.tvNote.setText("出库");
                holder.ivHead.setImageResource(android.R.drawable.arrow_up_float);
            }
            return convertView;
        }

        private class ViewHolder {
            private ImageView ivHead;
            private TextView tvId;
            private TextView tvModel;
            private TextView tvPack;
            private TextView tvCount;
            private TextView tvPrint;
            private TextView tvType;
            private TextView tvDate;
            private TextView tvNote;
        }
    }
}
