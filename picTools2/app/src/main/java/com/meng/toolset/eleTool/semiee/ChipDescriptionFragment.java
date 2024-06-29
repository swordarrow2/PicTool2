package com.meng.toolset.eleTool.semiee;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.meng.api.semiee.*;
import com.meng.app.BaseFragment;
import com.meng.tools.*;
import java.io.*;
import java.nio.charset.*;

public class ChipDescriptionFragment extends BaseFragment {

    private TextView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lv = new TextView(getActivity());
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final File fileJson = new File(getActivity().getExternalFilesDir("") + "/test_semiee_chip_description.json");
        if (!fileJson.exists()) {
            new Thread(new Runnable(){

                    @Override
                    public void run() {           
                        FileTool.saveToFile(fileJson, GSON.toJson(SemieeApi.getChipDescription("8c16d867-cf5b-4442-8fc2-773f03fb02e8")).getBytes(StandardCharsets.UTF_8));
                        getActivity().runOnUiThread(new Runnable(){

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
        ChipDescription result = GSON.fromJson(json, ChipDescription.class);
        lv.setText(result.result.feature);
        AndroidContent.copyToClipboard(result.toString());
    }
}

