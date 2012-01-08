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

public class EpMobile extends EpListActivity {
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
				if (selection
						.equals(getString(R.string.cycle_length_calculator_title)))
					intervalRateCalculator();
				else if (selection
						.equals(getString(R.string.qtc_calculator_title)))
					qtcCalculator();
				else if (selection
						.equals(getString(R.string.wct_algorithm_list_title)))
					wctAlgorithm();
				else if (selection
						.equals(getString(R.string.normal_ep_values_title)))
					normalEpValues();
				else if (selection.equals(getString(R.string.arvc_list_title)))
					arvc();
				else if (selection.equals(getString(R.string.short_qt_title)))
					shortQt();
				else if (selection
						.equals(getString(R.string.drug_dose_calculator_list_title)))
					drugDoseCalculators();
				else if (selection
						.equals(getString(R.string.icd_calculator_title)))
					icdCalculator();
				else if (selection
						.equals(getString(R.string.risk_score_list_title)))
					riskScores();
				else if (selection
						.equals(getString(R.string.wpw_algorithm_list_title)))
					wpw();
				else if (selection
						.equals(getString(R.string.long_qt_syndrome_diagnosis_title)))
					longQt();
			}
		});
	}

	private void qtcCalculator() {
		Intent i = new Intent(this, Qtc.class);
		startActivity(i);
	}

	private void intervalRateCalculator() {
		Intent i = new Intent(this, CycleLength.class);
		startActivity(i);
	}

	private void wctAlgorithm() {
		Intent i = new Intent(this, WctAlgorithmList.class);
		startActivity(i);
	}

	private void normalEpValues() {
		Intent i = new Intent(this, NormalEpValues.class);
		startActivity(i);
	}

	private void arvc() {
		Intent i = new Intent(this, ArvcList.class);
		startActivity(i);
	}

	private void shortQt() {
		Intent i = new Intent(this, ShortQt.class);
		startActivity(i);
	}

	private void drugDoseCalculators() {
		Intent i = new Intent(this, DrugDoseCalculatorList.class);
		startActivity(i);
	}

	private void icdCalculator() {
		Intent i = new Intent(this, CmsIcd.class);
		startActivity(i);
	}

	private void riskScores() {
		Intent i = new Intent(this, RiskScoreList.class);
		startActivity(i);
	}

	private void wpw() {
		Intent i = new Intent(this, WpwAlgorithmList.class);
		startActivity(i);
	}

	private void longQt() {
		Intent i = new Intent(this, LongQt.class);
		startActivity(i);
	}
}