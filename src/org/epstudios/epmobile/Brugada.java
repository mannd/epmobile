package org.epstudios.epmobile;

import android.app.Dialog;
import android.app.AlertDialog;
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
		
		step1();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			displayVtResult();
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
	
	private void displayVtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.vt_result);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.show();
	}
	
	private void displaySvtResult() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		String message;
		message = getString(R.string.svt_result);
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.wct_result_label));
		dialog.show();
	}
	
	private void displayMorphologyCriteria() {
		Dialog dialog = new Dialog(this);
		String message;
//		message = "Tachycardia with a RBBB-like QRS";
//		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.morphology_label));
		dialog.show();
	}
	
	private Button yesButton;
	private Button noButton;
	private Button backButton;
	private Button morphologyButton;
	private TextView stepTextView;
	private static int step = 1;


}
