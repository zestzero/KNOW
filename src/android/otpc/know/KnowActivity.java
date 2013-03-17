package android.otpc.know;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class KnowActivity extends Activity {
	
	private static String username, userid;
	
	private Context context;
	
	private TextView tv_username;
	private TextView tv_newsfeed;
	private TextView tv_create_quiz, tv_edit_quiz;
	private TextView tv_view_my_quiz, tv_view_taken_quiz;
	private TextView tv_profile;
	private TextView tv_logout;
	
	public void initView(Context context) {
		this.context = context;
		
		if(username == null)
			username = getIntent().getStringExtra("username");
		if(userid == null)
			userid = getIntent().getStringExtra("userid");
		
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText("Username : " + username + "\nUser ID : " + userid);
		
		tv_newsfeed = (TextView) findViewById(R.id.tv_newsfeed);
		
		tv_create_quiz = (TextView) findViewById(R.id.tv_create_quiz);
		tv_edit_quiz = (TextView) findViewById(R.id.tv_edit_quiz);
		
		tv_view_my_quiz = (TextView) findViewById(R.id.tv_view_my_quiz);
		tv_view_taken_quiz = (TextView) findViewById(R.id.tv_view_taken_quiz);
		
		tv_profile = (TextView) findViewById(R.id.tv_profile);
		
		tv_logout = (TextView) findViewById(R.id.tv_logout);
	}

	public void initController() {
		tv_newsfeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, NewsFeedActivity.class);
				startActivity(intent);
			}
		});
		
		tv_create_quiz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CreateQuizStep1Activity.class);
				startActivity(intent);
			}
		});
		
		tv_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
				.setTitle("Logout")
				.setMessage("ต้องการออกจากระบบ?")
				.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						clearAfterLogout();
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
					}
				})
				.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { }
				})
				.setCancelable(false)
				.show();
			}
		});
	}
	
	public void clearAfterLogout(){
		username = null;
		userid = null;
	}
	
	public String getUserId(){
		return userid;
	}
}
