/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.widget.CheckBox;

public class HasBled extends RiskScore {
    @Override
    protected void setContentView() {
        setContentView(R.layout.hasbled);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.hasbled_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[9];

        checkBoxes[0] = findViewById(R.id.hypertension);
        checkBoxes[1] = findViewById(R.id.abnormal_renal_function);
        checkBoxes[2] = findViewById(R.id.abnormal_liver_function);
        checkBoxes[3] = findViewById(R.id.stroke);
        checkBoxes[4] = findViewById(R.id.bleeding);
        checkBoxes[5] = findViewById(R.id.labile_inr);
        checkBoxes[6] = findViewById(R.id.age65);
        checkBoxes[7] = findViewById(R.id.drug);
        checkBoxes[8] = findViewById(R.id.etoh);
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
                getString(R.string.hasbled_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 3)
            message = getString(R.string.normal_hasbled);
        else
            message = getString(R.string.abnormal_hasbled);
        String risk = "";
        switch (result) {
            case 0:
            case 1:
                risk = "1.02-1.13";
                break;
            case 2:
                risk = "1.88";
                break;
            case 3:
                risk = "3.74";
                break;
            case 4:
                risk = "8.70";
                break;
            case 5:
                risk = "12.50";
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                risk = "> 12.50";
                break;
        }
        risk = "Bleeding risk is " + risk + " bleeds per 100 patient-years.";
        message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
                + risk;
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.hasbled_full_reference,
                R.string.hasbled_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hasbled_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.hasbled_full_reference,
                R.string.hasbled_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected  void showActivityInstructions() {
        showAlertDialog(R.string.hasbled_title,
                R.string.hasbled_instructions);
    }

}
