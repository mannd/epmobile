package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 12/2/15.
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
public class SameTtr extends RiskScore {

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.same_full_reference,
                R.string.same_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.same_risk_label);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                if (i == 4 || i == 5)
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.same_tt2r2_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result <= 2)
            message = getString(R.string.low_same_risk_message);
        else
            message = getString(R.string.high_same_risk_message);
        message = getRiskLabel() + " score = " + result + "\n" + message;
        resultMessage = message;
        return resultWithShortReference();
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.samett2r2);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.same_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[6];

        checkBoxes[0] = findViewById(R.id.sex);
        checkBoxes[1] = findViewById(R.id.age);
        checkBoxes[2] = findViewById(R.id.medhx);
        checkBoxes[3] = findViewById(R.id.treatment);
        checkBoxes[4] = findViewById(R.id.smoking);
        checkBoxes[5] = findViewById(R.id.race);
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.same_full_reference,
                R.string.same_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.same_tt2r2_title,
                R.string.same_instructions);
    }
}
