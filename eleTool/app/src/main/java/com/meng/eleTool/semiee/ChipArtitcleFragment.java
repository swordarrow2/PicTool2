package com.meng.eleTool.semiee;

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
import com.meng.eleTool.R;
import com.meng.eleTool.tools.FileTool;
import com.meng.eleTool.tools.GSON;
import java.io.File;
import java.util.List;
import com.meng.eleTool.tools.Network;
import com.meng.api.semiee.SemieeApi;
import java.nio.charset.StandardCharsets;
import com.meng.api.semiee.ChipArticle;

public class ChipArtitcleFragment extends Fragment {

    private TextView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new TextView(getActivity());
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final File fileJson = new File(getActivity().getExternalFilesDir("") + "/test_semiee_chip_article.json");
        if (!fileJson.exists()) {
            new Thread(new Runnable(){

                    @Override
                    public void run() {           
                        FileTool.saveFile(fileJson, GSON.toJson(SemieeApi.getChipArticle("8c16d867-cf5b-4442-8fc2-773f03fb02e8")).getBytes(StandardCharsets.UTF_8));
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
        ChipArticle result = GSON.fromJson(json, ChipArticle.class);
        lv.setText(result.result.feature);
    }
}

