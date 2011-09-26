package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WctAlgorithmList extends EpListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.wct_algorithm_list, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection.equals(getString(R.string.brugada_wct_title)))
					brugadaAlgorithm();
				else if (selection.equals(getString(R.string.morphology_label)))
					morphologyCriteria();
			}
		});
	}
	
	private void brugadaAlgorithm() {
		Intent i = new Intent(this, Brugada.class);
		startActivity(i);
	}
	
	private void morphologyCriteria() {
		Intent i = new Intent(this, WctMorphologyCriteria.class);
		startActivity(i);
	}

}
