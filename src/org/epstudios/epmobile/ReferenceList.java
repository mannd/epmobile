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

public class ReferenceList extends EpListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.reference_list,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection
						.equals(getString(R.string.normal_ep_values_title)))
					normalEpValues();
				else if (selection
						.equals(getString(R.string.icd_calculator_title)))
					icdCalculator();
				else if (selection
						.equals(getString(R.string.long_qt_drugs_title)))
					longQtDrugs();
				else if (selection
						.equals(getString(R.string.brugada_drugs_title)))
					brugadaDrugs();
				else if (selection
						.equals(getString(R.string.entrainment_title)))
					entrainment();
			}
		});
	}

	private void normalEpValues() {
		Intent i = new Intent(this, NormalEpValues.class);
		startActivity(i);
	}

	private void icdCalculator() {
		Intent i = new Intent(this, CmsIcd.class);
		startActivity(i);
	}

	private void longQtDrugs() {
		Intent i = new Intent(Intent.ACTION_VIEW,
				Uri.parse(getString(R.string.long_qt_drugs_link)));
		startActivity(i);
	}

	private void brugadaDrugs() {
		Intent i = new Intent(Intent.ACTION_VIEW,
				Uri.parse(getString(R.string.brugada_drugs_link)));
		startActivity(i);
	}

	private void entrainment() {
		Intent i = new Intent(this, Entrainment.class);
		startActivity(i);
	}

}
