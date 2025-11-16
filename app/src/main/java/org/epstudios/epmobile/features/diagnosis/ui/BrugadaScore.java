/*
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 9/28/17.
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

package org.epstudios.epmobile.features.diagnosis.ui;

import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.RiskScore;
import org.epstudios.epmobile.UnitConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BrugadaScore extends RiskScore {
    private final List<Integer> points = new ArrayList<>(Arrays.asList(35, 30, 20, 30, 20, 20, 10,
            5, 20, 10, 5, 5));

    @Override
    protected void setContentView() {
        setContentView(R.layout.brugadascore);
    }

   @Override
   protected void setupInsets() {
        setupInsets(R.id.brugada_score_root_view);
   }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[12];

        // ECG
        checkBoxes[0] = findViewById(R.id.spontaneous_type_1_ecg);
        checkBoxes[1] = findViewById(R.id.fever_type_1_ecg);
        checkBoxes[2] = findViewById(R.id.type_2_3_ecg);
        // Clinical history
        checkBoxes[3] = findViewById(R.id.unexplained_arrest);
        checkBoxes[4] = findViewById(R.id.agonal_respirations);
        checkBoxes[5] = findViewById(R.id.arrhythmic_syncope);
        checkBoxes[6] = findViewById(R.id.unclear_syncope);
        checkBoxes[7] = findViewById(R.id.afl_afb);
        // Family history
        checkBoxes[8] = findViewById(R.id.relative_definite_brugada);
        checkBoxes[9] = findViewById(R.id.suspicious_scd);
        checkBoxes[10] = findViewById(R.id.unexplained_scd);
        // Genetic testing
        checkBoxes[11] = findViewById(R.id.pathogenic_mutation);
    }

    @Override
    protected void calculateResult() {
        clearSelectedRisks();
        for (CheckBox selection : checkBoxes) {
            if (selection.isChecked()) {
                addSelectedRisk(selection.getText().toString());
            }
        }

        int result;
        int ecgScore = 0;
        int clinicalScore = 0;
        int familyScore = 0;
        int geneticScore = 0;
        int ecgIndexEnd = 2;
        int ecgIndexBegin = 0;
        for (int i = ecgIndexBegin; i <= ecgIndexEnd; ++i) {
            if (checkBoxes[i].isChecked() && points.get(i) > ecgScore) {
                ecgScore = points.get(i);
            }
        }
        int clinicalIndexEnd = 7;
        int clinicalIndexBegin = 3;
        for (int i = clinicalIndexBegin; i <= clinicalIndexEnd; ++i) {
            if (checkBoxes[i].isChecked() && points.get(i) > clinicalScore) {
                clinicalScore = points.get(i);
            }
        }
        int familyIndexEnd = 10;
        int familyIndexBegin = 8;
        for (int i = familyIndexBegin; i <= familyIndexEnd; ++i) {
            if (checkBoxes[i].isChecked() && points.get(i) > familyScore) {
                familyScore = points.get(i);
            }
        }
        int geneticIndexEnd = 11;
        int geneticIndexBegin = 11;
        for (int i = geneticIndexBegin; i <= geneticIndexEnd; ++i) {
            if (checkBoxes[i].isChecked() && points.get(i) > geneticScore) {
                geneticScore = points.get(i);
            }
        }
        String message;
        if (ecgScore == 0) {
            message = "Score requires at least 1 ECG finding.";
            resultMessage = message;
        } else {
            result = ecgScore + clinicalScore + familyScore + geneticScore;
            message = getResultMessage(result);
        }
        displayResult(message, getString(R.string.brugada_score_title));
    }

    private String getResultMessage(int result) {
        String message = String.format(Locale.getDefault(), getString(R.string.brugada_score_risk_score),
                UnitConverter.trimmedTrailingZeros(result / 10.0));
        if (result >= 35) {
            message += getString(R.string.brugada_score_probable_message);
        } else if (result >= 20) {
            message += getString(R.string.brugada_score_possible_message);
        } else {
            message += getString(R.string.nondiagnostic_message);
        }
        resultMessage = message;
        // no short reference added here
        return message;

    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.brugada_score_reference,
                R.string.brugada_score_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.brugada_score_title);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.brugada_score_reference,
                R.string.brugada_score_link);
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return false;
    }

    @Override
    protected void showActivityKey() {
        showKeyAlertDialog(R.string.brugada_score_key);
    }

}

