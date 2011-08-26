package org.epstudios.epmobile;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.EditText;

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
		CharSequence resultText = inputEditText.getText();
		resultTextView.setTextColor(Color.GREEN);
		try {
			int result = Integer.parseInt(resultText.toString());
			if (result == 0)
				throw new NumberFormatException();
			result = 60000 / result;
			if (intervalRateRadioGroup.getCheckedRadioButtonId() == R.id.cl_button)
				resultTextView.setText("HR = " + String.valueOf(result) + " bpm");
			else
				resultTextView.setText("CL = " + String.valueOf(result) + " msec");
		}
		catch (NumberFormatException e) {	
			resultTextView.setText("Invalid!");
			resultTextView.setTextColor(Color.RED);
		}		
	}		
	
	
	private void clearEntries() {
		inputEditText.setText(null);
		resultTextView.setText(getString(R.string.calculated_result_label));
		inputEditText.requestFocus();
		setInputHint();
	}
}
