package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DayCalculator extends EpActivity implements OnClickListener {
	private DatePicker indexDatePicker;
	private RadioGroup dayRadioGroup;
	private EditText numberOfDaysEditText;
	private Button calculateDateButton;
	private Button clearButton;
	private TextView calculatedDateTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daycalculator);

		View calculateDateButton = findViewById(R.id.calculate_date_button);
		calculateDateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		indexDatePicker = (DatePicker) findViewById(R.id.indexDatePicker);
		dayRadioGroup = (RadioGroup) findViewById(R.id.dayRadioGroup);
		numberOfDaysEditText = (EditText) findViewById(R.id.numberOfDaysEditText);
		calculatedDateTextView = (TextView) findViewById(R.id.calculated_date);

		dayRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton checkedRadioButton = (RadioButton) group
								.findViewById(checkedId);
						boolean isChecked = checkedRadioButton.isChecked();
						if (isChecked) {
							calculatedDateTextView.setText(checkedId);

						}

					}
				});

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_date_button:
			calculateDays();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}

	private void calculateDays() {

	}

	private void clearEntries() {
		numberOfDaysEditText.setText(null);
		calculatedDateTextView.setText(getString(R.string.date_result_label));
	}

}
