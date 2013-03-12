package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Entrainment extends EpActivity implements OnClickListener {
	private EditText tclEditText;
	private EditText ppiEditText;
	private EditText sqrsEditText;
	private EditText egmQrsEditText;
	private TextView sqrsTextView;
	private TextView egmQrsTextView;
	private CheckBox concealedFusionCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrainment);
		tclEditText = (EditText) findViewById(R.id.tclEditText);
		ppiEditText = (EditText) findViewById(R.id.ppiEditText);
		sqrsEditText = (EditText) findViewById(R.id.sqrsEditText);
		egmQrsEditText = (EditText) findViewById(R.id.egmQrsEditText);
		sqrsTextView = (TextView) findViewById(R.id.sqrsTextView);
		egmQrsTextView = (TextView) findViewById(R.id.egmQrsTextView);
		concealedFusionCheckBox = (CheckBox) findViewById(R.id.concealedFusionCheckBox);
		View calcButton = findViewById(R.id.calculate_button);
		View clearKeepTclButton = findViewById(R.id.clear_keep_tcl_button);
		View clearAllButton = findViewById(R.id.clear_all_button);
		View helpButton = findViewById(R.id.instructions_button);
		calcButton.setOnClickListener(this);
		clearKeepTclButton.setOnClickListener(this);
		clearAllButton.setOnClickListener(this);
		helpButton.setOnClickListener(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, ReferenceList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_button:
			calculate();
			break;
		case R.id.clear_keep_tcl_button:
			clearKeepTcl();
			break;

		case R.id.clear_all_button:
			clear();
			break;
		case R.id.instructions_button:
			displayInstructions();
			break;
		}
	}

	private void calculate() {

	}

	private void clearKeepTcl() {
		ppiEditText.setText(null);
		concealedFusionCheckBox.setChecked(false);
		sqrsTextView.setEnabled(false);
		sqrsEditText.setText(null);
		sqrsEditText.setEnabled(false);
		egmQrsTextView.setEnabled(false);
		egmQrsEditText.setText(null);

	}

	private void clear() {
		tclEditText.setText(null);
		clearKeepTcl();
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message = getString(R.string.entrainment_instructions);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.entrainment_title));
		dialog.show();
	}
}
