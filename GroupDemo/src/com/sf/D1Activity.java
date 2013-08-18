package com.sf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sf.util.AbsSubActivity;

public class D1Activity extends AbsSubActivity{
	
	private TextView tv;
	private EditText et;
	private Button btn1,btn2,btn3,btn4,btn5;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        
        tv = ((TextView)findViewById(R.id.test));
        et = (EditText) findViewById(R.id.et);
        btn1 = ((Button)findViewById(R.id.btn));
        btn2 = ((Button)findViewById(R.id.btn2));
        btn3 = ((Button)findViewById(R.id.btn3));
        btn4 = ((Button)findViewById(R.id.btn4));
        btn5 = ((Button)findViewById(R.id.btn5));

        tv.setText("D1Activity");
        btn1.setText("无操作");
        btn2.setText("无操作");
        btn3.setText("跳到SimpleActivity(startActivity方式)");
        btn4.setText("跳到SimpleActivity(startActivityForResult方式)");
        btn1.setOnClickListener(myClickListener);
        btn2.setOnClickListener(myClickListener);
        btn3.setOnClickListener(myClickListener);
        btn4.setOnClickListener(myClickListener);
        btn5.setOnClickListener(myClickListener);
    }
    
    private OnClickListener myClickListener = new  OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v == findViewById(R.id.btn)){

			} else if(v == findViewById(R.id.btn2)){

			} else if(v == findViewById(R.id.btn3)){
				startActivity(new Intent(D1Activity.this,
						SimpleActivity.class));
			} else if(v == findViewById(R.id.btn4)){
				startActivityForResult(new Intent(D1Activity.this,
						SimpleActivity.class),2);
			} else if(v == findViewById(R.id.btn5)){
				finish();
			}
		}
    	
    };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2 && resultCode == 2) {
			et.setText("从SimpleActivity回来了   " + data.getStringExtra("data"));
		}

	}
}
