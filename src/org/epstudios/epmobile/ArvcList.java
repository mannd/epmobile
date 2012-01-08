package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ArvcList extends EpListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.arvc_criteria_list,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection
						.equals(getString(R.string.arvc_2010_criteria_title)))
					arvc2010();
				else if (selection
						.equals(getString(R.string.arvc_old_criteria_title)))
					arvcOld();
			}
		});
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
