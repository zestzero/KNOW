package android.otpc.know;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

public class CreateQuizStep1Activity extends KnowActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz_1);
		
		initView();
		initController();
	}
	
	EditText et_quiz_title, et_quiz_pass, et_quiz_time;
	Spinner sp_quiz_status, sp_sort_answer;
	TableRow row3;
	Button bt_next;
	
	public void initView() {
		super.initView(CreateQuizStep1Activity.this);
		
		row3 = (TableRow) findViewById(R.id.tableRow3);
		
		et_quiz_title = (EditText) findViewById(R.id.et_quiz_title);
		et_quiz_pass = (EditText) findViewById(R.id.et_quiz_pass);
		et_quiz_time = (EditText) findViewById(R.id.et_quiz_time);
		
		sp_quiz_status = (Spinner) findViewById(R.id.sp_quiz_status);
		sp_sort_answer = (Spinner) findViewById(R.id.sp_sort_answer);
		
		String[] quiz_status = {"โปรดเลือกลักษณะชุดข้อสอบ", "สาธารณะ", "ส่วนตัว"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateQuizStep1Activity.this, 
				android.R.layout.simple_spinner_item, quiz_status);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_quiz_status.setAdapter(adapter);
		sp_quiz_status.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
					case 2:
						row3.setVisibility(View.VISIBLE);
						break;
					default:
						row3.setVisibility(View.INVISIBLE);
						break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
		
		bt_next = (Button) findViewById(R.id.bt_next);
	}

	public void initController() {
		super.initController();
		
		bt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(et_quiz_title.getText().toString().length() == 0){
					Toast.makeText(CreateQuizStep1Activity.this, "โปรดใส่ชื่อชุดข้อสอบ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(sp_quiz_status.getSelectedItemPosition() == 0){
					Toast.makeText(CreateQuizStep1Activity.this, "โปรดเลือกลักษณะชุดข้อสอบ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(sp_quiz_status.getSelectedItemPosition() == 2 && et_quiz_pass.getText().toString().length() == 0){
					Toast.makeText(CreateQuizStep1Activity.this, "โปรดใส่รหัสผ่านสำหรับชุดข้อสอบส่วนตัว", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(et_quiz_time.getText().toString().length() == 0){
					Toast.makeText(CreateQuizStep1Activity.this, "โปรดใส่เวลาในการทำข้อสอบ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject createQuiz = new JSONObject();
				try {
					createQuiz.put("quizName", et_quiz_title.getText().toString());
					createQuiz.put("createdBy", getUserId());
					createQuiz.put("status", sp_quiz_status.getSelectedItemPosition() == 1 ? "pb" : "pr");
					createQuiz.put("password", et_quiz_pass.getText().toString());
					createQuiz.put("limitTime", et_quiz_time.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				
				Intent intent = new Intent(CreateQuizStep1Activity.this, CreateQuizStep2Activity.class);
				intent.putExtra("createQuiz", createQuiz.toString());
				startActivity(intent);
			}
		});
	}
	
}