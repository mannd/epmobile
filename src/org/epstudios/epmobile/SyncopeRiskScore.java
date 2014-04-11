package org.epstudios.epmobile;

import android.content.Intent;
import android.view.MenuItem;

public abstract class SyncopeRiskScore extends RiskScore {

	@Override
	protected void calculateResult() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this,
					SyncopeRiskScoreList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
