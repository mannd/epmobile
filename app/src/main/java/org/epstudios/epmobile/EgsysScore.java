package org.epstudios.epmobile;

import android.widget.CheckBox;

public class EgsysScore extends SyncopeRiskScore {
    // These scores are from the validation group in the paper.
    // The derivation cohort had slightly different scores.
    final int[] points = new int[]{4, 3, 3, 2, -1, -1};

    @Override
    protected void setContentView() {
        setContentView(R.layout.simplerisk);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.simple_risk_root_view);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                result += points[i];
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.syncope_egsys_score_title));
    }

    private String getResultMessage(int result) {
        String message;
        int mortalityRisk;
        int syncopeRisk = 0;
        if (result < 3) {
            mortalityRisk = 2;
            syncopeRisk = 2;
        } else
            mortalityRisk = 21;
        if (result == 3)
            syncopeRisk = 13;
        if (result == 4)
            syncopeRisk = 33;
        if (result > 4)
            syncopeRisk = 77;

        message = getRiskLabel() + " score = " + result + "\n"
                + "2-year total mortality = " + mortalityRisk
                + "%\nCardiac syncope probability = " + syncopeRisk + "%";
        setResultMessage(message);
        return resultWithShortReference();

    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[6];

        checkBoxes[0] = findViewById(R.id.risk_one);
        checkBoxes[1] = findViewById(R.id.risk_two);
        checkBoxes[2] = findViewById(R.id.risk_three);
        checkBoxes[3] = findViewById(R.id.risk_four);
        checkBoxes[4] = findViewById(R.id.risk_five);
        checkBoxes[5] = findViewById(R.id.risk_six);

        checkBoxes[0].setText(getString(R.string.palps_before_syncope_label));
        checkBoxes[1]
                .setText(getString(R.string.abnormal_ecg_or_heart_disease_label));
        checkBoxes[2].setText(getString(R.string.syncope_during_effort_label));
        checkBoxes[3].setText(getString(R.string.syncope_while_supine_label));
        checkBoxes[4].setText(getString(R.string.autonomic_prodrome_label));
        checkBoxes[5].setText(getString(R.string.predisposing_factors_label));
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.syncope_egsys_full_reference,
                R.string.syncope_egsys_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.syncope_egsys_label);
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.syncope_egsys_full_reference,
                R.string.syncope_egsys_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.syncope_egsys_score_title,
                R.string.syncope_egsys_instructions);
    }
}
