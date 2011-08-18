package org.epstudios.epmobile;

import org.epstudios.epmobile.QtcCalculator.QtcFormula;

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
	
	private TextView qtcTextView;
	private EditText rrEditText;
	private EditText qtEditText;

	
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
		CharSequence rrText = rrEditText.getText();
		CharSequence qtText = qtEditText.getText();
		try {
			int rr = Integer.parseInt(rrText.toString());
			int qt = Integer.parseInt(qtText.toString());
			int qtc = QtcCalculator.calculate(rr, qt, QtcFormula.BAZETT);
			qtcTextView.setText("QTc = " + String.valueOf(qtc) + " msec");
		}
		catch (NumberFormatException e) {	
			qtcTextView.setText("Invalid!");
		}		
	}		
	
	
	private void clearEntries() {
		rrEditText.setText(null);
		qtEditText.setText(null);
		qtcTextView.setText(getString(R.string.cleared));
		rrEditText.requestFocus();
	}
		

}
