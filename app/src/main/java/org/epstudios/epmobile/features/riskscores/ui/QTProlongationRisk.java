package org.epstudios.epmobile.features.riskscores.ui;

import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.RiskScore;

/**
 * Copyright (C) 2020 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2020-04-05.
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
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class QTProlongationRisk extends RiskScore {
    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.qt_prolongation_risk_full_reference,
                R.string.qt_prolongation_risk_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.qt_prolongation_risk_label);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        int[] points = {1, 1, 1, 2, 2, 2, 3, 3, 3, 6};
        for (int n = 0; n < 10; n++) {
            if (checkBoxes[n].isChecked()) {
                addSelectedRisk(checkBoxes[n].getText().toString());
                result += points[n];
            }
        }
        // Handle case where both one and two QTc prolonging drugs are checked.
        if (checkBoxes[8].isChecked() && checkBoxes[9].isChecked()) {
            result -= 3;
        }
        displayResult(getResultMessage(result),
                getString(R.string.qt_prolongation_risk_result_label));
    }

    private String getResultMessage(int result) {
        String message = " " + result;
        message = getString(R.string.qt_prolongation_result) + message;
        String resultTrailer = " " + getString(R.string.qt_prolongation_result_trailer);
        if (result < 7) {
            message += getString(R.string.qt_prolongation_low_risk) + resultTrailer;
        } else if (result < 11) {
            message += getString(R.string.qt_prolongation_mod_risk) + resultTrailer;
        } else {  // result >= 11
            message += getString(R.string.qt_prolongation_high_risk) + resultTrailer;
        }
        resultMessage = message;
        return resultWithShortReference();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.qtprolongation);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.qt_prolongation_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[10];

        checkBoxes[0] = findViewById(R.id.age_factor);
        checkBoxes[1] = findViewById(R.id.female_sex_factor);
        checkBoxes[2] = findViewById(R.id.loop_diuretic_factor);
        checkBoxes[3] = findViewById(R.id.serum_k_factor);
        checkBoxes[4] = findViewById(R.id.admission_qtc_factor);
        checkBoxes[5] = findViewById(R.id.acute_mi_factor);
        checkBoxes[6] = findViewById(R.id.sepsis_factor);
        checkBoxes[7] = findViewById(R.id.heart_failure_factor);
        checkBoxes[8] = findViewById(R.id.one_qtc_drug_factor);
        checkBoxes[9] = findViewById(R.id.two_qtc_drugs_factor);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.qt_prolongation_risk_full_reference,
                R.string.qt_prolongation_risk_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.qt_prolongation_risk_title,
                R.string.qt_prolongation_risk_instructions);
    }
}
