package org.epstudios.epmobile;

import android.widget.CheckBox;

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
        return getString(R.string.qt_prolongation_risk_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.qt_prolongation_risk_label);
    }

    @Override
    protected String getShortReference() {
        return getString(R.string.qt_prolongation_risk_short_reference);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        int points[] = {1,1,1,2,2,2,3,3,3,6};
        for (int n = 0; n < 10; n++) {
            if (checkBox[n].isChecked()) {
                addSelectedRisk(checkBox[n].getText().toString());
                result += points[n];
            }
        }
        // Handle case where both one and two QTc prolonging drugs are checked.
        if (checkBox[8].isChecked() && checkBox[9].isChecked()) {
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
        }
        else if (result < 11) {
            message += getString(R.string.qt_prolongation_mod_risk) + resultTrailer;
        }
        else {  // result >= 11
            message += getString(R.string.qt_prolongation_high_risk) + resultTrailer;
        }
        setResultMessage(message);
        return resultWithShortReference();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.qtprolongation);
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[10];

        checkBox[0] = findViewById(R.id.age_factor);
        checkBox[1] = findViewById(R.id.female_sex_factor);
        checkBox[2] = findViewById(R.id.loop_diuretic_factor);
        checkBox[3] = findViewById(R.id.serum_k_factor);
        checkBox[4] = findViewById(R.id.admission_qtc_factor);
        checkBox[5] = findViewById(R.id.acute_mi_factor);
        checkBox[6] = findViewById(R.id.sepsis_factor);
        checkBox[7] = findViewById(R.id.heart_failure_factor);
        checkBox[8] = findViewById(R.id.one_qtc_drug_factor);
        checkBox[9] = findViewById(R.id.two_qtc_drugs_factor);
    }
}
