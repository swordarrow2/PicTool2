package com.meng.eleTool.semiee;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.meng.api.semiee.ChipDescription;
import com.meng.api.semiee.SemieeApi;
import com.meng.eleTool.tools.FileTool;
import com.meng.eleTool.tools.GSON;
import java.io.File;
import java.nio.charset.StandardCharsets;
import com.meng.api.semiee.ChipInformation;

public class ChipInformationFragment extends Fragment {

    private TextView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new TextView(getActivity());
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final File fileJson = new File(getActivity().getExternalFilesDir("") + "/test_semiee_chip_information.json");
        if (!fileJson.exists()) {
            new Thread(new Runnable(){

                    @Override
                    public void run() {           
                        FileTool.saveFile(fileJson, GSON.toJson(SemieeApi.getChipInformation("8c16d867-cf5b-4442-8fc2-773f03fb02e8")).getBytes(StandardCharsets.UTF_8));
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
        ChipInformation result = GSON.fromJson(json, ChipInformation.class);
        lv.setText(result.toString());
    }
}

