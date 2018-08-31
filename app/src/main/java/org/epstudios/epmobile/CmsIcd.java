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
	private static final int PRIOR_MI = 2;
	private static final int ICM = 3;
	private static final int NICM = 4;
	private static final int HIGH_RISK_CONDITION = 5;
	private static final int ICD_AT_ERI = 6;
	private static final int TRANSPLANT_LIST = 7;
	private static final int RECENT_CABG = 8;
	private static final int RECENT_MI = 9;
	private static final int REVASCULARIZATION_CANDIDATE = 10;
	private static final int CARDIOGENIC_SHOCK = 11;
	private static final int NONCARDIAC_DISEASE = 12;
	private static final int BRAIN_DAMAGE = 13;
	private static final int UNCONTROLLED_SVT = 14;

	private RadioGroup efRadioGroup;
	private RadioGroup nyhaRadioGroup;

	private CmsIcdViewModel viewModel;

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
		checkBox[CARDIAC_ARREST] = findViewById(R.id.icd_cardiac_arrest);
		checkBox[SUS_VT] = findViewById(R.id.icd_sus_vt);
		// primary prevention
		checkBox[PRIOR_MI] = findViewById(R.id.icd_mi);
		checkBox[ICM] = findViewById(R.id.icd_ischemic_cm);
		checkBox[NICM] = findViewById(R.id.icd_nonischemic_cm);
		checkBox[HIGH_RISK_CONDITION] = findViewById(R.id.icd_familial_conditions);
		// other indications
		checkBox[ICD_AT_ERI] = findViewById(R.id.icd_replacement);
		checkBox[TRANSPLANT_LIST] = findViewById(R.id.icd_transplant_bridge);
		// relative exclusions
		checkBox[RECENT_CABG] = findViewById(R.id.icd_recent_cabg);
		checkBox[RECENT_MI] = findViewById(R.id.icd_recent_mi);
		checkBox[REVASCULARIZATION_CANDIDATE] = findViewById(R.id.icd_revascularization_candidate);
		// absolute exclusions
		checkBox[CARDIOGENIC_SHOCK] = findViewById(R.id.icd_cardiogenic_shock);
		checkBox[NONCARDIAC_DISEASE] = findViewById(R.id.icd_bad_prognosis);
		checkBox[BRAIN_DAMAGE] = findViewById(R.id.icd_brain_damage);
		checkBox[UNCONTROLLED_SVT] = findViewById(R.id.icd_svt_uncontrolled_rate);
		// EF and NYHA radiogroups
		efRadioGroup = findViewById(R.id.icd_ef_radio_group);
		nyhaRadioGroup = findViewById(R.id.icd_nyha_radio_group);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.instructions_button)
			displayInstructions();
	}

	private void displayInstructions() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.cms_icd_instructions));
		builder.setTitle(getString(R.string.icd_calculator_title));
		builder.setPositiveButton(getString(R.string.ok_button_label), null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void calculateResult() {
		createViewModel();
		String message = viewModel.getMessage();
		displayResult(message, getString(R.string.icd_calculator_title));
	}

	private void createViewModel() {
		CmsIcdModel.EF ef;
		switch (efRadioGroup.getCheckedRadioButtonId()) {
			case R.id.icd_ef_lt_30:
				ef = CmsIcdModel.EF.LESS_THAN_30;
				break;
			case R.id.icd_ef_lt_35:
				ef = CmsIcdModel.EF.FROM_30_TO_35;
				break;
			case R.id.icd_ef_gt_35:
				ef = CmsIcdModel.EF.MORE_THAN_35;
				break;
			default:
				ef = CmsIcdModel.EF.NA;
		}
		CmsIcdModel.Nyha nyha;
		switch (nyhaRadioGroup.getCheckedRadioButtonId()) {
			case R.id.icd_nyha1:
				nyha = CmsIcdModel.Nyha.I;
				break;
			case R.id.icd_nyha2:
				nyha = CmsIcdModel.Nyha.II;
				break;
			case R.id.icd_nyha3:
				nyha = CmsIcdModel.Nyha.III;
				break;
			case R.id.icd_nyha4:
				nyha = CmsIcdModel.Nyha.IV;
				break;
			default:
				nyha = CmsIcdModel.Nyha.NA;
		}
		viewModel = new CmsIcdViewModel(this, checkBox[SUS_VT].isChecked(),
				checkBox[CARDIAC_ARREST].isChecked(), checkBox[PRIOR_MI].isChecked(),
				checkBox[ICM].isChecked(), checkBox[NICM].isChecked(),
				checkBox[HIGH_RISK_CONDITION].isChecked(), checkBox[ICD_AT_ERI].isChecked(),
				checkBox[TRANSPLANT_LIST].isChecked(), ef, nyha,
				checkBox[RECENT_CABG].isChecked(), checkBox[RECENT_MI].isChecked(),
				checkBox[REVASCULARIZATION_CANDIDATE].isChecked(),
				checkBox[CARDIOGENIC_SHOCK].isChecked(), checkBox[NONCARDIAC_DISEASE].isChecked(),
				checkBox[BRAIN_DAMAGE].isChecked(), checkBox[UNCONTROLLED_SVT].isChecked());
	}

	@Override
	protected void clearEntries() {
		for (CheckBox aCheckBox : checkBox) {
			aCheckBox.setChecked(false);
		}
		efRadioGroup.clearCheck();
		nyhaRadioGroup.clearCheck();
	}
}
