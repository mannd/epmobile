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

	private static final int SUS_VT = 0;
	private static final int CARDIAC_ARREST = 1;
	private static final int MI = 2;
	private static final int ISCHEMIC_CM = 3;
	private static final int NONISCHEMIC_CM = 4;
	private static final int FAMILIAL_CONDITION = 5;
	private static final int ICD_ERI = 6;
	private static final int TRANSPLANT_BRIDGE = 7;
	private static final int RECENT_CABG = 8;
	private static final int RECENT_MI = 9;
	private static final int REVASCULARIZATION_CANDIDATE = 10;
	private static final int CARDIOGENIC_SHOCK = 11;
	private static final int BAD_PROGNOSIS = 12;
	private static final int BRAIN_DAMAGE = 13;
	private static final int SVT_UNCONTROLLED_RATE = 14;
	// Result codes
	private static final int ABSOLUTE_EXCLUSION = 100;
	private static final int NO_INDICATIONS_CHECKED = 105;
	private static final int SECONDARY_PREVENTION = 110;
	private static final int EF_NOT_MEASURED = 120;
	private static final int POSSIBLE_INDICATION = 200;

	private static final String CR = "\n";

	private RadioGroup efRadioGroup;
	private RadioGroup nyhaRadioGroup;

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
		checkBox = new CheckBox[15];

		// secondary prevention
		checkBox[CARDIAC_ARREST] = (CheckBox) findViewById(R.id.icd_cardiac_arrest);
		checkBox[SUS_VT] = (CheckBox) findViewById(R.id.icd_sus_vt);
		// primary prevention
		checkBox[MI] = (CheckBox) findViewById(R.id.icd_mi);
		checkBox[ISCHEMIC_CM] = (CheckBox) findViewById(R.id.icd_ischemic_cm);
		checkBox[NONISCHEMIC_CM] = (CheckBox) findViewById(R.id.icd_nonischemic_cm);
		checkBox[FAMILIAL_CONDITION] = (CheckBox) findViewById(R.id.icd_familial_conditions);
		// other indications
		checkBox[ICD_ERI] = (CheckBox) findViewById(R.id.icd_replacement);
		checkBox[TRANSPLANT_BRIDGE] = (CheckBox) findViewById(R.id.icd_transplant_bridge);
		// relative exclusions
		checkBox[RECENT_CABG] = (CheckBox) findViewById(R.id.icd_recent_cabg);
		checkBox[RECENT_MI] = (CheckBox) findViewById(R.id.icd_recent_mi);
		checkBox[REVASCULARIZATION_CANDIDATE] = (CheckBox) findViewById(R.id.icd_revascularization_candidate);
		// absolute exclusions
		checkBox[CARDIOGENIC_SHOCK] = (CheckBox) findViewById(R.id.icd_cardiogenic_shock);
		checkBox[BAD_PROGNOSIS] = (CheckBox) findViewById(R.id.icd_bad_prognosis);
		checkBox[BRAIN_DAMAGE] = (CheckBox) findViewById(R.id.icd_brain_damage);
		checkBox[SVT_UNCONTROLLED_RATE] = (CheckBox) findViewById(R.id.icd_svt_uncontrolled_rate);

		efRadioGroup = (RadioGroup) findViewById(R.id.icd_ef_radio_group);
		nyhaRadioGroup = (RadioGroup) findViewById(R.id.icd_nyha_radio_group);
	}


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
		if (absoluteExclusion())
			result = ABSOLUTE_EXCLUSION;
		else if (noIndicationsChecked())
			result = NO_INDICATIONS_CHECKED;
		else if (efNotMeasured())
			result = EF_NOT_MEASURED;
		else if (secondaryPrevention())
			result = SECONDARY_PREVENTION;
		else if (checkBox[FAMILIAL_CONDITION].isChecked())
			result = FAMILIAL_CONDITION;
		else if (checkBox[ICD_ERI].isChecked())
		    result = ICD_ERI;
		else if (checkBox[TRANSPLANT_BRIDGE].isChecked())
			result = TRANSPLANT_BRIDGE;
		else  // this is a catch-all, further processing done in getResultMessage()
			result = POSSIBLE_INDICATION;
		displayResult(getResultMessage(result),
				getString(R.string.icd_calculator_title));
	}

	private Boolean absoluteExclusion() {
		return checkBox[CARDIOGENIC_SHOCK].isChecked()
				|| checkBox[BRAIN_DAMAGE].isChecked()
				|| checkBox[BAD_PROGNOSIS].isChecked()
				|| checkBox[SVT_UNCONTROLLED_RATE].isChecked();
	}

	private Boolean temporaryExclusion() {
		return  waitingPeriod() || checkBox[REVASCULARIZATION_CANDIDATE].isChecked();
	}

	private Boolean waitingPeriod() {
		return checkBox[RECENT_MI].isChecked()
				|| checkBox[RECENT_CABG].isChecked();
	}

	private Boolean noIndicationsChecked() {
		return !(secondaryPrevention() || primaryPrevention() || otherIndication());
	}

	private Boolean secondaryPrevention() {
		return checkBox[CARDIAC_ARREST].isChecked() || checkBox[SUS_VT].isChecked();
	}

	private Boolean primaryPrevention() {
		return checkBox[MI].isChecked()
				|| checkBox[ISCHEMIC_CM].isChecked()
				|| checkBox[NONISCHEMIC_CM].isChecked()
				|| checkBox[FAMILIAL_CONDITION].isChecked();
	}

	private Boolean otherIndication() {
		return checkBox[ICD_ERI].isChecked()
				|| checkBox[TRANSPLANT_BRIDGE].isChecked();
	}

	private Boolean efNotMeasured() {
	    return efRadioGroup.getCheckedRadioButtonId() < 0;
	}

	private Boolean nyhaClassNotMeasured() {
		return nyhaRadioGroup.getCheckedRadioButtonId() < 0;
	}

	private String notApprovedMessage(int indicationLabel, int details) {
		String message = "";
		if (indicationLabel != 0) {
			message += getString(indicationLabel) + CR;
		}
		message += getString(R.string.icd_not_approved_text);
		if (details != 0) {
			message += CR + getString(details);
		}
		return message;
	}

	private String notApprovedMessage(int indicationLabel, int details, Boolean waitingPeriod) {
		String message = notApprovedMessage(indicationLabel, details);
		if (waitingPeriod) {
			message += CR + getString(R.string.icd_approved_after_waiting_period);
		}
		return message;
	}

	private String icdApprovedMessage(int indication, Boolean needsSdmEncounter) {
		String message = getString(indication);
		message += CR + getString(R.string.icd_approved_text);
		if (needsSdmEncounter) {
			message += CR + getString(R.string.cms_icd_shared_decision_message);
		}
		return message;
	}

	private String icdApprovalUnclearMessage(int details) {
		String message = getString(R.string.icd_approval_unclear_message) + CR;
		return message + getString(details);
	}

	private String icdApprovedMessage(int indication, Boolean needsSdmEncounter,
									  Boolean noWaitingPeriod) {
	    String message = icdApprovedMessage(indication, needsSdmEncounter);
	    if (noWaitingPeriod) {
			message += CR + getString(R.string.icd_exceptions_apply);
		}
		return message;
	}

	private String getResultMessage(int result) {
	    // Figure out what buttons were pushed
		// Some primary prevention indications subject to temporary exclusions
		Boolean temporaryExclusion = temporaryExclusion();
		Boolean waitingPeriod = waitingPeriod();
		// Need to know EF for other primary prevention indications
		Boolean efLessThan30 = efRadioGroup.getCheckedRadioButtonId() == R.id.icd_ef_lt_30;
		Boolean ef30To35 = efRadioGroup.getCheckedRadioButtonId() == R.id.icd_ef_lt_35;
		Boolean efLessThan35 = efLessThan30
				|| ef30To35;
		Boolean nyhaI = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha1;
		Boolean nyhaIIorIII = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha2
				|| nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha3;
		Boolean nyhaIV = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha4;
		Boolean nyhaIIIorIV = nyhaRadioGroup.getCheckedRadioButtonId() == R.id.icd_nyha3
				|| nyhaIV;
		String message = "";
		if (result == ABSOLUTE_EXCLUSION) {
			message = notApprovedMessage(0, R.string.absolute_exclusion);
		}
		else if (result == NO_INDICATIONS_CHECKED) {
			message = icdApprovalUnclearMessage(R.string.no_indications_selected_message);
		}
		// EF must be measured for all ICD candidates
        else if (result == EF_NOT_MEASURED) {
			message = icdApprovalUnclearMessage(R.string.ef_not_measured_text);
		}
		else if (result == SECONDARY_PREVENTION) {
			message = icdApprovedMessage(R.string.secondary_prevention_label, false);
		}
		else if (result == FAMILIAL_CONDITION) {
		    message = icdApprovedMessage(R.string.primary_prevention_label, true);
		}
		// deal with ICD replacement next
		else if (result == ICD_ERI) {
			message = icdApprovedMessage(R.string.other_indication_label, false, waitingPeriod);
		}
		else if (primaryPrevention() && nyhaClassNotMeasured()) {
			message = icdApprovalUnclearMessage(R.string.icd_no_nyha_message);
		}
		// Now work out possible primary prevention indications
		else {
			// MADIT II -- note MADIT II explicitly excludes class IV,
			Boolean indicated = efLessThan30 && checkBox[MI].isChecked()
					&& !nyhaIV;
			// Handle situation where user checked MI box but possibly forgot to check a CM box
			Boolean possiblyIndicated = ef30To35 && checkBox[MI].isChecked()
					&& !(checkBox[ISCHEMIC_CM].isChecked() || checkBox[NONISCHEMIC_CM].isChecked())
					&& nyhaIIorIII;
			// Handle situation where user checked ICM but not MI and Class I and EF <= 30%
			Boolean possiblyForgotMI = checkBox[ISCHEMIC_CM].isChecked() && !checkBox[MI].isChecked()
					&& efLessThan30 && nyhaI;
			// MADIT indication eliminated in new guidelines
			// SCD-Heft Ischemic CM
			if (!indicated)
				indicated = efLessThan35 && checkBox[ISCHEMIC_CM].isChecked()
						&& nyhaIIorIII;
			// SCD-Heft Nonischemic CM
			if (!indicated)
				indicated = efLessThan35 && checkBox[NONISCHEMIC_CM].isChecked()
						&& nyhaIIorIII;
			if (possiblyIndicated) {
				message = notApprovedMessage(R.string.primary_prevention_label, R.string.icd_forgot_to_check_cm_checkbox);
			}
			else if (possiblyForgotMI) {
				message = notApprovedMessage(R.string.primary_prevention_label, R.string.icd_possibly_forgot_to_check_mi_checkbox);
			}
			else if (indicated) {
				if (temporaryExclusion) {
					message = notApprovedMessage(R.string.primary_prevention_label, R.string.icd_approval_affected_by_waiting_period, waitingPeriod);
				} else {
					message = icdApprovedMessage(R.string.primary_prevention_label, true);
				}
			}
			// handle transplant here
			else {
				if (result == TRANSPLANT_BRIDGE) {
					message = icdApprovalUnclearMessage(R.string.icd_transplant_bridge_message);
				}
				else {
					message = notApprovedMessage(R.string.primary_prevention_label, 0);
				}
			}
		}
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
