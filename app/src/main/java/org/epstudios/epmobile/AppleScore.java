package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 9/17/22.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <<a href="http://www.gnu.org/licenses/">...</a>>.
 */
public class AppleScore extends RiskScore {
    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
                result++;
            }
        }
        displayResult(getResultMessage(result), getString(R.string.apple_score_title));
    }

    private String getResultMessage(int result) {
        String message = "";
        int recurrenceRate = 0;
        int repeatRecurrenceRate = 0;  // recurrence rate after repeat ablation
        switch (result) {
            case 0:
                recurrenceRate = 46;
                repeatRecurrenceRate = 18;
                break;
            case 1:
                recurrenceRate = 57;
                repeatRecurrenceRate = 38;
                break;
            case 2:
                recurrenceRate = 76;
                repeatRecurrenceRate = 39;
                break;
            case 3:
            case 4:
            case 5:
                recurrenceRate = 72;
                repeatRecurrenceRate = 56;
                break;
            default:
                recurrenceRate = 0;
                repeatRecurrenceRate = 0;
                break;
        }
        if (recurrenceRate == 0) {
            message = "Error";
        } else {
            message = "Risk of AF recurrence following initial catheter ablation = "
                    + recurrenceRate + "%.\nRisk of AF recurrence following repeat catheter ablation = "
                    + repeatRecurrenceRate + "%.";
        }
        resultMessage = message;
        return message;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.apple);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.apple_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[5];

        checkBoxes[0] = findViewById(R.id.age);
        checkBoxes[1] = findViewById(R.id.persistentAF);
        checkBoxes[2] = findViewById(R.id.impairedGfr);
        checkBoxes[3] = findViewById(R.id.laEnlargement);
        checkBoxes[4] = findViewById(R.id.lowEF);
    }

    @Override
    protected String getFullReference() {
        String ref1 = convertReferenceToText(R.string.apple_reference_1,
                R.string.apple_link_1);
        String ref2 = convertReferenceToText(R.string.apple_reference_2,
                R.string.apple_link_2);
        return ref1 + "\n" + ref2;
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.apple_score_title);
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[2];
        references[0] = new Reference(this, R.string.apple_reference_1,
                R.string.apple_link_1);
        references[1] = new Reference(this, R.string.apple_reference_2,
                R.string.apple_link_2);
        showReferenceAlertDialog(references);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.apple_score_title,
                R.string.apple_instructions);
    }

}
