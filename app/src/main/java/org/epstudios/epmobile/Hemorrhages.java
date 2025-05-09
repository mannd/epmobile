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

@SuppressWarnings("SpellCheckingInspection")
public class Hemorrhages extends RiskScore {
    final static private int REBLEED = 5; // this is the only risk worth 2
    // points

    @Override
    protected void setContentView() {
        setContentView(R.layout.hemorrhages);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.hemorrhages_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[11];

        checkBoxes[0] = findViewById(R.id.hepatic_or_renal_disease);
        checkBoxes[1] = findViewById(R.id.etoh);
        checkBoxes[2] = findViewById(R.id.malignancy);
        checkBoxes[3] = findViewById(R.id.older);
        checkBoxes[4] = findViewById(R.id.platelet);
        checkBoxes[REBLEED] = findViewById(R.id.rebleeding);
        checkBoxes[6] = findViewById(R.id.htn_uncontrolled);
        checkBoxes[7] = findViewById(R.id.anemia);
        checkBoxes[8] = findViewById(R.id.genetic_factors);
        checkBoxes[9] = findViewById(R.id.fall_risk);
        checkBoxes[10] = findViewById(R.id.stroke);
    }

    @Override
    protected void calculateResult() {
        clearSelectedRisks();
        int result = 0;
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                if (i == REBLEED)
                    result += 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.hemorrhages_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 2)
            message = getString(R.string.low_risk_hemorrhages);
        else if (result < 4)
            message = getString(R.string.intermediate_risk_hemorrhages);
        else
            // result >= 4
            message = getString(R.string.high_risk_hemorrhages);
        String risk = "";
        switch (result) {
            case 0:
                risk = "1.9";
                break;
            case 1:
                risk = "2.5";
                break;
            case 2:
                risk = "5.3";
                break;
            case 3:
                risk = "8.4";
                break;
            case 4:
                risk = "10.4";
                break;
        }
        if (result >= 5)
            risk = "12.3";
        risk = "Bleeding risk is " + risk + " bleeds per 100 patient-years.";
        message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
                + risk;
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.hemorrhages_full_reference,
                R.string.hemorrhages_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hemorrhages_label);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.hemorrhages_full_reference,
                R.string.hemorrhages_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected  void showActivityInstructions() {
        showAlertDialog(R.string.hemorrhages_title,
                R.string.hemorrhages_instructions);
    }

}
