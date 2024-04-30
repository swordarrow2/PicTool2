package com.meng.eleTool.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.meng.eleTool.R;
import com.meng.eleTool.dao.GlobalData;
import com.meng.eleTool.model.Model;
import com.meng.eleTool.model.Record;
import com.meng.eleTool.tools.SharedPreferenceHelper;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by SJF on 2023/2/7.
 */

public class RecordEditActivity extends Activity {
	protected static final int DATE_DIALOG_ID = 0;
	private EditText etModelId;
	private EditText etCount;
	private RadioButton rbAdd, rbRemove;
	private Button btnOk;
	private Button btnSelect;

	private TextView etDate;
	private int mYear;
	private int mMonth;
	private int mDay;
    private int theme;
    
    @Override
    public void setTheme(int resid) { 
        switch (SharedPreferenceHelper.getString("color", "芳")) {
            case "芳":
                super.setTheme(theme = R.style.green);
                break;
            case "红":
                super.setTheme(theme = R.style.red);
                break;
            case "黑":
                super.setTheme(theme = R.style.black);
                break;
            case "紫":
                super.setTheme(theme = R.style.purple);
                break;
            case "蓝":
                super.setTheme(theme = R.style.blue);
                break;
            default:
                super.setTheme(theme = R.style.green);
                break;
        }
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_record);
		etModelId = (EditText) findViewById(R.id.add_recordEditText_model_id);
		etCount = (EditText) findViewById(R.id.add_recordEditText_count);
		etDate = (TextView) findViewById(R.id.add_recordEditText_date);

		rbAdd = (RadioButton) findViewById(R.id.add_recordRadioButton_in);
		rbRemove = (RadioButton) findViewById(R.id.add_recordRadioButton_out);

		btnOk = (Button) findViewById(R.id.add_recordButton_ok);
		btnSelect = (Button) findViewById(R.id.add_recordButton_select);
		etDate.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View p1) {
                    showDialog(DATE_DIALOG_ID);
                }
            });
		btnSelect.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View p1) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Model model : GlobalData.getModelDao().getAll()) {
                        list.add(model.name);
                    }
                    final String[] items = list.toArray(new String[0]);

                    new AlertDialog.Builder(RecordEditActivity.this).setTitle("标题")
						.setItems(items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dia, int which) {
								etModelId.setText(items[which]);
							}
						}).create().show();
                }
            });
		btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Model model = GlobalData.getModelDao().get(etModelId.getText().toString());
					int num = -1;
                    try {
                        num = Integer.parseInt(etCount.getText().toString());
                    } catch (NumberFormatException e) {

                    }
                    if (model == null || num == -1) {
                        Toast.makeText(RecordEditActivity.this, "输入正确的型号和数量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                	if (rbAdd.isChecked()) {
                        GlobalData.getRecordDao().save(new Record(0, model.id, "i", num, etDate.getText().toString()));
                        Toast.makeText(RecordEditActivity.this, "入库添加成功", Toast.LENGTH_SHORT).show();
                    } else if (rbRemove.isChecked()) {
                        GlobalData.getRecordDao().save(new Record(0, model.id, "o", num, etDate.getText().toString()));
                        Toast.makeText(RecordEditActivity.this, "出库添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecordEditActivity.this, "需选择出入库", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		etDate.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
	}
}
