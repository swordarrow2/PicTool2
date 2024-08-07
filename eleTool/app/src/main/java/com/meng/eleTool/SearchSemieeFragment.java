package com.meng.eleTool;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.meng.api.semiee.SearchResult;
import com.meng.api.semiee.SemieeApi;
import com.meng.eleTool.tools.FileTool;
import com.meng.eleTool.tools.GSON;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Adapter;
import com.meng.eleTool.semiee.ChipArtitcleFragment;
import com.meng.eleTool.semiee.ChipDescriptionFragment;
import com.meng.eleTool.semiee.ChipInformationFragment;

public class SearchSemieeFragment extends Fragment {

    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new ListView(getActivity());
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final File fileJson = new File(getActivity().getExternalFilesDir("") + "/test_semiee_search.json");
        if (!fileJson.exists()) {
            new Thread(new Runnable(){

                    @Override
                    public void run() {
                        FileTool.saveFile(fileJson, GSON.toJson(SemieeApi.search("sc8802")).getBytes(StandardCharsets.UTF_8));
                        getActivity().runOnUiThread(new Runnable(){

                                @Override
                                public void run() {
                                    init(fileJson);
                                }
                            });
                    }
                }).start();
        } else {
            init(fileJson);
        }

    }
    private void init(File fileJson) {
        String json = FileTool.readString(fileJson);
        final SearchResult result = GSON.fromJson(json, SearchResult.class);
        lv.setOnItemClickListener(new OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                    SearchResult.Result r=result.result.get(p3);
                    MainActivity ma=(MainActivity) getActivity();
                    ma.showFragment(ChipInformationFragment.class);
                }
            });
        lv.setAdapter(new SemieeSearchAdapter(result.result));
    }
    
    private class SemieeSearchAdapter extends BaseAdapter {

        private List<SearchResult.Result> allModel;


        public SemieeSearchAdapter(List<SearchResult.Result> list) {
            allModel = list;
        }

        public int getCount() {
            return allModel.size();
        }

        public SearchResult.Result getItem(int position) {
            return allModel.get(position);
        }

        public long getItemId(int position) {
            return allModel.get(position).hashCode();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.search_semiee_result_list_item, null);
                holder = new ViewHolder();
                holder.tvId = (TextView) convertView.findViewById(R.id.element_list_itemTextView_id);
                holder.tvModel = (TextView) convertView.findViewById(R.id.element_list_itemTextView_model);
                holder.tvBrand = (TextView) convertView.findViewById(R.id.element_list_itemTextView_pack);
                holder.tv_fk_t_brand_id = (TextView) convertView.findViewById(R.id.element_list_itemTextView_count);
                holder.tvUpdateTime = (TextView) convertView.findViewById(R.id.element_list_itemTextView_print);
                holder.tvRemark = (TextView) convertView.findViewById(R.id.element_list_itemTextView_type);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.element_list_itemImageView);
                holder.tvSimilar = (TextView) convertView.findViewById(R.id.element_list_itemTextView_note);
                holder.ivHead.setImageResource(android.R.drawable.btn_star);
                holder.tvChoiceSimilar = (TextView) convertView.findViewById(R.id.element_list_itemTextView_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SearchResult.Result element = allModel.get(position);

            holder.tvId.setText("id:" + element.id);
            holder.tvModel.setText("型号:" + element.model);
            holder.tvBrand.setText("厂家:" + element.brand_name);
            holder.tv_fk_t_brand_id.setText("fk_t_brand_id:" + element.fk_t_brand_id);
            holder.tvUpdateTime.setText("update_time:" + element.update_time);
            holder.tvRemark.setText("remark:" + element.remark);
            holder.tvSimilar.setText("similar_num:" + element.similar_num);
            holder.tvChoiceSimilar.setText("choice_similar_num:" + element.choice_similar_num);

            return convertView;
        }

        private class ViewHolder {
            private ImageView ivHead;
            private TextView tvId;
            private TextView tvModel;
            private TextView tvBrand;
            private TextView tv_fk_t_brand_id;
            private TextView tvUpdateTime;
            private TextView tvRemark;
            private TextView tvChoiceSimilar;
            private TextView tvSimilar;
        }
	}

}
