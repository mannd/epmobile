package org.epstudios.epmobile;

import org.epstudios.epmobile.QtcCalculator.QtcFormula;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;
//import android.text.Editable;

public class CycleLength extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cyclelength);
		
		View calculateQtcButton = findViewById(R.id.calculate_result_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        intervalRateRadioGroup = (RadioGroup) findViewById(R.id.intervalRateRadioGroup);
        resultTextView = (TextView) findViewById(R.id.calculated_result);
        inputEditText = (EditText) findViewById(R.id.inputEditText);
        clRadioButton = (RadioButton) findViewById(R.id.cl_button);
        hrRadioButton = (RadioButton) findViewById(R.id.hr_button);
        clRadioButton.setOnClickListener(this);
        hrRadioButton.setOnClickListener(this);
        
        
        setInputHint();
	}
	
	private TextView resultTextView;
	private EditText inputEditText;
	private RadioGroup intervalRateRadioGroup;
	private RadioButton clRadioButton;
	private RadioButton hrRadioButton;

	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_result_button:
			calculateResult();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		case R.id.cl_button:
		case R.id.hr_button:
			setInputHint();
			break;
		}
	}
	
	private void setInputHint() {
		if (intervalRateRadioGroup.getCheckedRadioButtonId() == R.id.cl_button)
			inputEditText.setHint(R.string.cl_hint);
		else
			inputEditText.setHint(R.string.hr_hint);
			
	}
	
	private void calculateResult() {
//		CharSequence rrText = rrEditText.getText();
//		CharSequence qtText = qtEditText.getText();
//		try {
//			int rr = Integer.parseInt(rrText.toString());
//			int qt = Integer.parseInt(qtText.toString());
//			int qtc = QtcCalculator.calculate(rr, qt, QtcFormula.BAZETT);
//			qtcTextView.setText("QTc = " + String.valueOf(qtc) + " msec");
//			if (qtc >= QTC_UPPER_LIMIT)
//				qtcTextView.setTextColor(Color.RED);
//			else
//				qtcTextView.setTextColor(Color.GREEN);
//		}
//		catch (NumberFormatException e) {	
//			qtcTextView.setText("Invalid!");
//			qtcTextView.setTextColor(Color.RED);
//		}		
	}		
	
	
	private void clearEntries() {
		inputEditText.setText(null);
		resultTextView.setText(getString(R.string.calculated_result_label));
		inputEditText.requestFocus();
		setInputHint();
	}
}
