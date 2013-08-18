package com.sf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OutActivity extends Activity{
	private TextView tv;
	private EditText et;
	private Button btn1,btn2,btn3,btn4,btn5;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        
        tv = ((TextView)findViewById(R.id.test));
        et = (EditText) findViewById(R.id.et);
        btn5 = ((Button)findViewById(R.id.btn5));

        tv.setText("OutActivity");
        btn5.setOnClickListener(myClickListener);
    }
    
    private OnClickListener myClickListener = new  OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v == findViewById(R.id.btn)){

			} else if(v == findViewById(R.id.btn2)){

			} else if(v == findViewById(R.id.btn3)){

			} else if(v == findViewById(R.id.btn4)){
		
			} else if(v == findViewById(R.id.btn5)){
				Intent intent = getIntent();	
				intent.putExtra("data", et.getText().toString().trim());
				setResult(2,intent);
				finish();
			}
		}
    	
    };
}
