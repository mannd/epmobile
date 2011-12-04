package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class CmsIcd extends RiskScore {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmsicd);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		View instructionsButton = findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);

		checkBox = new CheckBox[15];

		checkBox[0] = (CheckBox) findViewById(R.id.icd_cardiac_arrest);
		checkBox[1] = (CheckBox) findViewById(R.id.icd_sus_vt);
		checkBox[2] = (CheckBox) findViewById(R.id.icd_familial_conditions);
		checkBox[3] = (CheckBox) findViewById(R.id.icd_ischemic_cm);
		checkBox[4] = (CheckBox) findViewById(R.id.icd_nonischemic_cm);
		checkBox[5] = (CheckBox) findViewById(R.id.icd_long_duration_cm);
		checkBox[6] = (CheckBox) findViewById(R.id.icd_mi);
		checkBox[7] = (CheckBox) findViewById(R.id.icd_qrs_duration_long);
		// exclusions
		checkBox[8] = (CheckBox) findViewById(R.id.icd_cardiogenic_shock);
		checkBox[9] = (CheckBox) findViewById(R.id.icd_recent_cabg);
		checkBox[10] = (CheckBox) findViewById(R.id.icd_recent_mi);
		checkBox[11] = (CheckBox) findViewById(R.id.icd_recent_mi_eps);
		checkBox[12] = (CheckBox) findViewById(R.id.icd_revascularization_candidate);
		checkBox[13] = (CheckBox) findViewById(R.id.icd_bad_prognosis);
		checkBox[14] = (CheckBox) findViewById(R.id.icd_brain_damage);
		
		efRadioGroup = (RadioGroup) findViewById(R.id.icd_ef_radio_group);
		nyhaRadioGroup = (RadioGroup) findViewById(R.id.icd_nyha_radio_group);
		
		clearEntries();
	}
	
	private static final int CARDIAC_ARREST = 0;
	private static final int SUS_VT = 1;
	private static final int FAMILIAL_CONDITION = 2;
	private static final int ISCHEMIC_CM = 3;
	private static final int NONISCHEMIC_CM = 4;
	private static final int LONG_DURATION_CM = 5;
	private static final int MI = 6;
	private static final int QRS_DURATION_LONG = 7;
	private static final int CARDIOGENIC_SHOCK = 8;
	private static final int RECENT_CABG = 9;
	private static final int RECENT_MI = 10;
	private static final int RECENT_MI_EPS = 11;
	private static final int REVASCULARIZATION_CANDIDATE = 12;
	private static final int BAD_PROGNOSIS = 13;
	private static final int BRAIN_DAMAGE = 14;
	
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
		int result = -1;
		if (checkBox[CARDIAC_ARREST].isChecked())
			result = CARDIAC_ARREST;
		else if (checkBox[SUS_VT].isChecked())
			result = SUS_VT;
		displayResult(result);
	}

	@Override
	protected String getResultMessage(int result) {
		String message = "";
		switch (result) {
		case CARDIAC_ARREST:
		case SUS_VT:
			message = getString(R.string.icd_approved_text);
			message += "\n" + getString(R.string.icd_class1_indication);
			break;
			
		}
		return message;
	}

	@Override
	protected String getDialogTitle() {
		return getString(R.string.icd_calculator_title);
	}
	
	@Override
	protected void clearEntries() {
		super.clearEntries();
		efRadioGroup.clearCheck();
		nyhaRadioGroup.clearCheck();
		
	}

}
