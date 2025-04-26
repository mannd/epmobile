package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.RadioGroup;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 11/29/15.
 * <p/>
 * This file is part of epmobile.
 * <p/>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class AtriaStroke extends RiskScore {

    private RadioGroup radioGroup;

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.atria_stroke_full_reference,
                R.string.atria_stroke_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.atria_stroke_risk_label);
    }

    @Override
    protected void calculateResult() {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        if (radioButtonId == -1) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage(getString(R.string.no_age_checked_message));
            dialog.setTitle(getString(R.string.error_dialog_title));
            dialog.show();
            return;
        }
        // calculate
        int result = 0;
        clearSelectedRisks();
        for (CheckBox aCheckBox : checkBoxes) {
            if (aCheckBox.isChecked()) {
                addSelectedRisk(aCheckBox.getText().toString());
                result++;
            }
        }
        // checkBox[6] = prior stroke hx
        boolean hasStrokeHx = checkBoxes[6].isChecked();
        if (hasStrokeHx) {
            result--;
        }
        if (radioButtonId == R.id.age85) {
            result += (hasStrokeHx ? 9 : 6);
            addSelectedRisk(getString(R.string.atria_stroke_age_85));
        } else if (radioButtonId == R.id.age75) {
            result += (hasStrokeHx ? 7 : 5);
            addSelectedRisk(getString(R.string.atria_stroke_age_75));
        } else if (radioButtonId == R.id.age65) {
            result += (hasStrokeHx ? 7 : 3);
            addSelectedRisk(getString(R.string.atria_stroke_age_65));
        } else if (radioButtonId == R.id.agelessthan65) {
            result += hasStrokeHx ? 8 : 0;
            // Don't put in selectedRisks if risk is 0.
            if (hasStrokeHx) {
                addSelectedRisk(getString(R.string.atria_stroke_age_less_than_65));
            }
        }
        displayResult(getResultMessage(result), getString(R.string.atria_stroke_score_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 6)
            message = getString(R.string.low_atria_stroke_message);
        else if (result == 6)
            message = getString(R.string.medium_atria_stroke_message);
        else
            message = getString(R.string.high_atria_stroke_message);
        message = getRiskLabel() + " score = " + result + "\n" + message;
        setResultMessage(message);
        return resultWithShortReference();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.atriastroke);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.atria_stroke_risk_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[7];

        checkBoxes[0] = findViewById(R.id.sex);
        checkBoxes[1] = findViewById(R.id.diabetes);
        checkBoxes[2] = findViewById(R.id.chf);
        checkBoxes[3] = findViewById(R.id.htn);
        checkBoxes[4] = findViewById(R.id.proteinuria);
        checkBoxes[5] = findViewById(R.id.renal_disease);
        checkBoxes[6] = findViewById(R.id.stroke);
        radioGroup = findViewById(R.id.age);
    }

    @Override
    protected void clearEntries() {
        super.clearEntries();
        radioGroup.clearCheck();
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.atria_stroke_full_reference,
                R.string.atria_stroke_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected  void showActivityInstructions() {
        showAlertDialog(R.string.atria_stroke_score_title,
                R.string.atria_stroke_instructions);
    }
}
