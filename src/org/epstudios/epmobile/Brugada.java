package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Brugada extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplealgorithm);
		
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
		
		step = 1;	// needed to reset this when activity starts
		step1();
	}

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
		case R.id.morphology_button:
			displayMorphologyCriteria();
			break;
		}
	}
	
	private void step1() {
		stepTextView.setText(getString(R.string.brugada_step1));
		backButton.setEnabled(false);
	}
	
	private void step2() {
		stepTextView.setText(getString(R.string.brugada_step_2));
		backButton.setEnabled(true);
	}
	
	private void step3() {
		stepTextView.setText(getString(R.string.brugada_step_3));
		backButton.setEnabled(true);		
	}
	
	private void step4() {
		stepTextView.setText(getString(R.string.brugada_step_4));
		backButton.setEnabled(true);		
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
		if (step < 4)
			morphologyButton.setVisibility(View.GONE);
		else
			morphologyButton.setVisibility(View.VISIBLE);
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
		String sens = "";
		String spec = "";
		switch (step) {
		case 1:
		case 2:
			sens=".21";
			spec="1.0";
			break;
		case 3:
			sens=".82";
			spec=".98";
			break;
		case 4:
			sens=".987";
			spec=".965";
			break;
		}
		String message;
		message = getString(R.string.vt_result);
		message = message + " (Sens=" + sens + ", Spec=" + spec + ") ";
		message = message + getString(R.string.brugada_reference);
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
	
	private void displaySvtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.svt_result);
		message = message + " (Sens=.965, Spec=.967) ";
		message = message + getString(R.string.brugada_reference);
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
	
	private void displayMorphologyCriteria() {
		Intent i = new Intent(this, BrugadaMorphologyCriteria.class);
		startActivity(i);
	}
	
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button morphologyButton;
	private TextView stepTextView;
	private static int step = 1;


}
