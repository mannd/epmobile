package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CalculatorList extends EpActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selectionlist);
        super.onCreate(savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.calculator_list, android.R.layout.simple_list_item_1);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = ((TextView) view).getText().toString();
				if (selection
						.equals(getString(R.string.cycle_length_calculator_title)))
					intervalRateCalculator();
				else if (selection
						.equals(getString(R.string.qtc_calculator_title)))
					qtcCalculator();
				else if (selection
						.equals(getString(R.string.drug_dose_calculator_list_title)))
					drugDoseCalculators();
				else if (selection
						.equals(getString(R.string.day_calculator_title)))
					icdDayCalculator();
				else if (selection
						.equals(getString(R.string.ibw_calculator_title)))
					ibwCalculator();
                else if (selection.equals(getString(R.string.creatinine_clearance_calculator_title)))
                    creatinineClearanceCalculator();


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

	private void drugDoseCalculators() {
		Intent i = new Intent(this, DrugDoseCalculatorList.class);
		startActivity(i);
	}

	private void icdDayCalculator() {
		Intent i = new Intent(this, DayCalculator.class);
		startActivity(i);
	}

	private void ibwCalculator() {
		Intent i = new Intent(this, IbwCalculator.class);
		startActivity(i);
	}

    private void creatinineClearanceCalculator() {
        Intent i = new Intent(this, CreatinineClearanceCalculator.class);
        startActivity(i);
    }

}
