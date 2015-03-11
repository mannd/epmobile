package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SyncopeRiskScoreList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.syncope_risk_scores,
				android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection.equals(getString(R.string.syncope_sf_rule_title)))
					syncopeSfRule();
				else if (selection
						.equals(getString(R.string.syncope_martin_title)))
					martinAlgorithm();
				else if (selection
						.equals(getString(R.string.syncope_oesil_score_title)))
					oesilScore();
				else if (selection
						.equals(getString(R.string.syncope_egsys_score_title)))
					egsysScore();
			}
		});
	}

	private void syncopeSfRule() {
		Intent i = new Intent(this, SyncopeSfRule.class);
		startActivity(i);
	}

	private void martinAlgorithm() {
		Intent i = new Intent(this, MartinAlgorithm.class);
		startActivity(i);
	}

	private void oesilScore() {
		Intent i = new Intent(this, OesilScore.class);
		startActivity(i);
	}

	private void egsysScore() {
		Intent i = new Intent(this, EgsysScore.class);
		startActivity(i);
	}
}
