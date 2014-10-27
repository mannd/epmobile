package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AtrialTachLocalization extends LocationAlgorithm implements
		OnClickListener {

	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button row21Button;
	private Button row22Button;
	private Button row23Button;
	private Button instructionsButton;
	protected TextView stepTextView;

	// Steps on non-linear in this algorithm
	private final int step1 = 1;
	private final int v24PosStep = 2;
	private final int aVLStep = 3;
	private final int bifidIIStep = 4;
	private final int negAllInfStep = 5;
	private final int negAllInf2Step = 6;
	private final int sinusRhythmPStep = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complexalgorithm);

		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		row21Button = (Button) findViewById(R.id.row_2_1_button);
		row21Button.setOnClickListener(this);
		row22Button = (Button) findViewById(R.id.row_2_2_button);
		row22Button.setOnClickListener(this);
		row23Button = (Button) findViewById(R.id.row_2_3_button);
		row23Button.setOnClickListener(this);
		instructionsButton = (Button) findViewById(R.id.instructions_button);
		instructionsButton.setOnClickListener(this);
		instructionsButton.setText(getString(R.string.instructions_label));
		stepTextView = (TextView) findViewById(R.id.stepTextView);
		step1();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, DiagnosisList.class);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yes_button:
			getYesResult();
			break;
		case R.id.no_button:
			getNoResult();
			break;
		case R.id.back_button:
			getBackResult();
			break;
		case R.id.row_2_1_button:
			getRow21Result();
			break;
		case R.id.row_2_2_button:
			getRow22Result();
			break;
		case R.id.row_2_3_button:
			getRow23Result();
			break;
		case R.id.instructions_button:
			displayInstructions();
			break;
		}

	}

	protected void step1() {
		stepTextView.setText(getString(R.string.at_v1_morphology_label));
		yesButton.setText(getString(R.string.neg_label));
		noButton.setText(getString(R.string.pos_neg_label));
		backButton.setText(getString(R.string.neg_pos_label));
		row21Button.setText(getString(R.string.iso_pos_label));
		row22Button.setText(getString(R.string.iso_label));
		row23Button.setText(getString(R.string.pos_label));
		row21Button.setVisibility(View.VISIBLE);
		row22Button.setVisibility(View.VISIBLE);
		row23Button.setVisibility(View.VISIBLE);
		instructionsButton.setVisibility(View.VISIBLE);
	}

	private void getBackResult() {
		// for Step 1, back button is neg/pos button
		if (step == step1)
			step = aVLStep;
		else
			adjustStepsBackward();
		gotoStep();
	}

	private void getNoResult() {
		switch (step) {
		case step1:
			showResult(getString(R.string.location_ct));
			break;
		case v24PosStep:
			step = negAllInfStep;
			break;
		case aVLStep:
			showResult(getString(R.string.location_cs_os_ls));
			break;
		case bifidIIStep:
			step = sinusRhythmPStep;
			break;
		case negAllInfStep:
			showResult(getString(R.string.location_ta_raa));
			break;
		case negAllInf2Step:
			showResult(getString(R.string.location_lpv_laa));
			break;
		case sinusRhythmPStep:
			showResult(getString(R.string.location_rpv));
			break;
		}
		gotoStep();
	}

	protected void getYesResult() {
		switch (step) {
		case step1:
			step = v24PosStep;
			break;
		case v24PosStep:
			showResult(getString(R.string.location_ct));
			break;
		case aVLStep:
			showResult(getString(R.string.location_sma));
			break;
		case bifidIIStep:
			step = negAllInf2Step;
			break;
		case negAllInfStep:
			showResult(getString(R.string.location_ta));
			break;
		case negAllInf2Step:
			showResult(getString(R.string.location_cs_body));
			break;
		case sinusRhythmPStep:
			showResult(getString(R.string.location_ct_rpv));
			break;
		}
		gotoStep();
	}

	protected void getRow21Result() {
		step = aVLStep;
		gotoStep();
	}

	protected void getRow22Result() {
		showResult(getString(R.string.location_r_septum_perinodal));
	}

	protected void getRow23Result() {
		step = bifidIIStep;
		gotoStep();
	}

	protected void adjustStepsBackward() {
		switch (step) {
		case v24PosStep:
		case aVLStep:
		case bifidIIStep:
			step = step1;
			break;
		case negAllInfStep:
			step = v24PosStep;
			break;
		case negAllInf2Step:
		case sinusRhythmPStep:
			step = bifidIIStep;
			break;
		}
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		final SpannableString message = new SpannableString(
				getString(R.string.at_localization_instructions) + "\n"
						+ getString(R.string.at_localization_reference));
		Linkify.addLinks(message, Linkify.WEB_URLS);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.atrial_tachycardia_localization_title));
		dialog.show();
		((TextView) dialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}

	protected void gotoStep() {
		if (step > 1) {
			row21Button.setVisibility(View.GONE);
			row22Button.setVisibility(View.GONE);
			row23Button.setVisibility(View.GONE);
			instructionsButton.setVisibility(View.GONE);
			yesButton.setText(getString(R.string.yes));
			noButton.setText(getString(R.string.no));
			backButton.setText(getString(R.string.back));
		}
		switch (step) {
		case step1:
			step1();
			break;
		case v24PosStep:
			stepTextView.setText(getString(R.string.v24_pos_step));
			break;
		case aVLStep:
			stepTextView.setText(getString(R.string.avl_step));
			yesButton.setText(getString(R.string.neg_label));
			noButton.setText(getString(R.string.pos_label));
			break;
		case bifidIIStep:
			stepTextView.setText(getString(R.string.bifid_II_step));
			break;
		case negAllInfStep:
		case negAllInf2Step:
			stepTextView
					.setText(getString(R.string.negative_in_all_inf_leads_step));
			break;
		case sinusRhythmPStep:
			stepTextView.setText(getString(R.string.sinus_rhythm_p_wave_step));
			yesButton.setText(getString(R.string.pos_label));
			noButton.setText(getString(R.string.plus_minus_label));
			break;
		}
	}

	protected void showResult(String message) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.atrial_tachycardia_localization_title));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.done_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetSteps();
						step1();
					}
				});
		dialog.show();
	}

}
