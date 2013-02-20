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

public class AtrialTachLocalization extends EpActivity implements
		OnClickListener {
	private Button negButton;
	private Button posNegButton;
	private Button negPosButton;
	private Button isoButton;
	private Button posButton;
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	protected TextView stepTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atlocalization);

		negButton = (Button) findViewById(R.id.neg_button);
		negButton.setOnClickListener(this);
		posNegButton = (Button) findViewById(R.id.pos_neg_button);
		posNegButton.setOnClickListener(this);
		negPosButton = (Button) findViewById(R.id.neg_pos_button);
		negPosButton.setOnClickListener(this);
		isoButton = (Button) findViewById(R.id.iso_button);
		isoButton.setOnClickListener(this);
		posButton = (Button) findViewById(R.id.pos_button);
		posButton.setOnClickListener(this);
		yesButton = (Button) findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		noButton = (Button) findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(this);
		stepTextView = (TextView) findViewById(R.id.stepTextView);

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
		case R.id.neg_button:
			getNegResult();
			break;
		case R.id.pos_neg_button:
			getPosNegResult();
			break;
		case R.id.iso_button:
			getIsoResult();
			break;
		}

	}

	private void getPosNegResult() {
		showResult(getString(R.string.location_ct));
	}

	private void getIsoResult() {
		showResult(getString(R.string.location_r_septum_perinodal));
	}

	private void getNegResult() {
		setContentView(R.layout.simplealgorithm);
		stepTextView.setText(getString(R.string.v24_pos_step));

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
