package com.meng.eleTool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.meng.eleTool.R;
import com.meng.eleTool.dao.GlobalData;
import com.meng.eleTool.model.Model;
import com.meng.eleTool.tools.SharedPreferenceHelper;
import java.util.HashSet;

/**
 * Created by SJF on 2023/2/7.
 */

public class ModelEditActivity extends Activity {
	private EditText etName;
	private AutoCompleteTextView etPackage;
	private AutoCompleteTextView etType;
	private EditText etPrint;
	private EditText etFrom;
	private EditText etNote;
	private Button btnSave;
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
		Intent intent = getIntent();
		final int elementID = intent.getIntExtra("id", -1);
		setContentView(R.layout.add_element);

		etName = (EditText) findViewById(R.id.add_elementEditText_model);
		etPackage = (AutoCompleteTextView) findViewById(R.id.add_elementEditText_package);
		etPrint = (EditText) findViewById(R.id.add_elementEditText_print);
		etNote = (EditText) findViewById(R.id.add_elementEditText_note);
		etType = (AutoCompleteTextView) findViewById(R.id.add_elementEditText_type);
		etFrom = (EditText) findViewById(R.id.add_elementEditText_from);
		btnSave = (Button) findViewById(R.id.add_elementButton_ok);
		final Model ele = GlobalData.getModelDao().get(elementID);
		if (ele != null) {
			etName.setText(ele.name);
			etPackage.setText(ele.pack);
			etPrint.setText(ele.print);
			etNote.setText(ele.note);
			etType.setText(ele.type);
			etFrom.setText(ele.from);
		}
		HashSet<String> as = new HashSet<>();
		for (Model e : GlobalData.getModelDao().getAll()) {
			as.add(e.pack);
		}
		etPackage.setAdapter(
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, as.toArray(new String[0])));
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String strName = etName.getText().toString();
				if (GlobalData.getModelDao().isNameExist(strName)) {
					Toast.makeText(ModelEditActivity.this, "型号已存在", Toast.LENGTH_SHORT).show();
					return;
				}
				String strPackage = etPackage.getText().toString();
				String strType = etType.getText().toString();
				String strPrint = etPrint.getText().toString();
				String strFrom = etFrom.getText().toString();
				String strNote = etNote.getText().toString();
				if (strName.isEmpty()) {
					Toast.makeText(ModelEditActivity.this, "请输入必要的信息", Toast.LENGTH_SHORT).show();
					return;
				}
				Model element = new Model(0, strName, strPackage.toUpperCase(), strType, strPrint, strFrom,
						strNote.trim());
				GlobalData.getModelDao().save(element);
				Toast.makeText(ModelEditActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
