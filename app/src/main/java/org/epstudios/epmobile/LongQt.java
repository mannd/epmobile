package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DecimalFormat;
import java.text.Format;

public class LongQt extends RiskScore implements OnClickListener {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    protected void calculateResult() {
        // since this score uses 0.5, we will multiply points by 10, e.g.
        // 1 = 10, to avoid using non-integer arithmetic
        int score = 0;
        int selectedQTcId = qtcRadioGroup.getCheckedRadioButtonId();
        if (selectedQTcId == R.id.long_qt) {
            score += 10;
        }
        else if (selectedQTcId == R.id.longer_qt)
            score += 20;
        else if (selectedQTcId == R.id.longest_qt)
            score += 30;
        // get selected radio button from radioGroup
        RadioButton qtcRadioButton = (RadioButton) findViewById(selectedQTcId);
        if (qtcRadioButton != null) {
            addSelectedRisk(qtcRadioButton.getText().toString());
        }
        boolean hasTorsade = false;
        if (torsadeCheckBox.isChecked()) {
            score += 20;
            hasTorsade = true;
            addSelectedRisk(torsadeCheckBox.getText().toString());
        }
        if (longQtPostExerciseCheckBox.isChecked()) {
            score += 10;
            addSelectedRisk(longQtPostExerciseCheckBox.getText().toString());
        }
        if (tWaveAlternansCheckBox.isChecked()) {
            score += 10;
            addSelectedRisk(tWaveAlternansCheckBox.getText().toString());
        }
        if (notchedTWaveCheckBox.isChecked()) {
            score += 10;
            addSelectedRisk(notchedTWaveCheckBox.getText().toString());
        }
        if (lowHrCheckBox.isChecked()) {
            score += 5;
            addSelectedRisk((lowHrCheckBox.getText().toString()));
        }
        // Torsade and syncope are mutually exclusive, so don't count syncope
        // if has torsade.
        if (!hasTorsade) {
            int selectedSyncopeId = syncopeRadioGroup.getCheckedRadioButtonId();
            if (selectedSyncopeId == R.id.syncope_with_stress)
                score += 20;
            else if (selectedSyncopeId == R.id.syncope_without_stress)
                score += 10;
            RadioButton syncopeRadioButton = (RadioButton) findViewById(selectedSyncopeId);
            if (syncopeRadioButton != null) {
                addSelectedRisk(syncopeRadioButton.getText().toString());
            }
        }
        if (congenitalDeafnessCheckBox.isChecked()) {
            score += 5;
            addSelectedRisk(congenitalDeafnessCheckBox.getText().toString());
        }
        if (familyHxLqtCheckBox.isChecked()) {
            score += 10;
            addSelectedRisk(familyHxLqtCheckBox.getText().toString());
        }
        if (familyHxScdCheckBox.isChecked()) {
            score += 5;
            addSelectedRisk(familyHxScdCheckBox.getText().toString());
        }
        displayResult(score);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.longqt);
    }

    @Override
    protected void init() {

        qtcRadioGroup = findViewById(R.id.qtc_radio_group);
        torsadeCheckBox = findViewById(R.id.torsade);
        tWaveAlternansCheckBox = findViewById(R.id.t_wave_alternans);
        notchedTWaveCheckBox = findViewById(R.id.notched_t_wave);
        lowHrCheckBox = findViewById(R.id.low_hr);
        syncopeRadioGroup = findViewById(R.id.syncope_radio_group);
        congenitalDeafnessCheckBox = findViewById(R.id.congenital_deafness);
        familyHxLqtCheckBox = findViewById(R.id.family_hx_lqt);
        familyHxScdCheckBox = findViewById(R.id.family_hx_scd);
        longQtPostExerciseCheckBox = findViewById(R.id.long_qt_post_exercise);

        clearEntries();
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
        setResultMessage(message);
        super.displayResult(message, getString(R.string.long_qt_syndrome_diagnosis_title));
    }

    protected void clearEntries() {
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

    @Override
    protected String getFullReference() {
        String fullReference = convertReferenceToText(R.string.lqts_diagnosis_reference,
                R.string.long_qt_drugs_link);
        return fullReference;
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.long_qt_syndrome_diagnosis_title);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.lqts_diagnosis_reference,
                R.string.lqts_diagnosis_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.long_qt_syndrome_diagnosis_title,
                R.string.lqts_diagnosis_instructions);
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return false;
    }

    @Override
    protected void showActivityKey() {
        showKeyAlertDialog(R.string.lqts_key);
    }



}
