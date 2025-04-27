package org.epstudios.epmobile;

import android.view.View;
import android.widget.CheckBox;

public class OesilScore extends SyncopeRiskScore {
    // These scores are from the validation group in the paper.
    // The derivation cohort had slightly different scores.
    final String[] risk = new String[]{"0", "0.6", "14", "29", "52.9"};

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
        for (CheckBox aCheckBox : checkBoxes) {
            if (aCheckBox.isChecked()) {
                addSelectedRisk(aCheckBox.getText().toString());
                result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.syncope_oesil_score_title));
    }

    private String getResultMessage(int result) {
        String message;

        message = getRiskLabel() + " score = " + result + "\n"
                + "1-year total mortality = " + risk[result] + "%";
        resultMessage = message;
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

        checkBoxes[4].setVisibility(View.GONE);
        checkBoxes[5].setVisibility(View.GONE);

        checkBoxes[0].setText(getString(R.string.abnormal_ecg_label));
        checkBoxes[1].setText(getString(R.string.history_cv_disease_label));
        checkBoxes[2].setText(getString(R.string.lack_of_prodrome_label));
        checkBoxes[3].setText(getString(R.string.age_over_65_label));
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.syncope_oesil_full_reference,
                R.string.syncope_oesil_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.syncope_oesil_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.syncope_oesil_full_reference,
                R.string.syncope_oesil_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.syncope_oesil_score_title,
                R.string.syncope_oesil_instructions);
    }
}
