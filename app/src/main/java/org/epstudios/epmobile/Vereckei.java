package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Vereckei extends EpActivity implements OnClickListener {
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button morphologyButton;
	private TextView stepTextView;
	private static int step = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.simplealgorithm);
        super.onCreate(savedInstanceState);

		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		morphologyButton = (Button) findViewById(R.id.morphology_button);
		morphologyButton.setOnClickListener(this);
		morphologyButton.setVisibility(View.GONE);
		stepTextView = (TextView) findViewById(R.id.stepTextView);

		step = 1; // needed to reset this when activity starts
		step1();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this,
					WctAlgorithmList.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void step1() {
		stepTextView.setText(getString(R.string.vereckei_step1_label));
		backButton.setEnabled(false);
	}

	protected void step2() {
		stepTextView.setText(getString(R.string.vereckei_step2_label));
		backButton.setEnabled(true);
	}

	protected void step3() {
		stepTextView.setText(getString(R.string.vereckei_step3_label));
		backButton.setEnabled(true);
	}

	protected void step4() {
		stepTextView.setText(getString(R.string.vereckei_step4_label));
		backButton.setEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			displayVtResult(step);
			break;
		case R.id.no_button:
			getNoResult();
			break;
		case R.id.back_button:
			step--;
			gotoStep();
			break;
		}
	}

	private void getNoResult() {
		switch (step) {
		case 1:
		case 2:
		case 3:
			step++;
			gotoStep();
			break;
		case 4:
			displaySvtResult();
			break;
		}

	}

	private void gotoStep() {
		switch (step) {
		case 1:
			step1();
			break;
		case 2:
			step2();
			break;
		case 3:
			step3();
			break;
		case 4:
			step4();
			break;
		}
	}

	private void displayVtResult(int step) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.vt_result);
		message = message + "\n" + getString(R.string.verecki_reference);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Back",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						;
					}
				});
		dialog.show();
	}

	private void displaySvtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.svt_result);
		message = message + "\n" + getString(R.string.verecki_reference);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Back",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						;
					}
				});
		dialog.show();
	}

}
