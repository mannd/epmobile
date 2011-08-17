package org.epstudios.epmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.text.Editable;

public class Qtc extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qtc);
		
		View calculateQtcButton = findViewById(R.id.calculate_qtc_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        
        qtcTextView = (TextView) findViewById(R.id.calculated_qtc);
        rrEditText = (EditText) findViewById(R.id.rrEditText);
        qtEditText = (EditText) findViewById(R.id.qtEditText);
	}
	
	TextView qtcTextView;
	EditText rrEditText;
	EditText qtEditText;

	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_qtc_button:
			calculateQtc();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}
	
	private void calculateQtc() {
//		int rr = Integer.parseInt(rrEditText.getText().toString());
//		int qt = Integer.parseInt(qtEditText.getText().toString());
//		qtcTextView.setText(String.valueOf(rr));
		String rrText = rrEditText.getText().toString();
		String qtText = qtEditText.getText().toString();
		Integer rr = Integer.getInteger(rrText);
		Integer qt = Integer.getInteger(qtText);
		if (rr == null || qt == null)
			qtcTextView.setText("Invalid Arguements");
		
	}		
	
	
	private void clearEntries() {
		rrEditText.setText("");
		qtEditText.setText("");
		qtcTextView.setText("Cleared!");
	}
		

}
