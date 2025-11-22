package org.epstudios.epmobile.features.riskscores.ui;

import android.view.View;
import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.SyncopeRiskScore;

public class MartinAlgorithm extends SyncopeRiskScore {
    final int[] risk = new int[]{0, 5, 16, 27, 27};

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
                getString(R.string.syncope_martin_title));
    }

    private String getResultMessage(int result) {
        String message;

        message = getRiskLabel() + " score = " + result + "\n"
                + getString(R.string.syncope_martin_result_label) + " = "
                + risk[result] + "%";
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
        checkBoxes[1].setText(getString(R.string.history_vt_label));
        checkBoxes[2].setText(getString(R.string.history_chf_label));
        checkBoxes[3].setText(getString(R.string.age_over_45_label));
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.syncope_martin_full_reference,
                R.string.syncope_martin_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.syncope_martin_title);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.syncope_martin_full_reference,
                R.string.syncope_martin_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.syncope_martin_title,
                R.string.syncope_martin_instructions);
    }
}
