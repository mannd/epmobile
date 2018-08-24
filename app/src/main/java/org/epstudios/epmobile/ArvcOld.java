package org.epstudios.epmobile;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;

public class ArvcOld extends DiagnosticScore {
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

	@Override
	protected void calculateResult() {
		// Algorithm: 2 major, 1 major + 2 minor, or 4 minor = diagnosis
		int majorCount = 0;
		int minorCount = 0;
		if (rvDilatationCheckBox.isChecked())
			majorCount++;
		if (rvAneurysmsCheckBox.isChecked())
			majorCount++;
		if (rvSegmentalDilatationCheckBox.isChecked())
			majorCount++;
		if (mildRvDilatationCheckBox.isChecked())
			minorCount++;
		if (mildRvSegmentalDilatationCheckBox.isChecked())
			minorCount++;
		if (regionalRvHypokinesiaCheckBox.isChecked())
			minorCount++;
		if (fattyReplacementCheckBox.isChecked())
			majorCount++;
		if (twiCheckBox.isChecked())
			minorCount++;
		if (epsilonWavesCheckBox.isChecked())
			majorCount++;
		if (latePotentialsCheckBox.isChecked())
			minorCount++;
		if (lbbbCheckBox.isChecked())
			minorCount++;
		if (pvcsCheckBox.isChecked())
			minorCount++;
		if (familialDiseaseCheckBox.isChecked())
			majorCount++;
		if (familyHxScdCheckBox.isChecked())
			minorCount++;
		if (familyHxCheckBox.isChecked())
			minorCount++;
		displayResult(getResultMessage(majorCount, minorCount),
				getString(R.string.arvc_old_criteria_title));
	}

	private String getResultMessage(int major, int minor) {
		String message = "Major = " + major + "\n" + "Minor = " + minor + "\n";
		if ((major > 1) || (major > 0 && minor > 1) || (minor > 3))
			message += "Diagnostic of ARVC/D";
		else
			message += "Not Diagnostic of ARVC/D";
		return message;
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.arvcold);

	}

	@Override
	protected void init() {
		rvDilatationCheckBox = findViewById(R.id.rv_dilatation);
		rvAneurysmsCheckBox = findViewById(R.id.rv_aneurysms);
		rvSegmentalDilatationCheckBox = findViewById(R.id.rv_segmental_dilatation);
		mildRvDilatationCheckBox = findViewById(R.id.mild_rv_dilatation);
		mildRvSegmentalDilatationCheckBox = findViewById(R.id.mild_rv_segmental_dilatation);
		regionalRvHypokinesiaCheckBox = findViewById(R.id.regional_rv_hypokinesia);
		fattyReplacementCheckBox = findViewById(R.id.fatty_replacement);
		twiCheckBox = findViewById(R.id.twi);
		epsilonWavesCheckBox = findViewById(R.id.epsilon_waves);
		latePotentialsCheckBox = findViewById(R.id.late_potentials);
		lbbbCheckBox = findViewById(R.id.lbbb);
		pvcsCheckBox = findViewById(R.id.pvcs);
		familialDiseaseCheckBox = findViewById(R.id.familial_disease);
		familyHxScdCheckBox = findViewById(R.id.family_hx_scd);
		familyHxCheckBox = findViewById(R.id.family_hx);
	}

	@Override
	protected void clearEntries() {
		rvDilatationCheckBox.setChecked(false);
		rvAneurysmsCheckBox.setChecked(false);
		rvSegmentalDilatationCheckBox.setChecked(false);
		mildRvDilatationCheckBox.setChecked(false);
		mildRvSegmentalDilatationCheckBox.setChecked(false);
		regionalRvHypokinesiaCheckBox.setChecked(false);
		fattyReplacementCheckBox.setChecked(false);
		twiCheckBox.setChecked(false);
		epsilonWavesCheckBox.setChecked(false);
		latePotentialsCheckBox.setChecked(false);
		lbbbCheckBox.setChecked(false);
		pvcsCheckBox.setChecked(false);
		familialDiseaseCheckBox.setChecked(false);
		familyHxScdCheckBox.setChecked(false);
		familyHxCheckBox.setChecked(false);
	}

	// major
	private CheckBox rvDilatationCheckBox;
	private CheckBox rvAneurysmsCheckBox;
	private CheckBox rvSegmentalDilatationCheckBox;
	// minor
	private CheckBox mildRvDilatationCheckBox;
	private CheckBox mildRvSegmentalDilatationCheckBox;
	private CheckBox regionalRvHypokinesiaCheckBox;
	// major
	private CheckBox fattyReplacementCheckBox;
	// minor
	private CheckBox twiCheckBox;
	// major
	private CheckBox epsilonWavesCheckBox;
	// minor
	private CheckBox latePotentialsCheckBox;
	// minor
	private CheckBox lbbbCheckBox;
	private CheckBox pvcsCheckBox;
	// major
	private CheckBox familialDiseaseCheckBox;
	// minor
	private CheckBox familyHxScdCheckBox;
	private CheckBox familyHxCheckBox;

}
