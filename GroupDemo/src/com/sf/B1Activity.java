package com.sf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sf.util.AbsSubActivity;

public class B1Activity extends AbsSubActivity{
	
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

        tv.setText("B1Activity");
        btn1.setText("����B2Activity(startActivity��ʽ)");
        btn2.setText("����B2Activity(startActivityForResult��ʽ)");
        btn3.setText("����B3Activity(startActivity��ʽ)");
        btn4.setText("����B3Activity(startActivityForResult��ʽ)");
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
				startActivity(new Intent(B1Activity.this,
						B2Activity.class));
			} else if(v == findViewById(R.id.btn2)){
				startActivityForResult(new Intent(B1Activity.this,
						B2Activity.class),1);
			} else if(v == findViewById(R.id.btn3)){
				startActivity(new Intent(B1Activity.this,
						B3Activity.class));
			} else if(v == findViewById(R.id.btn4)){
				startActivityForResult(new Intent(B1Activity.this,
						B3Activity.class),2);
			} else if(v == findViewById(R.id.btn5)){
				finish();
			}
		}
    	
    };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == 1){
			et.setText("��B2Activity������   "+data.getStringExtra("data"));
		} else if(requestCode == 2 && resultCode == 2){
			et.setText("��B3Activity������   "+data.getStringExtra("data"));
		}
		
	}
}