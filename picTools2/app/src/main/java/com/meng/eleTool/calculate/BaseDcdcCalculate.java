package com.meng.eleTool.calculate;

import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.mediatool.*;
import com.meng.view.*;

public abstract class BaseDcdcCalculate extends BaseFragment {

    private LinearLayout ll;
    private TextView title;
    private TextView result;
    private Button btn;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.boost_calculate, null);
        ll = inflate.findViewById(R.id.boost_calculateLinearLayout);
        title = inflate.findViewById(R.id.boost_calculateTextView);
        result = inflate.findViewById(R.id.boost_calculateTextViewResult);
        btn = inflate.findViewById(R.id.boost_calculateButton);
        return inflate;
    }

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public final void setTitle(CharSequence s) {
        title.setText(s);
    }
    
    public final void setResult(CharSequence s) {
        result.setText(s);
    }

    public final void setButtonClick(OnClickListener listener) {
        btn.setOnClickListener(listener);
    }

    public abstract void init();


    public final SjfEditText addEditText(String hint) {
        SjfEditText et = new SjfEditText(getActivity());
        et.setHint(hint);
        TextView tv = new TextView(getActivity());
        tv.setText("\n" + hint);
        ll.addView(tv);
        ll.addView(et);
        return et;                          
    }

}
