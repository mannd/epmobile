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

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qtc);

		View calculateQtcButton = findViewById(R.id.calculate_qtc_button);
		calculateQtcButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		intervalRateSpinner = (Spinner) findViewById(R.id.interval_rate_spinner);
		qtcTextView = (TextView) findViewById(R.id.calculated_qtc);
		rrEditText = (EditText) findViewById(R.id.rrEditText);
		qtEditText = (EditText) findViewById(R.id.qtEditText);
		qtcFormulaTextView = (TextView) findViewById(R.id.qtc_formula);

		getPrefs();
		setAdapters();

		clearEntries();

	}

	private Spinner intervalRateSpinner;
	private TextView qtcTextView;
	private EditText rrEditText;
	private EditText qtEditText;
	private TextView qtcFormulaTextView;
	private String qtcFormula;
	private OnItemSelectedListener itemListener;

	private int qtcUpperLimit;
	private final static int QTC_UPPER_LIMIT = 440;
	private final static int INTERVAL_SELECTION = 0;
	private final static int RATE_SELECTION = 1;

	private IntervalRate defaultIntervalRateSelection = IntervalRate.INTERVAL;

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
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				updateIntervalRateSelection();
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}

		};

		intervalRateSpinner.setOnItemSelectedListener(itemListener);

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
			getPrefs();
			showQtcFormula();
			QtcFormula formula;
			if (qtcFormula.equals("BAZETT"))
				formula = QtcFormula.BAZETT;
			else if (qtcFormula.equals("FRIDERICIA"))
				formula = QtcFormula.FRIDERICIA;
			else if (qtcFormula.equals("SAGIE"))
				formula = QtcFormula.SAGIE;
			else if (qtcFormula.equals("HODGES"))
				formula = QtcFormula.HODGES;
			else
				formula = QtcFormula.BAZETT;
			Toast.makeText(this, "QTc Formula is " + qtcFormula, 3000).show();
			int qtc = QtcCalculator.calculate(rr, qt, formula);
			qtcTextView.setText("QTc = " + String.valueOf(qtc) + " msec");
			if (qtc >= qtcUpperLimit)
				qtcTextView.setTextColor(Color.RED);
			else
				qtcTextView.setTextColor(Color.GREEN);
		} catch (NumberFormatException e) {
			qtcTextView.setText("Invalid!");
			qtcTextView.setTextColor(Color.RED);
		}
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
