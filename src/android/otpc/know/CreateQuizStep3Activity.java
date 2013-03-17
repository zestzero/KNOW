package android.otpc.know;

import android.os.Bundle;
import android.widget.Toast;

public class CreateQuizStep3Activity extends KnowActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz_3);
		
		int mchoice, mchoose, tf;
		mchoice = getIntent().getIntExtra("MChoice", 0);
		mchoose = getIntent().getIntExtra("MChoose", 0);
		tf = getIntent().getIntExtra("TF", 0);
		
		Toast.makeText(CreateQuizStep3Activity.this, 
				"mchoice : " + mchoice + " / mchoose : " + mchoose + " / tf : " + tf, 
				Toast.LENGTH_LONG).show();
		
		initView();
		initController();
	}
	
	public void initView() {
		super.initView(CreateQuizStep3Activity.this);
	}

	public void initController() {
		super.initController();
	}
	
}