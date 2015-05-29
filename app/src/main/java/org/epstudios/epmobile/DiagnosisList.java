package org.epstudios.epmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiagnosisList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.diagnosis_list,
				android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);


		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection
						.equals(getString(R.string.wct_algorithm_list_title)))
					wctAlgorithm();
				else if (selection.equals(getString(R.string.short_qt_title)))
					shortQt();
				else if (selection
						.equals(getString(R.string.wpw_algorithm_list_title)))
					wpw();
				else if (selection
						.equals(getString(R.string.lqt_syndrome_title)))
					longQt();
				else if (selection.equals(getString(R.string.lvh_list_title)))
					lvhList();
				else if (selection
						.equals(getString(R.string.brugada_ecg_title)))
					brugadaEcg();
				else if (selection.equals(getString(R.string.vt_list_title)))
					vtList();
				else if (selection
						.equals(getString(R.string.atrial_tachycardia_localization_title)))
					atrialTachLocalization();
                else if (selection.equals(getString(R.string.rvh_title)))
                    rvhCriteria();
				else if (selection
						.equals(getString(R.string.arvc_2010_criteria_title)))
					arvc2010();
				else if (selection
						.equals(getString(R.string.arvc_old_criteria_title)))
					arvcOld();
			}
		});
	}

	protected void vtList() {
		Intent i = new Intent(this, VtList.class);
		startActivity(i);
	}

	private void wctAlgorithm() {
		Intent i = new Intent(this, WctAlgorithmList.class);
		startActivity(i);
	}

	private void shortQt() {
		Intent i = new Intent(this, ShortQt.class);
		startActivity(i);
	}

	private void wpw() {
		Intent i = new Intent(this, WpwAlgorithmList.class);
		startActivity(i);
	}

	private void longQt() {
		Intent i = new Intent(this, LongQtList.class);
		startActivity(i);
	}

	private void lvhList() {
		Intent i = new Intent(this, LvhList.class);
		startActivity(i);
	}

	private void brugadaEcg() {
		Intent i = new Intent(this, BrugadaEcg.class);
		startActivity(i);
	}

	private void atrialTachLocalization() {
		Intent i = new Intent(this, AtrialTachLocalization.class);
		startActivity(i);
	}

    private void rvhCriteria() {
        Intent i = new Intent(this, LinkView.class);
        i.putExtra("EXTRA_URL", "file:///android_asset/rvh.html");
        i.putExtra("EXTRA_TITLE", getString(R.string.rvh_title));
        startActivity(i);
    }

	private void arvc2010() {
		Intent i = new Intent(this, Arvc.class);
		startActivity(i);
	}

	private void arvcOld() {
		Intent i = new Intent(this, ArvcOld.class);
		startActivity(i);
	}
}
