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
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class Arvc extends DiagnosticScore {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, DiagnosisList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Algorithm
	// definite: 2 major or (1 major + 2 minor) or 4 minor
	// borderline: 1 major + 1 minor or (3 minor)
	// possible: 1 major or 2 minor
	// Must be from different groups

	// below declared in order of appearance in arvcdiagnosis.xml
	// Global/regional dysfunction
	// MAJOR
	private CheckBox regionalEchoAbnormalityCheckBox;
	private RadioGroup echoMajorRadioGroup;

	private CheckBox regionalMriAbnormalityCheckBox;
	private RadioGroup mriMajorRadioGroup;

	private CheckBox regionalRvAngioAbnormalityCheckBox;

	// MINOR
	private CheckBox regionalEchoMinorAbnormalityCheckBox;
	private RadioGroup echoMinorRadioGroup;

	private CheckBox minorRegionalMriAbnormalityCheckBox;
	private RadioGroup mriMinorRadioGroup;

	// Tissue characterization
	// MAJOR
	private CheckBox majorResidualMyocytesCheckBox;

	// MINOR
	private CheckBox minorResidualMyocytesCheckBox;

	// Repolarization Abnormality
	// MAJOR
	private CheckBox majorRepolarizationCheckBox;

	// MINOR
	private CheckBox minorRepolarizationNoRbbbCheckBox;
	private CheckBox minorRepolarizationRbbbCheckBox;

	// Depolarization Abnormality
	// MAJOR
	private CheckBox majorDepolarizationCheckBox;

	// MINOR
	private CheckBox filteredQrsCheckBox;
	private CheckBox durationTerminalQrsCheckBox;
	private CheckBox rootMeanSquareCheckBox;
	private CheckBox terminalActivationDurationCheckBox;

	// Arrhythmias
	// MAJOR
	private CheckBox majorArrhythmiasCheckBox;

	// MINOR
	private CheckBox rvotVtCheckBox;
	private CheckBox pvcsCheckBox;

	// Family History
	// MAJOR
	private CheckBox firstDegreeRelativeCheckBox;
	private CheckBox pathologyCheckBox;
	private CheckBox geneticCheckBox;

	// MINOR
	private CheckBox possibleFamilyHistoryCheckBox;
	private CheckBox familyHistorySuddenDeathCheckBox;
	private CheckBox secondDegreeRelativeCheckBox;

	@Override
	protected void setContentView() {
		setContentView(R.layout.arvcdiagnosis);
	}

	@Override
	protected void init() {
		regionalEchoAbnormalityCheckBox = (CheckBox) findViewById(R.id.regional_echo_abnormality);
		echoMajorRadioGroup = (RadioGroup) findViewById(R.id.echo_major_radio_group);
		regionalMriAbnormalityCheckBox = (CheckBox) findViewById(R.id.regional_mri_abnormality);
		mriMajorRadioGroup = (RadioGroup) findViewById(R.id.mri_major_radio_group);
		regionalRvAngioAbnormalityCheckBox = (CheckBox) findViewById(R.id.regional_rv_angio_abnormality);
		regionalEchoMinorAbnormalityCheckBox = (CheckBox) findViewById(R.id.regional_echo_minor_abnormality);
		echoMinorRadioGroup = (RadioGroup) findViewById(R.id.echo_minor_radio_group);
		minorRegionalMriAbnormalityCheckBox = (CheckBox) findViewById(R.id.minor_regional_mri_abnormality);
		mriMinorRadioGroup = (RadioGroup) findViewById(R.id.mri_minor_radio_group);
		majorResidualMyocytesCheckBox = (CheckBox) findViewById(R.id.major_residual_myocytes);
		minorResidualMyocytesCheckBox = (CheckBox) findViewById(R.id.minor_residual_myocytes);
		majorRepolarizationCheckBox = (CheckBox) findViewById(R.id.major_repolarization);
		minorRepolarizationNoRbbbCheckBox = (CheckBox) findViewById(R.id.minor_repolarization_no_rbbb);
		minorRepolarizationRbbbCheckBox = (CheckBox) findViewById(R.id.minor_repolarization_rbbb);
		majorDepolarizationCheckBox = (CheckBox) findViewById(R.id.major_depolarization);
		filteredQrsCheckBox = (CheckBox) findViewById(R.id.filtered_qrs);
		durationTerminalQrsCheckBox = (CheckBox) findViewById(R.id.duration_terminal_qrs);
		rootMeanSquareCheckBox = (CheckBox) findViewById(R.id.root_mean_square);
		terminalActivationDurationCheckBox = (CheckBox) findViewById(R.id.terminal_activation_duration);
		majorArrhythmiasCheckBox = (CheckBox) findViewById(R.id.major_arrhythmias);
		rvotVtCheckBox = (CheckBox) findViewById(R.id.rvot_vt);
		pvcsCheckBox = (CheckBox) findViewById(R.id.pvcs);
		firstDegreeRelativeCheckBox = (CheckBox) findViewById(R.id.first_degree_relative);
		pathologyCheckBox = (CheckBox) findViewById(R.id.pathology);
		geneticCheckBox = (CheckBox) findViewById(R.id.genetic);
		possibleFamilyHistoryCheckBox = (CheckBox) findViewById(R.id.possible_family_history);
		familyHistorySuddenDeathCheckBox = (CheckBox) findViewById(R.id.family_history_sudden_death);
		secondDegreeRelativeCheckBox = (CheckBox) findViewById(R.id.second_degree_relative);
	}

	protected void calculateResult() {
		int majorCount = 0;
		int minorCount = 0;
		if (regionalEchoAbnormalityCheckBox.isChecked()
				&& echoMajorRadioGroup.getCheckedRadioButtonId() > -1
				|| regionalMriAbnormalityCheckBox.isChecked()
				&& mriMajorRadioGroup.getCheckedRadioButtonId() > -1
				|| regionalRvAngioAbnormalityCheckBox.isChecked())
			majorCount++;
		if (regionalEchoMinorAbnormalityCheckBox.isChecked()
				&& echoMinorRadioGroup.getCheckedRadioButtonId() > -1
				|| minorRegionalMriAbnormalityCheckBox.isChecked()
				&& mriMinorRadioGroup.getCheckedRadioButtonId() > -1)
			minorCount++;
		if (majorResidualMyocytesCheckBox.isChecked())
			majorCount++;
		if (minorResidualMyocytesCheckBox.isChecked())
			minorCount++;
		if (majorRepolarizationCheckBox.isChecked())
			majorCount++;
		if (minorRepolarizationNoRbbbCheckBox.isChecked()
				|| minorRepolarizationRbbbCheckBox.isChecked())
			minorCount++;
		if (majorDepolarizationCheckBox.isChecked())
			majorCount++;
		if (filteredQrsCheckBox.isChecked()
				|| durationTerminalQrsCheckBox.isChecked()
				|| rootMeanSquareCheckBox.isChecked()
				|| terminalActivationDurationCheckBox.isChecked())
			minorCount++;
		if (majorArrhythmiasCheckBox.isChecked())
			majorCount++;
		if (rvotVtCheckBox.isChecked() || pvcsCheckBox.isChecked())
			minorCount++;
		if (firstDegreeRelativeCheckBox.isChecked()
				|| pathologyCheckBox.isChecked() || geneticCheckBox.isChecked())
			majorCount++;
		if (possibleFamilyHistoryCheckBox.isChecked()
				|| familyHistorySuddenDeathCheckBox.isChecked()
				|| secondDegreeRelativeCheckBox.isChecked())
			minorCount++;

		displayResult(getResultMessage(majorCount, minorCount),
				getString(R.string.arvc_2010_criteria_title));
	}

	private String getResultMessage(int major, int minor) {
		String message = "Major = " + major + "\n" + "Minor = " + minor + "\n";
		if (major >= 2 || major == 1 && minor >= 2 || minor >= 4)
			message = message + "Definite diagnosis of ARVC/D";
		else if (major == 1 && minor >= 1 || minor == 3)
			message = message + "Borderline diagnosis of ARVC/D";
		else if (major == 1 || minor == 2)
			message = message + "Possible diagnosis of ARVC/D";
		else
			message = message + "Not diagnostic of ARVC/D";
		return message;
	}

	@Override
	protected void clearEntries() {
		regionalEchoAbnormalityCheckBox.setChecked(false);
		echoMajorRadioGroup.clearCheck();
		regionalMriAbnormalityCheckBox.setChecked(false);
		mriMajorRadioGroup.clearCheck();
		regionalRvAngioAbnormalityCheckBox.setChecked(false);
		regionalEchoMinorAbnormalityCheckBox.setChecked(false);
		echoMinorRadioGroup.clearCheck();
		minorRegionalMriAbnormalityCheckBox.setChecked(false);
		mriMinorRadioGroup.clearCheck();
		majorResidualMyocytesCheckBox.setChecked(false);
		minorResidualMyocytesCheckBox.setChecked(false);
		majorRepolarizationCheckBox.setChecked(false);
		minorRepolarizationNoRbbbCheckBox.setChecked(false);
		minorRepolarizationRbbbCheckBox.setChecked(false);
		majorDepolarizationCheckBox.setChecked(false);
		filteredQrsCheckBox.setChecked(false);
		durationTerminalQrsCheckBox.setChecked(false);
		rootMeanSquareCheckBox.setChecked(false);
		terminalActivationDurationCheckBox.setChecked(false);
		majorArrhythmiasCheckBox.setChecked(false);
		rvotVtCheckBox.setChecked(false);
		pvcsCheckBox.setChecked(false);
		firstDegreeRelativeCheckBox.setChecked(false);
		pathologyCheckBox.setChecked(false);
		geneticCheckBox.setChecked(false);
		possibleFamilyHistoryCheckBox.setChecked(false);
		familyHistorySuddenDeathCheckBox.setChecked(false);
		secondDegreeRelativeCheckBox.setChecked(false);
	}
}
