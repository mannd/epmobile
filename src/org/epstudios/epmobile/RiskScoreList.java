package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RiskScoreList extends EpListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.risk_score_list,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection.equals(getString(R.string.chads_title)))
					chadsScore();
				else if (selection.equals(getString(R.string.chadsvasc_title)))
					chadsVascScore();
				else if (selection.equals(getString(R.string.hasbled_title)))
					hasBledScore();
				else if (selection.equals(getString(R.string.hcm_title)))
					hcmScore();
				else if (selection
						.equals(getString(R.string.hemorrhages_title)))
					hemorrhagesScore();
				else if (selection
						.equals(getString(R.string.syncope_list_title)))
					syncopeRiskScores();
				else if (selection.equals(getString(R.string.icd_risk_title)))
					IcdRiskScore();
			}
		});
	}

	private void hasBledScore() {
		Intent i = new Intent(this, HasBled.class);
		startActivity(i);
	}

	private void chadsScore() {
		Intent i = new Intent(this, Chads.class);
		startActivity(i);
	}

	private void chadsVascScore() {
		Intent i = new Intent(this, ChadsVasc.class);
		startActivity(i);
	}

	private void hcmScore() {
		Intent i = new Intent(this, Hcm.class);
		startActivity(i);
	}

	private void hemorrhagesScore() {
		Intent i = new Intent(this, Hemorrhages.class);
		startActivity(i);
	}

	private void syncopeRiskScores() {
		Intent i = new Intent(this, SyncopeRiskScoreList.class);
		startActivity(i);
	}

	private void IcdRiskScore() {
		Intent i = new Intent(this, IcdRisk.class);
		startActivity(i);
	}

}
