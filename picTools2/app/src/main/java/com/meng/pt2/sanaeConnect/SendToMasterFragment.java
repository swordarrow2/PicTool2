package com.meng.pt2.sanaeConnect;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.pt2.*;
import com.meng.pt2.sanaeConnect.*;
import com.meng.pt2.tools.*;
import java.io.*;

public class SendToMasterFragment extends Fragment {
    private Button btnSend;
	private EditText text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_to_master, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		text = (EditText)view.findViewById(R.id.send_to_masterEditText);
        btnSend = (Button) view.findViewById(R.id.send_to_masterButton);
		btnSend.setOnClickListener(onClick);
	}

	OnClickListener onClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.send_to_masterButton:
					BotDataPack bdp=BotDataPack.encode(BotDataPack.sendToMaster);
					bdp.write("    " + text.getText().toString());
					if (MainActivity2.instence.sanaeConnect.isClosed()) {
						MainActivity2.instence.sanaeConnect.connect();
					}
					MainActivity2.instence.sanaeConnect.send(bdp.getData());
					LogTool.t("发送成功");
					text.setText("");
					break;	
			}
		}
	};
}
