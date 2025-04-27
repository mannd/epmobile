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

public class Chads extends RiskScore {
    @Override
    protected void setContentView() {
        setContentView(R.layout.chads);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.chads_root_view);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.chads_label);
    }

    @Override
    protected String getFullReference() {
        String ref1 = convertReferenceToText(R.string.chads_original_reference,
                R.string.chads_original_link);
        String ref2 = convertReferenceToText(R.string.chads_chadsvasc_full_reference,
                R.string.chads_chadsvasc_link);
        return ref1 + "\n" + ref2;
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[5];

        checkBoxes[0] = findViewById(R.id.chf);
        checkBoxes[1] = findViewById(R.id.hypertension);
        checkBoxes[2] = findViewById(R.id.age75);
        checkBoxes[3] = findViewById(R.id.diabetes);
        checkBoxes[4] = findViewById(R.id.stroke);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                if (i == 4) // stroke = 2 points
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result), getString(R.string.chads_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 1)
            message = getString(R.string.low_chads_message);
        else if (result == 1)
            message = getString(R.string.medium_chads_message);
        else
            message = getString(R.string.high_chads_message);
        message += "\n" + getString(R.string.chads_proviso_message);
        String risk = "";
        String neuroRisk = "";
        switch (result) {
            case 0:
                risk = "0.6";
                neuroRisk = "0.9";
                break;
            case 1:
                risk = "3.0";
                neuroRisk = "4.3";
                break;
            case 2:
                risk = "4.2";
                neuroRisk = "6.1";
                break;
            case 3:
                risk = "7.1";
                neuroRisk = "9.9";
                break;
            case 4:
                risk = "11.1";
                neuroRisk = "14.9";
                break;
            case 5:
                risk = "12.5";
                neuroRisk = "16.7";
                break;
            case 6:
                risk = "13.0";
                neuroRisk = "17.2";
                break;
        }
        risk = "Annual stroke risk is " + risk + "%";
        neuroRisk = "Annual stroke/TIA/peripheral emboli risk is " + neuroRisk + "%";
        message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
                + risk + "\n" + neuroRisk;
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[2];
        references[0] = new Reference(this, R.string.chads_original_reference,
                R.string.chads_original_link);
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
        showAlertDialog(R.string.chads_title,
                R.string.chads_chadsvasc_stroke_instructions);
    }

}
