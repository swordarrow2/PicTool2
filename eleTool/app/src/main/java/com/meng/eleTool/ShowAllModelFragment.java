package com.meng.eleTool;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.meng.eleTool.activity.ModelEditActivity;
import com.meng.eleTool.dao.GlobalData;
import com.meng.eleTool.model.Model;
import com.meng.eleTool.model.Record;
import java.util.HashMap;
import java.util.List;

public class ShowAllModelFragment extends Fragment {


	private ListView lv;
	private ShowAllAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		lv = new ListView(getActivity());
		return lv;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		lv.setAdapter(adapter = new ShowAllAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent2 = new Intent(getActivity(), ModelEditActivity.class);
				intent2.putExtra("id", adapter.getItem(position).id);
				startActivity(intent2);
			}
		});
	}

	private class ShowAllAdapter extends BaseAdapter {

		private List<Model> allModel;
		private HashMap<Model, Integer> modelMap = new HashMap<>();

		private void init() {
			allModel = GlobalData.getModelDao().getAll();
			for (Model model : allModel) {
				modelMap.put(model, 0);
			}
			List<Record> records = GlobalData.getRecordDao().getAll();

			for (Record r : records) {
				Model model = GlobalData.getModelDao().get(r.model_id);
				if (model == null) {
					continue;
				}
				Integer num = modelMap.get(model);
				if (num == null) {
					continue;
				}
				modelMap.put(model, num + r.nums);
			}
		}

		@Override
		public void notifyDataSetChanged() {
			modelMap.clear();
			init();
			super.notifyDataSetChanged();
		}

		public ShowAllAdapter() {
			init();
		}

		public int getCount() {
			return allModel.size();
		}

		public Model getItem(int position) {
			return allModel.get(position);
		}

		public long getItemId(int position) {
			return allModel.get(position).hashCode();
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.element_list_item, null);
				holder = new ViewHolder();
				holder.tvId = (TextView) convertView.findViewById(R.id.element_list_itemTextView_id);
				holder.tvModel = (TextView) convertView.findViewById(R.id.element_list_itemTextView_model);
				holder.tvPack = (TextView) convertView.findViewById(R.id.element_list_itemTextView_pack);
				holder.tvCount = (TextView) convertView.findViewById(R.id.element_list_itemTextView_count);
				holder.tvPrint = (TextView) convertView.findViewById(R.id.element_list_itemTextView_print);
				holder.tvType = (TextView) convertView.findViewById(R.id.element_list_itemTextView_type);
				holder.ivHead = (ImageView) convertView.findViewById(R.id.element_list_itemImageView);
				holder.tvNote = (TextView) convertView.findViewById(R.id.element_list_itemTextView_note);
				holder.ivHead.setImageResource(android.R.drawable.btn_star);
				holder.tvDate = (TextView) convertView.findViewById(R.id.element_list_itemTextView_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Model element = allModel.get(position);

			holder.tvId.setText("编号:" + element.id);
			holder.tvModel.setText("型号:" + element.name);
			holder.tvPack.setText("封装:" + element.pack);
			holder.tvCount.setText("库存:" + modelMap.get(element));
			holder.tvPrint.setText("丝印:" + element.print + "来源:" + element.from);
			holder.tvType.setText("类型:" + element.type);
			holder.tvNote.setText("备注:" + element.note);
			holder.tvDate.setText("");

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

	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

}
