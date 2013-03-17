package android.otpc.know;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class CreateQuizStep2Activity extends KnowActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz_2);
		
		initView();
		initController();
	}
	
	private Button bt_next, bt_previous;
	private TableRow row2, row3;
	private EditText et_num_question_order_1, et_num_question_order_2, et_num_question_order_3;
	private Spinner sp_question_order_1, sp_question_order_2, sp_question_order_3;
	private List<String> question_type_master = Arrays.asList("โปรดเลือกประเภทของคำถาม", "ข้อสอบหลายตัวเลือก", "ข้อสอบหลายคำตอบ", "ข้อสอบถูกผิด");
	private List<String> question_type_secodary;
	private List<String> question_type_third;
	
	public void initView() {
		super.initView(CreateQuizStep2Activity.this);
		
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_previous = (Button) findViewById(R.id.bt_previous);
		
		row2 = (TableRow) findViewById(R.id.tableRow2);
		row3 = (TableRow) findViewById(R.id.tableRow3);
		
		sp_question_order_1 = (Spinner) findViewById(R.id.sp_question_order_1);
		sp_question_order_2 = (Spinner) findViewById(R.id.sp_question_order_2);
		sp_question_order_3 = (Spinner) findViewById(R.id.sp_question_order_3);
		
		et_num_question_order_1 = (EditText) findViewById(R.id.et_num_question_order_1);
		et_num_question_order_2 = (EditText) findViewById(R.id.et_num_question_order_2);
		et_num_question_order_3 = (EditText) findViewById(R.id.et_num_question_order_3);
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CreateQuizStep2Activity.this, 
				android.R.layout.simple_spinner_item, question_type_master);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_question_order_1.setAdapter(adapter1);
		sp_question_order_1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
					case 0:
						row2.setVisibility(View.INVISIBLE);
						row3.setVisibility(View.INVISIBLE);
						et_num_question_order_2.setVisibility(View.INVISIBLE);
						et_num_question_order_3.setVisibility(View.INVISIBLE);
						break;
					default:
						row2.setVisibility(View.VISIBLE);
						question_type_secodary = new ArrayList<String>(question_type_master);
						question_type_secodary.remove(arg2);
						question_type_secodary.set(0, "--------------------");
						
						ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CreateQuizStep2Activity.this, 
								android.R.layout.simple_spinner_item, question_type_secodary);
						adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sp_question_order_2.setAdapter(adapter2);
						sp_question_order_2.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								switch (arg2) {
									case 0:
										row3.setVisibility(View.INVISIBLE);
										et_num_question_order_2.setVisibility(View.INVISIBLE);
										et_num_question_order_3.setVisibility(View.INVISIBLE);
										break;
									default:
										row3.setVisibility(View.VISIBLE);
										et_num_question_order_2.setVisibility(View.VISIBLE);
										question_type_third = new ArrayList<String>(question_type_secodary);
										question_type_third.remove(arg2);
										question_type_secodary.set(0, "--------------------");
										
										ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CreateQuizStep2Activity.this, 
												android.R.layout.simple_spinner_item, question_type_third);
										adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
										sp_question_order_3.setAdapter(adapter3);
										sp_question_order_3.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
												switch (arg2) {
													case 0:
														et_num_question_order_3.setVisibility(View.INVISIBLE);
														break;
													default:
														et_num_question_order_3.setVisibility(View.VISIBLE);
														break;
												}
											}

											@Override
											public void onNothingSelected(AdapterView<?> arg0) { }
										});
										break;
								}
							}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) { }
						});
						break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
	}

	public void initController() {
		super.initController();
		
		bt_previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		bt_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(sp_question_order_1.getSelectedItemPosition() == 0){
					Toast.makeText(CreateQuizStep2Activity.this, "โปรดเลือกประเภทของคำถาม", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent intent = new Intent(CreateQuizStep2Activity.this, CreateQuizStep3Activity.class);
				
				if(et_num_question_order_1.getText().toString().length() == 0 || 
						et_num_question_order_1.getText().toString().equalsIgnoreCase("0")){
					Toast.makeText(CreateQuizStep2Activity.this, "โปรดใส่จำนวนข้อของลำดับที่ 1", Toast.LENGTH_SHORT).show();
					return;
				} else {
					intent.putExtra(getQuestionType(sp_question_order_1.getSelectedItem().toString()), Integer.parseInt(et_num_question_order_1.getText().toString()));
				}
				
				if(et_num_question_order_2.getVisibility() == View.VISIBLE){
					if(et_num_question_order_2.getText().toString().length() == 0 || 
							et_num_question_order_2.getText().toString().equalsIgnoreCase("0")){
						Toast.makeText(CreateQuizStep2Activity.this, "โปรดใส่จำนวนข้อของลำดับที่ 2", Toast.LENGTH_SHORT).show();
						return;
					}
					intent.putExtra(getQuestionType(sp_question_order_2.getSelectedItem().toString()), Integer.parseInt(et_num_question_order_2.getText().toString()));
				}
				
				if(et_num_question_order_3.getVisibility() == View.VISIBLE){
					if(et_num_question_order_3.getText().toString().length() == 0 || 
							et_num_question_order_3.getText().toString().equalsIgnoreCase("0")){
						Toast.makeText(CreateQuizStep2Activity.this, "โปรดใส่จำนวนข้อของลำดับที่ 3", Toast.LENGTH_SHORT).show();
						return;
					}
					intent.putExtra(getQuestionType(sp_question_order_3.getSelectedItem().toString()), Integer.parseInt(et_num_question_order_3.getText().toString()));
				}
				
				intent.putExtra("createQuiz", getIntent().getStringExtra("createQuiz"));
				startActivity(intent);
			}
		});
	}
	
	private String getQuestionType(String text){
		if(text.equalsIgnoreCase("ข้อสอบหลายตัวเลือก"))
			return "MChoice";
		if(text.equalsIgnoreCase("ข้อสอบหลายคำตอบ"))
			return "MChoose";
		if(text.equalsIgnoreCase("ข้อสอบถูกผิด"))
			return "TF";
		return null;
	}
	
}