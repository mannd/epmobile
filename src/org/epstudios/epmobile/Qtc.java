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

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class Qtc extends EpActivity implements OnClickListener {
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
        qtcFormulaTextView = (TextView) findViewById(R.id.qtc_formula);
        
        clearEntries();
        
	}

	private TextView qtcTextView;
	private EditText rrEditText;
	private EditText qtEditText;
	private TextView qtcFormulaTextView;
	private String qtcFormula;

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

	private int qtcUpperLimit;
	private final static int QTC_UPPER_LIMIT = 440;

	private void showQtcFormula() {
		qtcFormulaTextView.setText("QTc formula used was " + qtcFormula);
	}

	private void calculateQtc() {
		CharSequence rrText = rrEditText.getText();
		CharSequence qtText = qtEditText.getText();
		try {
			int rr = Integer.parseInt(rrText.toString());
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
		String s = prefs.getString("maximum_qtc", "");
		try {
			qtcUpperLimit = Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			qtcUpperLimit = QTC_UPPER_LIMIT;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("maximum_qtc", String.valueOf(QTC_UPPER_LIMIT));
			editor.commit();
		}
	}

}
