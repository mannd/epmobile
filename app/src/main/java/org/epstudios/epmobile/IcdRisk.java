package org.epstudios.epmobile;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class IcdRisk extends DiagnosticScore {
    CheckBox sexCheckBox;
    RadioGroup admittedRadioGroup;
    RadioGroup nyhaClassRadioGroup;
    CheckBox noPriorCabgCheckBox;
    CheckBox currentDialysisCheckBox;
    CheckBox chronicLungDiseaseCheckBox;
    RadioGroup abnormalConductionRadioGroup;
    RadioGroup procedureTypeRadioGroup;
    RadioGroup icdTypeRadioGroup;
    RadioGroup sodiumRadioGroup;
    RadioGroup hgbRadioGroup;
    RadioGroup bunRadioGroup;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, RiskScoreList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.icdrisk);
    }

    @Override
    protected void calculateResult() {
        String message;
        int score;
        if (radioGroupsIncomplete()) {
            message = getString(R.string.incomplete_radio_groups_error);
            String title = getString(R.string.error_dialog_title);
            displayResult(message, title);
            return;
        }
        score = calculateScore();
        message = "Risk score = " + score;
        if (score <= 10) {
            message += "\nVery low risk of complications (0.3%)";
        } else if (score >= 30) {
            message += "\nHigh risk of complications (4.2%)";
        } else {
            message += "\nIntermediate risk of complications (between 0.3% and 4.2%)";
        }
        message += "\n" + getString(R.string.icd_risk_reference);
        displayResult(message, getString(R.string.icd_result_title));

    }

    private int calculateScore() {
        int score = 0;
        if (sexCheckBox.isChecked()) {
            score += 2;
        }
        if (admittedRadioGroup.getCheckedRadioButtonId() == R.id.for_heart_failure) {
            score += 4;
        }
        if (admittedRadioGroup.getCheckedRadioButtonId() == R.id.for_other_reason) {
            score += 5;
        }
        if (nyhaClassRadioGroup.getCheckedRadioButtonId() == R.id.class_three) {
            score += 3;
        }
        if (nyhaClassRadioGroup.getCheckedRadioButtonId() == R.id.class_four) {
            score += 7;
        }
        if (noPriorCabgCheckBox.isChecked()) {
            score += 2;
        }
        if (currentDialysisCheckBox.isChecked()) {
            score += 3;
        }
        if (chronicLungDiseaseCheckBox.isChecked()) {
            score += 2;
        }
        if (abnormalConductionRadioGroup.getCheckedRadioButtonId() == R.id.yes_lbbb
                || abnormalConductionRadioGroup.getCheckedRadioButtonId() == R.id.yes_other) {
            score += 2;
        }
        int procedureType = procedureTypeRadioGroup.getCheckedRadioButtonId();
        if (procedureType == R.id.initial_implant) {
            score += 13;
        } else if (procedureType == R.id.generator_replacement_infection) {
            score += 17;
        } else if (procedureType == R.id.generator_replacement_device_relocation) {
            score += 18;
        } else if (procedureType == R.id.generator_replacement_upgrade) {
            score += 12;
        } else if (procedureType == R.id.generator_replacement_malfunction) {
            score += 13;
        } else if (procedureType == R.id.generator_replacement_other) {
            score += 14;
        }
        if (icdTypeRadioGroup.getCheckedRadioButtonId() == R.id.dual_chamber) {
            score += 4;
        }
        if (icdTypeRadioGroup.getCheckedRadioButtonId() == R.id.crt_d) {
            score += 6;
        }
        if (sodiumRadioGroup.getCheckedRadioButtonId() == R.id.low_sodium) {
            score += 3;
        }
        if (sodiumRadioGroup.getCheckedRadioButtonId() == R.id.high_sodium) {
            score += 2;
        }
        if (hgbRadioGroup.getCheckedRadioButtonId() == R.id.low_hgb) {
            score += 3;
        }
        if (hgbRadioGroup.getCheckedRadioButtonId() == R.id.normal_hgb) {
            score += 2;
        }
        if (bunRadioGroup.getCheckedRadioButtonId() == R.id.normal_bun) {
            score += 2;
        }
        if (bunRadioGroup.getCheckedRadioButtonId() == R.id.high_bun) {
            score += 4;
        }

        return score;
    }

    @Override
    protected void init() {
        sexCheckBox = findViewById(R.id.female_sex);
        admittedRadioGroup = findViewById(R.id.admission_reason_radio_group);
        nyhaClassRadioGroup = findViewById(R.id.nyha_class_radio_group);
        noPriorCabgCheckBox = findViewById(R.id.no_prior_cabg);
        currentDialysisCheckBox = findViewById(R.id.current_dialysis);
        chronicLungDiseaseCheckBox = findViewById(R.id.chronic_lung_disease);
        abnormalConductionRadioGroup = findViewById(R.id.abnormal_conduction_radio_group);
        procedureTypeRadioGroup = findViewById(R.id.procedure_type_radio_group);
        icdTypeRadioGroup = findViewById(R.id.icd_type_radio_group);
        sodiumRadioGroup = findViewById(R.id.sodium_radio_group);
        hgbRadioGroup = findViewById(R.id.hgb_radio_group);
        bunRadioGroup = findViewById(R.id.bun_radio_group);

    }

    @Override
    protected void clearEntries() {
        sexCheckBox.setChecked(false);
        admittedRadioGroup.clearCheck();
        nyhaClassRadioGroup.clearCheck();
        noPriorCabgCheckBox.setChecked(false);
        currentDialysisCheckBox.setChecked(false);
        chronicLungDiseaseCheckBox.setChecked(false);
        abnormalConductionRadioGroup.clearCheck();
        procedureTypeRadioGroup.clearCheck();
        icdTypeRadioGroup.clearCheck();
        sodiumRadioGroup.clearCheck();
        hgbRadioGroup.clearCheck();
        bunRadioGroup.clearCheck();

    }

    private boolean radioGroupsIncomplete() {
        return admittedRadioGroup.getCheckedRadioButtonId() == -1
                || nyhaClassRadioGroup.getCheckedRadioButtonId() == -1
                || abnormalConductionRadioGroup.getCheckedRadioButtonId() == -1
                || procedureTypeRadioGroup.getCheckedRadioButtonId() == -1
                || icdTypeRadioGroup.getCheckedRadioButtonId() == -1
                || sodiumRadioGroup.getCheckedRadioButtonId() == -1
                || hgbRadioGroup.getCheckedRadioButtonId() == -1
                || bunRadioGroup.getCheckedRadioButtonId() == -1;
    }

}
