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

public class AtrialTachLocalization extends EpActivity implements
		OnClickListener {

	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button instructionsButton;
	protected TextView stepTextView;

	protected int step = 1;
	private int priorStep = 1;
	private int priorStep1 = 1;
	private int priorStep2 = 1;
	private int priorStep3 = 1;
	private int priorStep4 = 1;
	private int priorStep5 = 1;
	private int priorStep6 = 1;
	private int priorStep7 = 1;

	private final int v1NegStep = 1;
	private final int v1PosNegStep = 2;
	private final int v1NegPosStep = 3;
	private final int v1IsoStep = 4;
	private final int v1PosStep = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplealgorithm);

		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		instructionsButton = (Button) findViewById(R.id.morphology_button);
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
		case R.id.morphology_button:
			displayInstructions();
			break;
		}

	}

	private String getV1Question(String label) {
		return getString(R.string.at_v1_morphology_label) + " " + label + "?";
	}

	protected void step1() {
		stepTextView.setText(getV1Question(getString(R.string.neg_label)));
		backButton.setEnabled(false);
		instructionsButton.setVisibility(View.VISIBLE);
	}

	private void getBackResult() {
		adjustStepsBackward();
		gotoStep();
	}

	private void getNoResult() {
		adjustStepsForward();
		switch (step) {
		case v1NegStep:
			step = v1PosNegStep;
			break;

		}
		gotoStep();
	}

	protected void getYesResult() {
		adjustStepsForward();
		switch (step) {

		}
		gotoStep();
	}

	private void displayInstructions() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		final SpannableString message = new SpannableString(
				getString(R.string.outflow_vt_instructions));
		Linkify.addLinks(message, Linkify.WEB_URLS);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.outflow_tract_vt_title));
		dialog.show();
		((TextView) dialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}

	protected void gotoStep() {
		if (step > 1) {
			backButton.setEnabled(true);
			instructionsButton.setVisibility(View.GONE);

		}
		switch (step) {
		case v1NegStep:
			step1();
			break;
		case v1PosNegStep:
			stepTextView
					.setText(getV1Question(getString(R.string.pos_neg_label)));
			break;
		}

	}

	protected void adjustStepsForward() {
		priorStep7 = priorStep6;
		priorStep6 = priorStep5;
		priorStep5 = priorStep4;
		priorStep4 = priorStep3;
		priorStep3 = priorStep2;
		priorStep2 = priorStep1;
		priorStep1 = priorStep;
		priorStep = step;
	}

	protected void adjustStepsBackward() {
		step = priorStep;
		priorStep = priorStep1;
		priorStep1 = priorStep2;
		priorStep2 = priorStep3;
		priorStep3 = priorStep4;
		priorStep4 = priorStep5;
		priorStep5 = priorStep6;
		priorStep6 = priorStep7;
	}

	private void resetSteps() {
		priorStep7 = priorStep6 = priorStep5 = priorStep4 = 1;
		priorStep3 = priorStep2 = priorStep1 = priorStep = step = 1;
	}

	protected void showResult(String message) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setTitle(getString(R.string.at_location_label));
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
						// resetSteps();
						// gotoStep();
					}
				});
		dialog.show();
	}

}
