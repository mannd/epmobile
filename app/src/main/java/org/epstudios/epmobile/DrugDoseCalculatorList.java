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

public class DrugDoseCalculatorList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
        super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.drug_calculator_list,
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
						.equals(getString(R.string.dabigatran_calculator_title)))
					dabigatranCalculator();
				else if (selection
						.equals(getString(R.string.dofetilide_calculator_title)))
					dofetilideCalculator();
				else if (selection
						.equals(getString(R.string.rivaroxaban_calculator_title)))
					rivaroxabanCalculator();
				else if (selection.equals(getString(R.string.warfarin_title)))
					warfarinCalculator();
				else if (selection
						.equals(getString(R.string.sotalol_calculator_title)))
					sotalolCalculator();
				else if (selection
						.equals(getString(R.string.apixaban_calculator_title)))
					apixabanCalculator();
                else if (selection
                        .equals(getString(R.string.edoxaban_calculator_title)))
                    edoxabanCalculator();

			}
		});
	}

	private void dabigatranCalculator() {
		Intent i = new Intent(this, Dabigatran.class);
		startActivity(i);
	}

	private void dofetilideCalculator() {
		Intent i = new Intent(this, Dofetilide.class);
		startActivity(i);
	}

	private void rivaroxabanCalculator() {
		Intent i = new Intent(this, Rivaroxaban.class);
		startActivity(i);
	}

	private void warfarinCalculator() {
		Intent i = new Intent(this, Warfarin.class);
		startActivity(i);
	}

	private void sotalolCalculator() {
		Intent i = new Intent(this, Sotalol.class);
		startActivity(i);
	}

	private void apixabanCalculator() {
		Intent i = new Intent(this, Apixaban.class);
		startActivity(i);
	}

    private void edoxabanCalculator() {
        Intent i = new Intent(this, Edoxaban.class);
        startActivity(i);
    }

}
