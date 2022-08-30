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
		initToolbar();
		tclEditText = findViewById(R.id.tclEditText);
		ppiEditText = findViewById(R.id.ppiEditText);
		sqrsEditText = findViewById(R.id.sqrsEditText);
		egmQrsEditText = findViewById(R.id.egmQrsEditText);
		sqrsTextView = findViewById(R.id.sqrsTextView);
		egmQrsTextView = findViewById(R.id.egmQrsTextView);
		concealedFusionCheckBox = findViewById(R.id.concealedFusionCheckBox);
		resultTextView = findViewById(R.id.resultTextView);
		View calcButton = findViewById(R.id.calculate_button);
		View clearKeepTclButton = findViewById(R.id.clear_keep_tcl_button);
		View clearAllButton = findViewById(R.id.clear_all_button);
		View helpButton = findViewById(R.id.instructions_button);
		calcButton.setOnClickListener(this);
		clearKeepTclButton.setOnClickListener(this);
		clearAllButton.setOnClickListener(this);
		helpButton.setOnClickListener(this);

		concealedFusionCheckBox.setOnClickListener(v -> {
			boolean checked = concealedFusionCheckBox.isChecked();
			sqrsTextView.setEnabled(checked);
			sqrsEditText.setEnabled(checked);
			egmQrsTextView.setEnabled(checked);
			egmQrsEditText.setEnabled(checked);
			if (!checked) {
				sqrsEditText.setText(null);
				egmQrsEditText.setText(null);
			}

		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, ReferenceList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.calculate_button) {
			calculate();
		}
		else if (id == R.id.clear_keep_tcl_button) {
			clearKeepTcl();
		}
		else if (id == R.id.clear_all_button) {
			clear();
		}
		else if (id == R.id.instructions_button) {
			displayInstructions();
		}
	}

	private void calculate() {
		String tclText = tclEditText.getText().toString();
		String ppiText = ppiEditText.getText().toString();
		String sqrsText = sqrsEditText.getText().toString();
		String egmQrsText = egmQrsEditText.getText().toString();
		resultTextView.setTextAppearance(this,
				android.R.style.TextAppearance_Medium);
		try {
			int tcl = Integer.parseInt(tclText);
			int ppi = Integer.parseInt(ppiText);
			int ppiMinusTcl = ppi - tcl;
			if (ppiMinusTcl < 0)
				throw new NumberFormatException();
			String message;
			if (!concealedFusionCheckBox.isChecked()) {
				if (ppiMinusTcl > 30)
					message = getString(R.string.entrainment_remote_site_message);
				else {
					message = getString(R.string.entrainment_outer_loop_message);
					resultTextView.setTextColor(getResources().getColor(
							R.color.cyan));
				}
			} else { // concealed fusion present!
				if (ppiMinusTcl > 30) {
					message = getString(R.string.entrainment_bystander_message);
				} else {
					message = getString(R.string.entrainment_inner_loop_message);
					resultTextView.setTextColor(getResources().getColor(
							R.color.green));
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
							int egmMinusQrs = Math.abs(egmQrs - sqrs);
							message += " ";
							if (egmMinusQrs <= 20)
								message += getString(R.string.entrainment_egm_match_message);
							else
								message += getString(R.string.entrainment_egm_no_match_message);
							// Note: iOS version uses doubles, so no bug there.
							boolean hasHighChanceOfSuccess = ppiMinusTcl <= 10 && egmQrs / (double) tcl <= 0.7
									&& egmMinusQrs <= 10;
							if (hasHighChanceOfSuccess) {
								message += getString(R.string.entrainment_high_chance_success);
							}
						}
					}
				}
			}

			message = getString(R.string.ppi_minus_tcl_label)
					+ ppiMinusTcl + ". " + message;
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
		resultTextView.setTextAppearance(this,
				android.R.style.TextAppearance_Medium);
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
