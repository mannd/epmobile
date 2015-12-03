package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RiskScoreList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.risk_score_list,
				android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
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
                else if (selection.equals(getString(R.string.hcm_scd_esc_score_title)))
                    hcmScdRisk();
				else if (selection.equals(getString(R.string.same_tt2r2_title)))
					sameTt2r2Score();
                else if (selection.equals(getString(R.string.atria_bleeding_score_title)))
                    atriaBleedingScore();
                else if (selection.equals(getString(R.string.atria_stroke_score_title)))
                    atriaStrokeScore();
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

	private void hcmScdRisk() {
		startActivity(new Intent(this, HcmScd.class));
	}

	private void sameTt2r2Score() {
		startActivity(new Intent(this, SameTtr.class));
	}

    private void atriaBleedingScore() {
        startActivity(new Intent(this, AtriaBleed.class));
    }

    private void atriaStrokeScore() {
		startActivity(new Intent(this, AtriaStroke.class));
	}

}
