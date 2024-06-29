package com.meng.toolset.eleTool.calculate;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import com.meng.api.semiee.*;
import com.meng.app.BaseFragment;
import com.meng.app.MFragmentManager;
import com.meng.toolset.eleTool.semiee.*;
import com.meng.toolset.mediatool.*;
import com.meng.tools.*;
import com.meng.tools.app.ThreadPool;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class SearchSemieeFragment extends BaseFragment {

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
            new Thread(new Runnable() {

                @Override
                public void run() {
                    FileTool.saveToFile(fileJson, GSON.toJson(SemieeApi.search("sc8802")).getBytes(StandardCharsets.UTF_8));
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                init(fileJson);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }).start();
        } else {
            try {
                init(fileJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void init(File fileJson) throws IOException {
        String json = FileTool.readString(fileJson);
        final SearchResult result = GSON.fromJson(json, SearchResult.class);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                final SearchResult.Result r = result.result.get(p3);
                ThreadPool.execute(new Runnable() {

                    @Override
                    public void run() {
                        //       ChipParameter cp = SemieeApi.getChipParameter(r.id);
                        //       AndroidContent.copyToClipboard(cp.toString());

                    }
                });
                //   MFragmentManager.getInstance().showFragment(ChipArtitcleFragment.class);
                MFragmentManager.getInstance().showFragment(ChipDescriptionFragment.class);

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

            holder.tvId.setText(String.format(Locale.CHINA, "id:%s", element.id));
            holder.tvModel.setText(String.format(Locale.CHINA, "型号:%s", element.model));
            holder.tvBrand.setText(String.format(Locale.CHINA, "厂家:%s", element.brand_name));
            holder.tv_fk_t_brand_id.setText(String.format(Locale.CHINA, "fk_t_brand_id:%d", element.fk_t_brand_id));
            holder.tvUpdateTime.setText(String.format(Locale.CHINA, "update_time:%s", element.update_time));
            holder.tvRemark.setText(String.format(Locale.CHINA, "remark:%s", element.remark));
            holder.tvSimilar.setText(String.format(Locale.CHINA, "similar_num:%d", element.similar_num));
            holder.tvChoiceSimilar.setText(String.format(Locale.CHINA, "choice_similar_num:%d", element.choice_similar_num));

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
