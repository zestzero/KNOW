package android.otpc.know;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class KnowActivity extends Activity {
	
	private String username, userid;
	
	private TextView tv_username;
	private TextView tv_create_quiz, tv_edit_quiz;
	private TextView tv_view_my_quiz, tv_view_taken_quiz;
	private TextView tv_profile;
	
	public void initView() {
		username = getIntent().getStringExtra("username");
		userid = getIntent().getStringExtra("userid");
		
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText("Username : " + username + "\nUser ID : " + userid);
		
		tv_create_quiz = (TextView) findViewById(R.id.tv_create_quiz);
		tv_edit_quiz = (TextView) findViewById(R.id.tv_edit_quiz);
		
		tv_view_my_quiz = (TextView) findViewById(R.id.tv_view_my_quiz);
		tv_view_taken_quiz = (TextView) findViewById(R.id.tv_view_taken_quiz);
		
		tv_profile = (TextView) findViewById(R.id.tv_profile);
	}

	public void initController() {
		tv_create_quiz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CreateQuizStep1Activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}
	
}
