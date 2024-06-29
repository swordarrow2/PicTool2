package com.meng.app;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.meng.toolset.mediatool.R;

public class Welcome extends BaseFragment {

    private TextView textView;

    public Welcome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.aboutTextView);
        welcome();
    }

    private void welcome() {
        textView.setText("选择想要使用的功能吧");
    }
}

