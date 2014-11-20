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

import org.epstudios.epmobile.QtcCalculator.QtcFormula;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Qtc extends EpActivity implements OnClickListener {
	private enum IntervalRate {
		INTERVAL, RATE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.qtc);
		super.onCreate(savedInstanceState);

		View calculateQtcButton = findViewById(R.id.calculate_qtc_button);
		calculateQtcButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		intervalRateSpinner = (Spinner) findViewById(R.id.interval_rate_spinner);
		qtcTextView = (TextView) findViewById(R.id.calculated_qtc);
		rrEditText = (EditText) findViewById(R.id.rrEditText);
		qtEditText = (EditText) findViewById(R.id.qtEditText);
		qtcFormulaTextView = (TextView) findViewById(R.id.qtc_formula);
		qtcFormulaSpinner = (Spinner) findViewById(R.id.qtc_formula_spinner);

		getPrefs();
		setAdapters();
		setFormulaAdapters();

		clearEntries();

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

	private Spinner intervalRateSpinner;
	private TextView qtcTextView;
	private EditText rrEditText;
	private EditText qtEditText;
	private TextView qtcFormulaTextView;
	private String qtcFormula;
	private OnItemSelectedListener itemListener;
	private Spinner qtcFormulaSpinner;

	private int qtcUpperLimit;
	private final static int QTC_UPPER_LIMIT = 440;
	private final static int INTERVAL_SELECTION = 0;
	private final static int RATE_SELECTION = 1;

	private final static int BAZETT_FORMULA = 0;
	private final static int FRIDERICIA_FORMULA = 1;
	private final static int SAGIE_FORMULA = 2;
	private final static int HODGES_FORMULA = 3;

	private IntervalRate defaultIntervalRateSelection = IntervalRate.INTERVAL;

	@Override
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

	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.interval_rate_labels,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		intervalRateSpinner.setAdapter(adapter);
		if (defaultIntervalRateSelection.equals(IntervalRate.INTERVAL))
			intervalRateSpinner.setSelection(INTERVAL_SELECTION);
		else
			intervalRateSpinner.setSelection(RATE_SELECTION);
		itemListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateIntervalRateSelection();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		intervalRateSpinner.setOnItemSelectedListener(itemListener);

	}

	private void setFormulaAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.formula_names,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		qtcFormulaSpinner.setAdapter(adapter);
		int formula = BAZETT_FORMULA;
		QtcFormula f = getQtcFormula(qtcFormula);
		switch (f) {
		case BAZETT:
			formula = BAZETT_FORMULA;
			break;
		case FRIDERICIA:
			formula = FRIDERICIA_FORMULA;
			break;
		case SAGIE:
			formula = SAGIE_FORMULA;
			break;
		case HODGES:
			formula = HODGES_FORMULA;
			break;
		}
		qtcFormulaSpinner.setSelection(formula);
		itemListener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateQtcFormula();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		qtcFormulaSpinner.setOnItemSelectedListener(itemListener);

	}

	private void updateQtcFormula() {
		int result = qtcFormulaSpinner.getSelectedItemPosition();
		switch (result) {
		case BAZETT_FORMULA:
			qtcFormula = "BAZETT";
			break;
		case FRIDERICIA_FORMULA:
			qtcFormula = "FRIDERICIA";
			break;
		case SAGIE_FORMULA:
			qtcFormula = "SAGIE";
			break;
		case HODGES_FORMULA:
			qtcFormula = "HODGES";
			break;
		}
	}

	private void updateIntervalRateSelection() {
		IntervalRate intervalRateSelection = getIntervalRateSelection();
		if (intervalRateSelection.equals(IntervalRate.INTERVAL))
			rrEditText.setHint(getString(R.string.rr_hint));
		else
			rrEditText.setHint(getString(R.string.hr_hint));
	}

	private IntervalRate getIntervalRateSelection() {
		String result = intervalRateSpinner.getSelectedItem().toString();
		if (result.startsWith("RR"))
			return IntervalRate.INTERVAL;
		else
			return IntervalRate.RATE;

	}

	private void showQtcFormula() {
		qtcFormulaTextView.setText("QTc formula used was " + qtcFormula);
	}

	private void calculateQtc() {
		CharSequence rrText = rrEditText.getText();
		CharSequence qtText = qtEditText.getText();
		IntervalRate intervalRateSelection = getIntervalRateSelection();
		try {
			int rr = Integer.parseInt(rrText.toString());
			if (intervalRateSelection.equals(IntervalRate.RATE))
				rr = 60000 / rr;
			int qt = Integer.parseInt(qtText.toString());
			// getPrefs();
			showQtcFormula();
			QtcFormula formula = getQtcFormula(qtcFormula);
			Toast.makeText(this, "QTc Formula is " + qtcFormula,
					Toast.LENGTH_LONG).show();
			int qtc = QtcCalculator.calculate(rr, qt, formula);
			qtcTextView.setText("QTc = " + String.valueOf(qtc) + " msec");
			if (qtc >= qtcUpperLimit)
				qtcTextView.setTextColor(Color.RED);
			else
				qtcTextView
						.setTextColor(getResources().getColor(R.color.green));
		} catch (NumberFormatException e) {
			qtcTextView.setText(getString(R.string.invalid_warning));
			qtcTextView.setTextColor(Color.RED);
		}
	}

	private QtcFormula getQtcFormula(String name) {
		if (name.equals("BAZETT"))
			return QtcFormula.BAZETT;
		else if (name.equals("FRIDERICIA"))
			return QtcFormula.FRIDERICIA;
		else if (name.equals("SAGIE"))
			return QtcFormula.SAGIE;
		else if (name.equals("HODGES"))
			return QtcFormula.HODGES;
		else
			return QtcFormula.BAZETT;

	}

	private void clearEntries() {
		rrEditText.setText(null);
		qtEditText.setText(null);
		qtcTextView.setText(getString(R.string.qtc_result_label));
		qtcTextView.setTextColor(Color.LTGRAY);
		qtcFormulaTextView.setText(null);
		rrEditText.requestFocus();
	}

	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		qtcFormula = prefs.getString("qtc_formula", "BAZETT");
		String intervalRatePreference = prefs.getString("interval_rate",
				"INTERVAL");
		if (intervalRatePreference.equals("INTERVAL"))
			defaultIntervalRateSelection = IntervalRate.INTERVAL;
		else
			defaultIntervalRateSelection = IntervalRate.RATE;
		String s = prefs.getString("maximum_qtc", "");
		try {
			qtcUpperLimit = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			qtcUpperLimit = QTC_UPPER_LIMIT;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("maximum_qtc", String.valueOf(QTC_UPPER_LIMIT));
			editor.commit();
		}
	}

}
