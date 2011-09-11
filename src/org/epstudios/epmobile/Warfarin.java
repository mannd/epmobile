package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Warfarin extends EpActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warfarin);
		
		View calculateDoseButton = findViewById(R.id.calculate_dose_button);
		calculateDoseButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		
		tabletRadioGroup = (RadioGroup) findViewById(R.id.tabletRadioGroup);
		inrTargetRadioGroup = (RadioGroup) findViewById(R.id.inrTargetRadioGroup);
		inrEditText = (EditText) findViewById(R.id.inrEditText);
		weeklyDoseEditText = (EditText) findViewById(R.id.weeklyDoseEditText);
		
		clearEntries();
	}
	
	private RadioGroup tabletRadioGroup;
	private RadioGroup inrTargetRadioGroup;
	private EditText inrEditText;
	private EditText weeklyDoseEditText;
	private String defaultWarfarinTablet;
	private String defaultInrTarget;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.calculate_dose_button:
			calculateResult();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}
	
	private void calculateResult() {
		String message;
		try {
			double inr = Double.parseDouble(inrEditText.getText().toString());
			if (inr >= 6.0)
				message = "Hold warfarin until INR back in therapeutic range.";
			else
				message = "Does not compute.";
			displayResult(message);
		}
		catch (NumberFormatException e) {
			message = "Invalid Entry";
			displayResult(message);
		}
	}
	

	private void displayResult(String message) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Don't Reset",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				});
		dialog.show();
	}
	
	private void clearEntries() {
		inrEditText.setText(null);
		weeklyDoseEditText.setText(null);
		getPrefs();
		int defaultId = Integer.parseInt(defaultWarfarinTablet);
		int id = 2;  // 5 mg default default
		switch (defaultId) {
		case 0:
			id = R.id.tablet1;
			break;
		case 1:
			id = R.id.tablet2;
			break;
		case 2:
			id = R.id.tablet3;
			break;
		case 3:
			id = R.id.tablet4;
			break;
		}
		tabletRadioGroup.check(id);
		defaultId = Integer.parseInt(defaultInrTarget);
		id = 0;
		switch (defaultId) {
		case 0:
			id = R.id.inrTarget1;
			break;
		case 1:
			id = R.id.inrTarget2;
			break;
		}
		inrTargetRadioGroup.check(id);
	}
	
	private void getPrefs() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		defaultWarfarinTablet = prefs.getString("default_warfarin_tablet", "2");	// 2 = 5 mg dose
		defaultInrTarget = prefs.getString("default_inr_target", "0"); 	// 0 = 2.0 - 3.0 target
	}

}
