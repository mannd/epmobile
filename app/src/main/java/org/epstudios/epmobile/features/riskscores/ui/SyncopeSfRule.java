package org.epstudios.epmobile.features.riskscores.ui;

import android.view.View;
import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.SyncopeRiskScore;

public class SyncopeSfRule extends SyncopeRiskScore {
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
                getString(R.string.syncope_sf_rule_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 1)
            message = getString(R.string.no_sf_rule_risk_message);
        else
            message = getString(R.string.high_sf_rule_risk_message);
        message = getRiskLabel() + " score "
                + (result > 0 ? "\u2265 1" : "= 0") + "\n" + message;
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

        checkBoxes[5].setVisibility(View.GONE);

        checkBoxes[0].setText(getString(R.string.abnormal_ecg_label));
        checkBoxes[1].setText(getString(R.string.chf_label));
        checkBoxes[2].setText(getString(R.string.sob_label));
        checkBoxes[3].setText(getString(R.string.low_hct_label));
        checkBoxes[4].setText(getString(R.string.low_bp_label));
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.syncope_sf_rule_full_reference,
                R.string.syncope_sf_rule_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.syncope_sf_rule_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.syncope_sf_rule_full_reference,
                R.string.syncope_sf_rule_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.syncope_sf_rule_title,
                R.string.syncope_sf_rule_instructions);
    }
}
