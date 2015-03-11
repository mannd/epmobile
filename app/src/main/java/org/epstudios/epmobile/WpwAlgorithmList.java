/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WpwAlgorithmList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.wpw_algorithm_list,
				android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection.equals(getString(R.string.arruda_title)))
					arrudaAlgorithm();
				else if (selection
						.equals(getString(R.string.modified_arruda_title)))
					modifiedArrudaAlgorithm();
				else if (selection.equals(getString(R.string.milstein_title)))
					milsteinAlgorithm();
				else if (selection
						.equals(getString(R.string.anatomy_av_annulus_title)))
					avAnnulusMap();
                else if (selection.equals(getString(R.string.davila_title)))
                    davilaAlgorithm();
			}
		});
	}

	private void arrudaAlgorithm() {
		Intent i = new Intent(this, WpwArruda.class);
		startActivity(i);
	}

	private void modifiedArrudaAlgorithm() {
		Intent i = new Intent(this, WpwModifiedArruda.class);
		startActivity(i);
	}

	private void milsteinAlgorithm() {
		Intent i = new Intent(this, WpwMilstein.class);
		startActivity(i);
	}

	private void avAnnulusMap() {
		Intent i = new Intent(this, AvAnnulusMap.class);
		startActivity(i);
	}

    private void davilaAlgorithm() {
        Intent i = new Intent(this, DavilaAlgorithm.class);
        startActivity(i);
    }

}
