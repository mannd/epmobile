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
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shortqt);
		
//		View calculateButton = findViewById(R.id.calculate_button);
//        calculateButton.setOnClickListener(this);
//        View clearButton = findViewById(R.id.clear_button);
//        clearButton.setOnClickListener(this);
//        
 
        clearEntries();
		
	}
	
	// Algorithm

	
	
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.calculate_button:
//			calculateResult();
//			break;
//		case R.id.clear_button:
//			clearEntries();
//			break;
//		}
	}
	
	private void calculateResult() {
//		int majorCount = 0;
//		int minorCount = 0;
//		if (regionalEchoAbnormalityCheckBox.isChecked() && echoMajorRadioGroup.getCheckedRadioButtonId() > -1
//				|| regionalMriAbnormalityCheckBox.isChecked() && mriMajorRadioGroup.getCheckedRadioButtonId() > -1
//				|| regionalRvAngioAbnormalityCheckBox.isChecked())
//			majorCount++;
//		if (regionalEchoMinorAbnormalityCheckBox.isChecked() && echoMinorRadioGroup.getCheckedRadioButtonId() > -1
//				|| minorRegionalMriAbnormalityCheckBox.isChecked() && mriMinorRadioGroup.getCheckedRadioButtonId() > -1)
//			minorCount++;
//		if (majorResidualMyocytesCheckBox.isChecked())
//			majorCount++;
//		if (minorResidualMyocytesCheckBox.isChecked())
//			minorCount++;
//		if (majorRepolarizationCheckBox.isChecked())
//			majorCount++;
//		if (minorRepolarizationNoRbbbCheckBox.isChecked() || minorRepolarizationRbbbCheckBox.isChecked())
//			minorCount++;
//		if (majorDepolarizationCheckBox.isChecked())
//			majorCount++;
//		if (filteredQrsCheckBox.isChecked() || durationTerminalQrsCheckBox.isChecked()
//				|| rootMeanSquareCheckBox.isChecked() || terminalActivationDurationCheckBox.isChecked())
//			minorCount++;
//		if (majorArrhythmiasCheckBox.isChecked())
//			majorCount++;
//		if (rvotVtCheckBox.isChecked() || pvcsCheckBox.isChecked())
//			minorCount++;
//		if (firstDegreeRelativeCheckBox.isChecked() || pathologyCheckBox.isChecked()
//				|| geneticCheckBox.isChecked())
//			majorCount++;
//		if (possibleFamilyHistoryCheckBox.isChecked() || familyHistorySuddenDeathCheckBox.isChecked()
//				|| secondDegreeRelativeCheckBox.isChecked())
//			minorCount++;
		
		//displayResult(majorCount, minorCount);
	}
	
	private void displayResult(int major, int minor) {
//		AlertDialog dialog = new AlertDialog.Builder(this).create();
//		String message;
//		message = "Major = " + major + "\n" + "Minor = " + minor + "\n";
//		if (major >= 2 || major == 1 && minor >= 2 || minor >= 4)
//			message = message + "Definite diagnosis of ARVC/D";
//		else if (major == 1 && minor >= 1 || minor == 3)
//			message = message + "Borderline diagnosis of ARVC/D";
//		else if (major == 1 || minor == 2)
//			message = message + "Possible diagnosis of ARVC/D";
//		else
//			message = message + "Not diagnostic of ARVC/D"; 
//		dialog.setMessage(message);
//		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						clearEntries();
//					}
//				});
//		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Don't Reset",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {}
//				});
//		dialog.setTitle(getString(R.string.arvc_title));
//	
//		dialog.show();
	}
	
	
	
	
	private void clearEntries() {
 
		
	}
	
	
}
