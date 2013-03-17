package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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
	private TextView resultTextView;

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
		resultTextView = (TextView) findViewById(R.id.resultTextView);
		View calcButton = findViewById(R.id.calculate_button);
		View clearKeepTclButton = findViewById(R.id.clear_keep_tcl_button);
		View clearAllButton = findViewById(R.id.clear_all_button);
		View helpButton = findViewById(R.id.instructions_button);
		calcButton.setOnClickListener(this);
		clearKeepTclButton.setOnClickListener(this);
		clearAllButton.setOnClickListener(this);
		helpButton.setOnClickListener(this);

		concealedFusionCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = concealedFusionCheckBox.isChecked();
				sqrsTextView.setEnabled(checked);
				sqrsEditText.setEnabled(checked);
				egmQrsTextView.setEnabled(checked);
				egmQrsEditText.setEnabled(checked);
				if (!checked) {
					sqrsEditText.setText(null);
					egmQrsEditText.setText(null);
				}

			}

		});

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
		String tclText = tclEditText.getText().toString();
		String ppiText = ppiEditText.getText().toString();
		String sqrsText = sqrsEditText.getText().toString();
		String egmQrsText = egmQrsEditText.getText().toString();
		resultTextView.setTextColor(Color.LTGRAY);
		try {
			int tcl = Integer.parseInt(tclText);
			int ppi = Integer.parseInt(ppiText);
			int ppiMinusTcl = ppi - tcl;
			if (ppiMinusTcl < 0)
				throw new NumberFormatException();
			String message = "";
			if (!concealedFusionCheckBox.isChecked()) {
				if (ppiMinusTcl > 30)
					message = getString(R.string.entrainment_remote_site_message);
				else {
					message = getString(R.string.entrainment_outer_loop_message);
					resultTextView.setTextColor(Color.CYAN);
				}
			} else { // concealed fusion present!
				if (ppiMinusTcl > 30) {
					message = getString(R.string.entrainment_bystander_message);
				} else {
					message = getString(R.string.entrainment_inner_loop_message);
					resultTextView.setTextColor(Color.GREEN);
					int egmQrs = 0;
					int sqrs = 0;
					boolean hasEgmQrs = false;
					boolean hasSqrs = false;
					boolean invalidSqrs = false;
					if (egmQrsText.length() != 0) {
						egmQrs = Integer.parseInt(egmQrsText);
						hasEgmQrs = true;
					}
					if (sqrsText.length() != 0) {
						sqrs = Integer.parseInt(sqrsText);
						hasSqrs = true;
					}
					if (hasSqrs) {
						double sQrsPercent = (double) sqrs / tcl;
						message += " ";
						if (sQrsPercent < .3)
							message += getString(R.string.exit_site_label);
						else if (sQrsPercent <= .5)
							message += getString(R.string.central_site_label);
						else if (sQrsPercent <= .7)
							message += getString(R.string.proximal_site_label);
						else if (sQrsPercent <= 1)
							message += getString(R.string.entry_site_label);
						else {
							message += getString(R.string.invalid_sqrs_label);
							invalidSqrs = true;
						}
						if (hasEgmQrs && !invalidSqrs) {
							int egmMinusQrs = egmQrs - sqrs;
							message += " ";
							if (Math.abs(egmMinusQrs) <= 20)
								message += getString(R.string.entrainment_egm_match_message);
							else
								message += getString(R.string.entrainment_egm_no_match_message);
						}
					}
				}
			}

			message = getString(R.string.ppi_minus_tcl_label)
					+ String.valueOf(ppiMinusTcl) + ". " + message;
			resultTextView.setText(message);

		} catch (NumberFormatException e) {
			resultTextView.setText(getString(R.string.invalid_warning));
			resultTextView.setTextColor(Color.RED);
		}

	}

	private void clearKeepTcl() {
		ppiEditText.setText(null);
		concealedFusionCheckBox.setChecked(false);
		sqrsTextView.setEnabled(false);
		sqrsEditText.setText(null);
		sqrsEditText.setEnabled(false);
		egmQrsTextView.setEnabled(false);
		egmQrsEditText.setText(null);
		resultTextView.setTextColor(Color.LTGRAY);
		resultTextView.setText(null);

	}

	private void clear() {
		tclEditText.setText(null);
		clearKeepTcl();
	}

	private void displayInstructions() {
		final SpannableString message = new SpannableString(
				getString(R.string.entrainment_instructions) + "\n\n"
						+ getString(R.string.entrainment_reference));
		Linkify.addLinks(message, Linkify.WEB_URLS);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.entrainment_title));
		dialog.show();
		((TextView) dialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
