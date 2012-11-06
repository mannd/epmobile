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

public class EpMobile extends EpMainListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.main_index, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence selection = ((TextView) view).getText();
				if (selection.equals(getString(R.string.calculator_list_title)))
					calculatorList();
				else if (selection
						.equals(getString(R.string.reference_list_title)))
					referenceList();
				else if (selection
						.equals(getString(R.string.risk_score_list_title)))
					riskScores();
				else if (selection
						.equals(getString(R.string.diagnosis_list_title)))
					diagnosisList();
			}
		});
	}

	private void calculatorList() {
		Intent i = new Intent(this, CalculatorList.class);
		startActivity(i);
	}

	private void diagnosisList() {
		Intent i = new Intent(this, DiagnosisList.class);
		startActivity(i);
	}

	private void riskScores() {
		Intent i = new Intent(this, RiskScoreList.class);
		startActivity(i);
	}

	private void referenceList() {
		Intent i = new Intent(this, ReferenceList.class);
		startActivity(i);
	}

}