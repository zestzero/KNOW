package android.otpc.know;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class NewsFeedActivity extends KnowActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeed);
		
		initView();
		initController();
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	public void initController() {
		super.initController();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle("Logout")
		.setMessage("ต้องการออกจากระบบ?")
		.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setCancelable(false)
		.show();
	}
	
}
