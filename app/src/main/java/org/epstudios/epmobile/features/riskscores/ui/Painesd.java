package org.epstudios.epmobile.features.riskscores.ui;

import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.RiskScore;
import org.jetbrains.annotations.Nullable;

/**
 * Copyright (C) 2025 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 10/16/25.
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
public class Painesd extends RiskScore {
    @Override
    @Nullable
    protected String getFullReference() {
        return convertReferenceToText(R.string.painesd_reference,
                R.string.painesd_link);
    }

    @Override
    @Nullable
    protected String getRiskLabel() {
        return getString(R.string.painesd_label);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.painesd_risk_title,
                R.string.painesd_instructions);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return true;
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        // NB: This is about the most inefficient way to code this.
        // Ideally the whole risk score system could be rewritten to
        // simplify the code and simplify adding risk scores, but for
        // now this will have to do.
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                addSelectedRisk(checkBoxes[i].getText().toString());
            }
        }
        if (checkBoxes[0].isChecked()) { // COPD
            result += 5;
        }
        if (checkBoxes[1].isChecked()) { // Age
            result += 3;
        }
        if (checkBoxes[2].isChecked()) { // Ischemic CM
            result += 6;
        }
        if (checkBoxes[3].isChecked()) { // Low NYHA Class
            result += 6;
        }
        if (checkBoxes[4].isChecked()) { // Low EF
            result += 3;
        }
        if (checkBoxes[5].isChecked()) { // Storm
            result += 5;
        }
        if (checkBoxes[6].isChecked()) { // Diabetes
            result += 3;
        }
        displayResult(getResultMessage(result), getString(R.string.painesd_risk_title));
    }


    private String getResultMessage(int result) {
        String message = getString(R.string.painesd_score_result_title, result) + "\n\n";
        String arg;
        String riskLevel;
        if (result < 9) {
            arg = getString(R.string.ahd_low_risk);
            riskLevel = "low";
        } else if (result < 15) {
            arg = getString(R.string.ahd_moderate_risk);
            riskLevel = "moderate";
        } else {
            arg = getString(R.string.ahd_high_risk);
            riskLevel = "high";
        }
        message += getString(R.string.ahd_risk_message, arg);
        message += "\n\n";
        message += getString(R.string.ahd_risk_level, riskLevel);
        return message;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.painesd_reference,
                R.string.painesd_link);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.painesd);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.painesd_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[7];

        checkBoxes[0] = findViewById(R.id.copd);
        checkBoxes[1] = findViewById(R.id.age);
        checkBoxes[2] = findViewById(R.id.ischemic_cm);
        checkBoxes[3] = findViewById(R.id.low_nyha_class);
        checkBoxes[4] = findViewById(R.id.low_ef);
        checkBoxes[5] = findViewById(R.id.storm);
        checkBoxes[6] = findViewById(R.id.diabetes);
    }
}
