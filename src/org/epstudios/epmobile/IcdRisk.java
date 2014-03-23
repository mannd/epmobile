package org.epstudios.epmobile;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class IcdRisk extends DiagnosticScore {
	CheckBox sexCheckBox;
	RadioGroup admittedRadioGroup;
	RadioGroup nyhaClassRadioGroup;
	CheckBox noPriorCabgCheckBox;
	CheckBox currentDialysisCheckBox;
	CheckBox chronicLungDiseaseCheckBox;
	RadioGroup abnormalConductionRadioGroup;
	RadioGroup procedureTypeRadioGroup;
	RadioGroup icdTypeRadioGroup;
	RadioGroup sodiumRadioGroup;
	RadioGroup hgbRadioGroup;
	RadioGroup bunRadioGroup;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, RiskScoreList.class);
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
		setContentView(R.layout.icdrisk);
	}

	@Override
	protected void calculateResult() {
		// TODO Auto-generated method stub
		displayResult("Test", "test title");

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void clearEntries() {
		// TODO Auto-generated method stub

	}

}
