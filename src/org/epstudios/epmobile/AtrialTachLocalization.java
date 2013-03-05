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

	private final int v1NegStep = 1;
	private final int v1PosNegStep = 2;
	private final int v1NegPosStep = 3;
	private final int v1IsoStep = 4;
	private final int v1PosStep = 5;

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

	protected void getRow21Result() {

	}

	protected void getRow22Result() {

	}

	protected void getRow23Result() {

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
			row21Button.setVisibility(View.GONE);
			row22Button.setVisibility(View.GONE);
			row23Button.setVisibility(View.GONE);
			instructionsButton.setVisibility(View.GONE);

		}
		switch (step) {
		case v1NegStep:
			step1();
			break;
		case v1PosNegStep:

			break;
		}

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
						resetSteps();
						gotoStep();
					}
				});
		dialog.show();
	}

}
