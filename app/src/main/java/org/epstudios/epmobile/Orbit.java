package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2016 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/14/16.
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
public class Orbit extends RiskScore {

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.orbit_full_reference,
                R.string.orbit_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.orbit_label);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                if (i == 1 || i == 2) // anemia and bleeding are 2 points
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result), getString(R.string.orbit_risk_title));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.orbit);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.orbit_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[5];

        checkBoxes[0] = findViewById(R.id.age75);
        checkBoxes[1] = findViewById(R.id.anemia);
        checkBoxes[2] = findViewById(R.id.bleeding);
        checkBoxes[3] = findViewById(R.id.renal_insufficiency);
        checkBoxes[4] = findViewById(R.id.antiplatelet_therapy);
    }

    private String getResultMessage(int result) {
        String message;
        if (result < 1)
            message = getString(R.string.low_orbit_message);
        else if (result == 1)
            message = getString(R.string.medium_orbit_message);
        else
            message = getString(R.string.high_orbit_message);
        String risk;
        switch (result) {
            case 0:
            case 1:
            case 2:
                risk = "2.4";
                break;
            case 3:
                //noinspection DuplicateBranchesInSwitch
                risk = "4.7";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                // 7 should be highest score possible
            default:
                risk = "4.7";
                break;
        }
        risk = "Bleeding risk is " + risk + " bleeds per 100 patient-years.";
        message = getRiskLabel() + " score = " + result + "\n" + message + "\n"
                + risk;
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.orbit_full_reference,
                R.string.orbit_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected  void showActivityInstructions() {
        showAlertDialog(R.string.orbit_risk_title,
                R.string.hemorrhages_instructions); // Use HEMORRHAGES instructions, same type score.
    }

}
