package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class VtList extends EpListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.vt_list, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection.equals(getString(R.string.epicardial_vt_title)))
					epicardialVt();
				else if (selection
						.equals(getString(R.string.outflow_tract_vt_title)))
					outflowTractVt();
			}
		});
	}

	protected void outflowTractVt() {
		// TODO Auto-generated method stub

	}

	protected void epicardialVt() {
		Intent i = new Intent(this, EpiVt.class);
		startActivity(i);
	}

}
