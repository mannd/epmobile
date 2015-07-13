package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class CmsIcd extends DiagnosticScore {
	protected CheckBox[] checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, ReferenceList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void setContentView() {
		setContentView(R.layout.cmsicd);
	}

	@Override
	protected void init() {
		checkBox = new CheckBox[16];

		checkBox[0] = (CheckBox) findViewById(R.id.icd_cardiac_arrest);
		checkBox[1] = (CheckBox) findViewById(R.id.icd_sus_vt);
		checkBox[2] = (CheckBox) findViewById(R.id.icd_familial_conditions);
		checkBox[3] = (CheckBox) findViewById(R.id.icd_ischemic_cm);
		checkBox[4] = (CheckBox) findViewById(R.id.icd_nonischemic_cm);
		checkBox[5] = (CheckBox) findViewById(R.id.icd_long_duration_cm);
		checkBox[6] = (CheckBox) findViewById(R.id.icd_mi);
		checkBox[7] = (CheckBox) findViewById(R.id.icd_inducible_vt);
		checkBox[8] = (CheckBox) findViewById(R.id.icd_qrs_duration_long);
		// exclusions
		checkBox[9] = (CheckBox) findViewById(R.id.icd_cardiogenic_shock);
		checkBox[10] = (CheckBox) findViewById(R.id.icd_recent_cabg);
		checkBox[11] = (CheckBox) findViewById(R.id.icd_recent_mi);
		checkBox[12] = (CheckBox) findViewById(R.id.icd_recent_mi_eps);
		checkBox[13] = (CheckBox) findViewById(R.id.icd_revascularization_candidate);
		checkBox[14] = (CheckBox) findViewById(R.id.icd_bad_prognosis);
		checkBox[15] = (CheckBox) findViewById(R.id.icd_brain_damage);

		efRadioGroup = (RadioGroup) findViewById(R.id.icd_ef_radio_group);
		nyhaRadioGroup = (RadioGroup) findViewById(R.id.icd_nyha_radio_group);
	}

	private static final int CARDIAC_ARREST = 0;
	private static final int SUS_VT = 1;
	private static final int FAMILIAL_CONDITION = 2;
	private static final int ISCHEMIC_CM = 3;
	private static final int NONISCHEMIC_CM = 4;
	private static final int LONG_DURATION_CM = 5;
	private static final int MI = 6;
	private static final int INDUCIBLE_VT = 7;
	private static final int QRS_DURATION_LONG = 8;
	private static final int CARDIOGENIC_SHOCK = 9;
	private static final int RECENT_CABG = 10;
	private static final int RECENT_MI = 11;
	private static final int RECENT_MI_EPS = 12;
	private static final int REVASCULARIZATION_CANDIDATE = 13;
	private static final int BAD_PROGNOSIS = 14;
	private static final int BRAIN_DAMAGE = 15;
	private static final int ABSOLUTE_EXCLUSION = 100;
	private static final int POSSIBLE_INDICATION = 200;

	private static final String CR = "\n";

	private RadioGroup efRadioGroup;
	private RadioGroup nyhaRadioGroup;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.instructions_button)
			displayInstructions();
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getString(R.string.cms_icd_instructions);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.icd_calculator_title));
		dialog.show();
	}

	@Override
	protected void calculateResult() {
		int result;
		// according to NCD, brain damage excludes all indications
		if (checkBox[BRAIN_DAMAGE].isChecked())
			result = BRAIN_DAMAGE;
		else if (checkBox[CARDIAC_ARREST].isChecked())
			result = CARDIAC_ARREST;
		else if (checkBox[SUS_VT].isChecked())
			result = SUS_VT;
		else if (absoluteExclusion())
			result = ABSOLUTE_EXCLUSION;
		else if (checkBox[FAMILIAL_CONDITION].isChecked())
			result = FAMILIAL_CONDITION;
		else
			result = POSSIBLE_INDICATION;
		displayResult(getResultMessage(result),
				getString(R.string.icd_calculator_title));
	}

	private Boolean absoluteExclusion() {
		return checkBox[CARDIOGENIC_SHOCK].isChecked()
				|| checkBox[REVASCULARIZATION_CANDIDATE].isChecked()
				|| checkBox[BAD_PROGNOSIS].isChecked();
	}

	private String getResultMessage(int result) {
		String message = "";
		if (result == BRAIN_DAMAGE) {
			message += getString(R.string.icd_not_approved_text);
			message += CR + getString(R.string.brain_damage_exclusion);
			return message;
		}
		// need EF and NYHA for possible CRT with Secondary indication
		Boolean efLessThan30 = efRadioGroup.getCheckedRadioButtonId() == R.id.icd_ef_lt_30;
		Boolean efLessThan35 = efLessThan30
				|| efRadioGroup.getCheckedRadioButtonId() == R.id.icd_ef_lt_35;
		Boolean nyhaIIorIII = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha2
				|| nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha3;
		Boolean nyhaIV = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha4;
		Boolean nyhaIIIorIV = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha3
				|| nyhaIV;
		Boolean crtCriteriaMet = nyhaIIIorIV
				&& checkBox[QRS_DURATION_LONG].isChecked() && efLessThan35;
		// no ef or NYHA class needed for secondary prevention
		if (result == CARDIAC_ARREST || result == SUS_VT) {
			message = getString(R.string.secondary_prevention_label);
			message += CR + getString(R.string.icd_approved_text);
			if (crtCriteriaMet)
				message += CR + getString(R.string.crt_approved_text);
			return message;
		}
		message = getString(R.string.primary_prevention_label) + CR;
		// check absolute exclusions since they apply to all other indications
		if (result == ABSOLUTE_EXCLUSION) {
			message += getString(R.string.icd_not_approved_text);
			message += CR + getString(R.string.absolute_exclusion);
			return message;
		}
		if (result == FAMILIAL_CONDITION) {
			message += getString(R.string.icd_approved_text);
			if (crtCriteriaMet)
				message += CR + getString(R.string.crt_approved_text);
			return message;
		}
		// primary prevention except for familial condition needs ef and NYHA
		// class (because NYHA class IV is an exclusion except for CRT)
		if (efRadioGroup.getCheckedRadioButtonId() < 0
				&& nyhaRadioGroup.getCheckedRadioButtonId() < 0) {
			message = getString(R.string.icd_no_ef_or_nyha_message);
			return message;
		}
		if (efRadioGroup.getCheckedRadioButtonId() < 0) {
			message = getString(R.string.icd_no_ef_message);
			return message;
		}
		if (nyhaRadioGroup.getCheckedRadioButtonId() < 0) {
			message = getString(R.string.icd_no_nyha_message);
			return message;
		}
		// Now work out possible indications
		// MADIT II -- note MADIT II explicitly excludes class IV,
		// but Guideline 8 allows class IV if QRS wide
		Boolean indicated = efLessThan30 && checkBox[MI].isChecked()
				&& (!nyhaIV || crtCriteriaMet);
		// MADIT
		Boolean maditIndication = false;
		if (!indicated) {
			indicated = efLessThan35 && checkBox[MI].isChecked()
					&& checkBox[INDUCIBLE_VT].isChecked();
			if (indicated)
				maditIndication = true;
		}
		// SCD-Heft Ischemic CM
		if (!indicated)
			indicated = efLessThan35 && checkBox[ISCHEMIC_CM].isChecked()
					&& checkBox[MI].isChecked()
					&& (nyhaIIorIII || crtCriteriaMet);
		// SCD-Heft Nonischemic CM
		if (!indicated)
			indicated = efLessThan35 && checkBox[NONISCHEMIC_CM].isChecked()
					&& (nyhaIIorIII || crtCriteriaMet)
					&& checkBox[LONG_DURATION_CM].isChecked();
		if (indicated) {
			if (checkBox[RECENT_MI].isChecked()) {
				message += getString(R.string.icd_not_approved_text);
				message += CR + getString(R.string.post_mi_time_exclusion);
			} else if (maditIndication && checkBox[RECENT_MI_EPS].isChecked()) {
				message += getString(R.string.icd_not_approved_text);
				message += CR + getString(R.string.post_mi_early_eps_exclusion);
			} else if (checkBox[RECENT_CABG].isChecked()) {
				message += getString(R.string.icd_not_approved_text);
				message += CR
						+ getString(R.string.post_revascularization_exclusion);
			} else if (crtCriteriaMet && nyhaIV) // CRT-ICD must be used for
													// NYHA IV
				message += getString(R.string.icd_crt_approved_text);
			else {
				message += getString(R.string.icd_approved_text);
				if (crtCriteriaMet)
					message += CR + getString(R.string.crt_approved_text);
			}
		} else
			message += getString(R.string.icd_not_approved_text);
		return message;
	}

	@Override
	protected void clearEntries() {
		for (int i = 0; i < checkBox.length; i++) {
			checkBox[i].setChecked(false);
		}
		efRadioGroup.clearCheck();
		nyhaRadioGroup.clearCheck();
	}
}
