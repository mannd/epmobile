/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CycleLength extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.cyclelength);
        super.onCreate(savedInstanceState);

        View calculateResultButton = findViewById(R.id.calculate_result_button);
		calculateResultButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		intervalRateRadioGroup = (RadioGroup) findViewById(R.id.intervalRateRadioGroup);
		resultTextView = (TextView) findViewById(R.id.calculated_result);
		inputEditText = (EditText) findViewById(R.id.inputEditText);
		clRadioButton = (RadioButton) findViewById(R.id.cl_button);
		hrRadioButton = (RadioButton) findViewById(R.id.hr_button);
		clRadioButton.setOnClickListener(this);
		hrRadioButton.setOnClickListener(this);
		measurementTextView = (TextView) findViewById(R.id.MeasurementTextView);
		if (savedInstanceState != null) {
			String savedLabel = savedInstanceState.getString("label");
			String savedHint = savedInstanceState.getString("hint");
			measurementTextView.setText(savedLabel);
			inputEditText.setHint(savedHint);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, CalculatorList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("label", measurementTextView.getText().toString());
		outState.putString("hint", inputEditText.getHint().toString());

	}

	private TextView resultTextView;
	private EditText inputEditText;
	private RadioGroup intervalRateRadioGroup;
	private RadioButton clRadioButton;
	private RadioButton hrRadioButton;
	private TextView measurementTextView;

	@Override
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
			setMeasurementTextView();
			break;
		}
	}

	private void setInputHint() {
		if (intervalRateRadioGroup.getCheckedRadioButtonId() == R.id.cl_button)
			inputEditText.setHint(R.string.cl_hint);
		else
			inputEditText.setHint(R.string.hr_hint);
	}

	private void setMeasurementTextView() {
		if (intervalRateRadioGroup.getCheckedRadioButtonId() == R.id.cl_button)
			measurementTextView.setText(R.string.cl_hint);
		else
			measurementTextView.setText(R.string.hr_hint);
	}

	private void calculateResult() {
		CharSequence resultText = inputEditText.getText();
		resultTextView.setTextColor(getResources().getColor(R.color.green));
		try {
			int result = Integer.parseInt(resultText.toString());
			if (result == 0)
				throw new NumberFormatException();
			result = calculate(result);
			if (intervalRateRadioGroup.getCheckedRadioButtonId() == R.id.cl_button)
				resultTextView.setText("Rate = " + String.valueOf(result)
						+ " bpm");
			else
				resultTextView.setText("Interval = " + String.valueOf(result)
						+ " msec");
		} catch (NumberFormatException e) {
			resultTextView.setText(getString(R.string.invalid_warning));
			resultTextView.setTextColor(Color.RED);
		}
	}

	public static int calculate(int value) {
		// assumes 0 has been weeded out before calling
		if (BuildConfig.DEBUG && value == 0) {
			throw new AssertionError();
		}
		return (int) Math.round(60000.0 / value);
	}

	private void clearEntries() {
		inputEditText.setText(null);
		resultTextView.setText(getString(R.string.calculated_result_label));
		inputEditText.requestFocus();
		setInputHint();
	}
}
