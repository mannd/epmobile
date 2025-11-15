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

package org.epstudios.epmobile.features.riskscores.ui;

import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.Reference;
import org.epstudios.epmobile.RiskScore;

public class ChadsVasc extends RiskScore {
    private boolean isFemale;

    @Override
    protected void setContentView() {
        setContentView(R.layout.chadsvasc);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.chadsvasc_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[8];

        checkBoxes[0] = findViewById(R.id.chf);
        checkBoxes[1] = findViewById(R.id.hypertension);
        checkBoxes[2] = findViewById(R.id.age75);
        checkBoxes[3] = findViewById(R.id.diabetes);
        checkBoxes[4] = findViewById(R.id.stroke);
        checkBoxes[5] = findViewById(R.id.vascular);
        checkBoxes[6] = findViewById(R.id.age65);
        checkBoxes[7] = findViewById(R.id.female);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        isFemale = false;
        clearSelectedRisks();
        // correct checking both age checkboxes
        if (checkBoxes[2].isChecked() && checkBoxes[6].isChecked())
            checkBoxes[6].setChecked(false);
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                if (i == 7) {
                    isFemale = true;
                }
                if (i == 4 || i == 2) // stroke, age>75 = 2 points
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.chadsvasc_title));
    }

    //  https://www.mdcalc.com/cha2ds2-vasc-score-atrial-fibrillation-stroke-risk
    private String getResultMessage(int result) {
        String message;
        if (result < 1)
            message = getString(R.string.low_chadsvasc_message);
        else if (result == 1) {
            message = getString(R.string.medium_chadsvasc_message);
            if (isFemale) {
                message += " " + getString(R.string.female_only_chadsvasc_message);
            } else {
                message += " " + getString(R.string.non_female_chadsvasc_message);
            }
        } else
            message = getString(R.string.high_chadsvasc_message);
        String risk = "";
        String neuroRisk = "";
        switch (result) {
            case 0:
                risk = "0.2";
                neuroRisk = "0.3";
                break;
            case 1:
                risk = "0.6";
                neuroRisk = "0.9";
                break;
            case 2:
                risk = "2.2";
                neuroRisk = "2.9";
                break;
            case 3:
                risk = "3.2";
                neuroRisk = "4.6";
                break;
            case 4:
                risk = "4.8";
                neuroRisk = "6.7";
                break;
            case 5:
                risk = "7.2";
                neuroRisk = "10.0";
                break;
            case 6:
                risk = "9.7";
                neuroRisk = "13.6";
                break;
            case 7:
                risk = "11.2";
                neuroRisk = "15.7";
                break;
            case 8:
                risk = "10.8";
                neuroRisk = "15.2";
                break;
            case 9:
                risk = "12.23";
                neuroRisk = "17.4";
                break;
        }
        risk = "Annual ischemic stroke risk is " + risk + "%";
        neuroRisk = "Annual stroke/TIA/peripheral emboli risk is " + neuroRisk + "%";
        message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
                + risk + "\n" + neuroRisk;
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected String getFullReference() {
        String ref1 = convertReferenceToText(R.string.chadsvasc_original_reference,
                R.string.chadsvasc_original_link);
        String ref2 = convertReferenceToText(R.string.chads_chadsvasc_full_reference,
                R.string.chads_chadsvasc_link);
        return ref1 + "\n" + ref2;
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.chadsvasc_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[2];
        references[0] = new Reference(this, R.string.chadsvasc_original_reference,
                R.string.chadsvasc_original_link);
        references[1] = new Reference(this, R.string.chads_chadsvasc_full_reference,
                R.string.chads_chadsvasc_link);
        showReferenceAlertDialog(references);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected  void showActivityInstructions() {
        showAlertDialog(R.string.chadsvasc_title,
                R.string.chads_chadsvasc_stroke_instructions);
    }

}
