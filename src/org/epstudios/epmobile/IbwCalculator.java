package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;

public class IbwCalculator extends EpActivity implements OnClickListener {
	private EditText weightEditText;
	private EditText heightEditText;
	private Spinner weightSpinner;
	private Spinner heightSpinner;

	private enum WeightUnit {
		KG, LB
	};

	private enum HeightUnit {
		CM, IN
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ibw);

		weightEditText = (EditText) findViewById(R.id.weightEditText);
		heightEditText = (EditText) findViewById(R.id.heightEditText);
		weightSpinner = (Spinner) findViewById(R.id.weight_spinner);
		heightSpinner = (Spinner) findViewById(R.id.height_spinner);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, CalculatorList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {

	}

}
