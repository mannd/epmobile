package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class VtList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.vt_list, android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection.equals(getString(R.string.epicardial_vt_title)))
					epicardialVt();
				else if (selection
						.equals(getString(R.string.outflow_tract_vt_title)))
					outflowTractVt();
				else if (selection
						.equals(getString(R.string.mitral_annular_vt_title)))
					mitralAnnularVt();
			}
		});
	}

	protected void outflowTractVt() {
		Intent i = new Intent(this, OutflowVt.class);
		startActivity(i);
	}

	protected void epicardialVt() {
		Intent i = new Intent(this, EpiVt.class);
		startActivity(i);
	}

	protected void mitralAnnularVt() {
		Intent i = new Intent(this, MitralAnnularVt.class);
		startActivity(i);
	}

}
