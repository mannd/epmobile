package org.epstudios.epmobile;

import java.text.DecimalFormat;
import java.text.Format;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class LongQt extends EpActivity implements OnClickListener {
	private RadioGroup qtcRadioGroup;
	private CheckBox torsadeCheckBox;
	private CheckBox tWaveAlternansCheckBox;
	private CheckBox notchedTWaveCheckBox;
	private CheckBox lowHrCheckBox;
	private RadioGroup syncopeRadioGroup;
	private CheckBox congenitalDeafnessCheckBox;
	private CheckBox familyHxLqtCheckBox;
	private CheckBox familyHxScdCheckBox;
	private CheckBox longQtPostExerciseCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.longqt);
        super.onCreate(savedInstanceState);

		View calculateButton = findViewById(R.id.calculate_button);
		calculateButton.setOnClickListener(this);
		View clearButton = findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

		qtcRadioGroup = (RadioGroup) findViewById(R.id.qtc_radio_group);
		torsadeCheckBox = (CheckBox) findViewById(R.id.torsade);
		tWaveAlternansCheckBox = (CheckBox) findViewById(R.id.t_wave_alternans);
		notchedTWaveCheckBox = (CheckBox) findViewById(R.id.notched_t_wave);
		lowHrCheckBox = (CheckBox) findViewById(R.id.low_hr);
		syncopeRadioGroup = (RadioGroup) findViewById(R.id.syncope_radio_group);
		congenitalDeafnessCheckBox = (CheckBox) findViewById(R.id.congenital_deafness);
		familyHxLqtCheckBox = (CheckBox) findViewById(R.id.family_hx_lqt);
		familyHxScdCheckBox = (CheckBox) findViewById(R.id.family_hx_scd);
		longQtPostExerciseCheckBox = (CheckBox) findViewById(R.id.long_qt_post_exercise);

		clearEntries();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, LongQtList.class);
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
		switch (v.getId()) {
		case R.id.calculate_button:
			calculateResult();
			break;
		case R.id.clear_button:
			clearEntries();
			break;
		}
	}

	private void calculateResult() {
		// since this score uses 0.5, we will multiply points by 10, e.g.
		// 1 = 10, to avoid using non-integer arithmetic
		int score = 0;
		if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.long_qt)
			score += 10;
		else if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.longer_qt)
			score += 20;
		else if (qtcRadioGroup.getCheckedRadioButtonId() == R.id.longest_qt)
			score += 30;
		boolean hasTorsade = false;
		if (torsadeCheckBox.isChecked()) {
			score += 20;
			hasTorsade = true;
		}
		if (longQtPostExerciseCheckBox.isChecked())
			score += 10;
		if (tWaveAlternansCheckBox.isChecked())
			score += 10;
		if (notchedTWaveCheckBox.isChecked())
			score += 10;
		if (lowHrCheckBox.isChecked())
			score += 5;
		// Torsade and syncope are mutually exclusive, so don't count syncope
		// if has torsade.
		if (!hasTorsade) {
			if (syncopeRadioGroup.getCheckedRadioButtonId() == R.id.syncope_with_stress)
				score += 20;
			else if (syncopeRadioGroup.getCheckedRadioButtonId() == R.id.syncope_without_stress)
				score += 10;
		}
		if (congenitalDeafnessCheckBox.isChecked())
			score += 5;
		if (familyHxLqtCheckBox.isChecked())
			score += 10;
		if (familyHxScdCheckBox.isChecked())
			score += 5;

		displayResult(score);
	}

	private void displayResult(int score) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		double displayScore = score / 10.0;
		Format formatter = new DecimalFormat("0.#");
		String message = "Score = " + formatter.format(displayScore) + "\n";
		if (score >= 35)
			message += "High probability of ";
		else if (score >= 15)
			message += "Intermediate probability of ";
		else
			message += "Low probability of ";
		message += "Long QT Syndrome";
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearEntries();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
				getString(R.string.dont_reset_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.setTitle(getString(R.string.long_qt_syndrome_diagnosis_title));

		dialog.show();
	}

	private void clearEntries() {
		qtcRadioGroup.clearCheck();
		torsadeCheckBox.setChecked(false);
		tWaveAlternansCheckBox.setChecked(false);
		notchedTWaveCheckBox.setChecked(false);
		lowHrCheckBox.setChecked(false);
		syncopeRadioGroup.clearCheck();
		congenitalDeafnessCheckBox.setChecked(false);
		familyHxLqtCheckBox.setChecked(false);
		familyHxScdCheckBox.setChecked(false);
		longQtPostExerciseCheckBox.setChecked(false);
	}

}
