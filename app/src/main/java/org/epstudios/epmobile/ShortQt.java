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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class ShortQt extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.shortqt);
        super.onCreate(savedInstanceState);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);

		qtcRadioGroup = (RadioGroup) findViewById(R.id.qtc_radio_group);
		shortJtCheckBox = (CheckBox) findViewById(R.id.short_jt);
		suddenCardiacArrestCheckBox = (CheckBox) findViewById(R.id.sudden_cardiac_arrest);
		polymorphicVtCheckBox = (CheckBox) findViewById(R.id.polymorphic_vt);
		unexplainedSyncopeCheckBox = (CheckBox) findViewById(R.id.unexplained_syncope);
		afbCheckBox = (CheckBox) findViewById(R.id.afb);
		relativeWithSqtsCheckBox = (CheckBox) findViewById(R.id.relative_with_sqts);
		relativeWithSdCheckBox = (CheckBox) findViewById(R.id.relative_with_sd);
		sidsCheckBox = (CheckBox) findViewById(R.id.sids);
		genotypePositiveCheckBox = (CheckBox) findViewById(R.id.genotype_positive);
		mutationCheckBox = (CheckBox) findViewById(R.id.mutation);

		clearEntries();

	}

	// Algorithm

	private RadioGroup qtcRadioGroup;
	private CheckBox shortJtCheckBox;
	private CheckBox suddenCardiacArrestCheckBox;
	private CheckBox polymorphicVtCheckBox;
	private CheckBox unexplainedSyncopeCheckBox;
	private CheckBox afbCheckBox;
	private CheckBox relativeWithSqtsCheckBox;
	private CheckBox relativeWithSdCheckBox;
	private CheckBox sidsCheckBox;
	private CheckBox genotypePositiveCheckBox;
	private CheckBox mutationCheckBox;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_button:
			calculateResult();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		case R.id.instructions_button:
			displayInstructions();
			break;
		}
	}

	private void calculateResult() {
		int score = 0;
		// ECG criteria
		// one of the short QT intervals must be selected to get other points
		if (qtcRadioGroup.getCheckedRadioButtonId() == -1
				&& !shortJtCheckBox.isChecked()) {
			displayResult(score);
			return;
		}
		if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.short_qt)
			score++;
		else if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.shorter_qt)
			score += 2;
		else if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.shortest_qt)
			score += 3;
		// Short JT is very specific for SQTS
		if (shortJtCheckBox.isChecked())
			score++;
		// Clinical history
		// points can be received for only one of the next 3 selections
		if (suddenCardiacArrestCheckBox.isChecked())
			score += 2;
		else if (polymorphicVtCheckBox.isChecked())
			score += 2;
		else if (unexplainedSyncopeCheckBox.isChecked())
			score++;
		if (afbCheckBox.isChecked())
			score++;
		// Family history
		// points can be received only once in this section
		if (relativeWithSqtsCheckBox.isChecked())
			score += 2;
		else if (relativeWithSdCheckBox.isChecked())
			score++;
		else if (sidsCheckBox.isChecked())
			score++;
		// Genotype
		if (genotypePositiveCheckBox.isChecked())
			score += 2;
		if (mutationCheckBox.isChecked())
			score++;

		displayResult(score);
	}

	private void displayResult(int score) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = "Score = " + score + "\n";
		if (score >= 4)
			message += "High probability";
		else if (score == 3)
			message += "Intermediate probability";
		else if (score <= 2)
			message += "Low probability";
		message += " of Short QT Syndrome";
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				getString(R.string.dont_reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setTitle(getString(R.string.short_qt_title));

		dialog.show();
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getString(R.string.sqts_instructions);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.short_qt_title));
		dialog.show();
	}

	private void clearEntries() {
		qtcRadioGroup.clearCheck();
		shortJtCheckBox.setChecked(false);
		suddenCardiacArrestCheckBox.setChecked(false);
		polymorphicVtCheckBox.setChecked(false);
		unexplainedSyncopeCheckBox.setChecked(false);
		afbCheckBox.setChecked(false);
		relativeWithSqtsCheckBox.setChecked(false);
		relativeWithSdCheckBox.setChecked(false);
		sidsCheckBox.setChecked(false);
		genotypePositiveCheckBox.setChecked(false);
		mutationCheckBox.setChecked(false);

	}

}
