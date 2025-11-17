package org.epstudios.epmobile.features.riskscores.ui;

import android.widget.CheckBox;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.Reference;
import org.epstudios.epmobile.features.riskscores.data.CalculationResult;
import org.epstudios.epmobile.features.riskscores.data.HcmRiskScdModel;
import org.epstudios.epmobile.features.riskscores.data.HcmValidationError;

import java.text.DecimalFormat;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/11/24.
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
public class HcmScd2022 extends HcmRiskScd {
    private enum Recommendation {
        class1,
        class2a,
        class2b,
        class3
    }

    @Override
    protected String getFullReference() {
        String reference2 = convertReferenceToText(R.string.hcm_scd_reference_2,
                R.string.hcm_scd_link_2);
        String reference3 = convertReferenceToText(R.string.hcm_scd_reference_3,
                R.string.hcm_scd_link_3);
        String reference4 = convertReferenceToText(R.string.hcm_scd_reference_4,
                R.string.hcm_scd_link_4);
        return reference2 + "\n" + reference3 + "\n" + reference4;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[3];
        references[0] = new Reference(this, R.string.hcm_scd_reference_2, R.string.hcm_scd_link_2);
        references[1] = new Reference(this, R.string.hcm_scd_reference_3, R.string.hcm_scd_link_3);
        references[2] = new Reference(this, R.string.hcm_scd_reference_4, R.string.hcm_scd_link_4);
        showReferenceAlertDialog(references);
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.hcm_scd_2022_title, R.string.hcm_scd_2022_instructions);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hcm_scd_2022_title);
    }

    @Override
    protected void calculateResult() {
        String ageString = ageEditText.getText().toString();
        String maxLvWallThicknessString = maxLvWallThicknessEditText.getText().toString();
        String maxLvotGradientString = maxLvotGradientEditText.getText().toString();
        String laDiameterString = laSizeEditText.getText().toString();
        boolean hasFamilyHxScd = checkBoxes[0].isChecked();
        boolean hasNsvt = checkBoxes[1].isChecked();
        boolean hasSyncope = checkBoxes[2].isChecked();
        boolean apicalAneurysm = checkBoxes[3].isChecked();
        boolean lowLvef = checkBoxes[4].isChecked();
        boolean extensiveLge = checkBoxes[5].isChecked();
        boolean abnormalBP = checkBoxes[6].isChecked();
        boolean sarcomericMutation = checkBoxes[7].isChecked();
            HcmRiskScdModel model = new HcmRiskScdModel(
                    ageString,
                    maxLvWallThicknessString,
                    maxLvotGradientString,
                    laDiameterString,
                    hasFamilyHxScd,
                    hasNsvt,
                    hasSyncope
            );
            CalculationResult calculationResult = model.calculateResult();

        if (calculationResult instanceof CalculationResult.Success) {
            CalculationResult.Success successResult = (CalculationResult.Success) calculationResult;
            double value = successResult.getValue();
            clearSelectedRisks();

            if (!ageString.isEmpty()) {
                addSelectedRisk(getString(R.string.hcm_scd_input_age, ageString));
            }
            if (!maxLvWallThicknessString.isEmpty()) {
                addSelectedRisk(getString(R.string.hcm_scd_input_lv_wall_thickness, maxLvWallThicknessString));
            }
            if (!laDiameterString.isEmpty()) {
                addSelectedRisk(getString(R.string.hcm_scd_input_la_diameter, laDiameterString));
            }
            if (!maxLvotGradientString.isEmpty()) {
                addSelectedRisk(getString(R.string.hcm_scd_input_lvot_gradient, maxLvotGradientString));
            }
            addSelectedRisks(checkBoxes);
            displayResult(getResultMessage(value,
                    apicalAneurysm,
                    lowLvef,
                    extensiveLge,
                    abnormalBP,
                    sarcomericMutation
            ), getString(R.string.hcm_scd_2022_title));
        } else if (calculationResult instanceof CalculationResult.Failure) {
            CalculationResult.Failure failureResult = (CalculationResult.Failure) calculationResult;
            HcmValidationError error = failureResult.getError();
            String errorMessage;

            if (error instanceof HcmValidationError.AgeOutOfRange) {
                HcmValidationError.AgeOutOfRange ageError = (HcmValidationError.AgeOutOfRange) error;
                errorMessage = getString(R.string.error_age_out_of_range, ageError.getAge());

            } else if (error instanceof HcmValidationError.LvWallThicknessOutOfRange) {
                HcmValidationError.LvWallThicknessOutOfRange thicknessError = (HcmValidationError.LvWallThicknessOutOfRange) error;
                errorMessage = getString(R.string.error_lv_wall_thickness_out_of_range, thicknessError.getThickness());

            } else if (error instanceof HcmValidationError.LaSizeOutOfRange) {
                HcmValidationError.LaSizeOutOfRange sizeError = (HcmValidationError.LaSizeOutOfRange) error;
                errorMessage = getString(R.string.error_la_size_out_of_range, sizeError.getSize());

            } else if (error instanceof HcmValidationError.LvotGradientOutOfRange) {
                HcmValidationError.LvotGradientOutOfRange gradientError = (HcmValidationError.LvotGradientOutOfRange) error;
                errorMessage = getString(R.string.error_lvot_gradient_out_of_range, gradientError.getGradient());

            } else if (error instanceof HcmValidationError.ParsingError) {
                errorMessage = getString(R.string.error_parsing);

            } else {
                errorMessage = getString(R.string.error_unknown);
            }

            displayResult(errorMessage, getString(R.string.error_dialog_title));
        }
    }

    private String getResultMessage(
            double result,
            boolean apicalAneurysm,
            boolean lowLvef,
            boolean extensiveLge,
            boolean abnormalBP,
            boolean sarcomericMutation ) {
        Recommendation recommendation = Recommendation.class3;
        if (result >= 0.06) {
            recommendation = Recommendation.class2a;
        }
        if (result >= 0.04 && result < 0.06) {
            if (apicalAneurysm || lowLvef || extensiveLge || abnormalBP || sarcomericMutation) {
                recommendation = Recommendation.class2a;
            } else {
                recommendation = Recommendation.class2b;
            }
        }
        if (result < 0.04) {
            if (apicalAneurysm || lowLvef || extensiveLge) {
                recommendation = Recommendation.class2b;
            } else {
                recommendation = Recommendation.class3;
            }
        }
        result *= 100.0;
        DecimalFormat formatter = new DecimalFormat("##.##");
        String formattedResult = formatter.format(result);
        String message = "5 year SCD risk = " + formattedResult + "%";
        switch (recommendation) {
            case class3:
                message += "\nICD generally not indicated. (Class 3)";
                break;
            case class2b:
                message += "\nICD may be considered. (Class 2b)";
                break;
            case class2a:
                message += "\nICD should be considered. (Class 2a)";
                break;
            case class1:
                message += "\nICD is indicated. (Class 1)";
                break;
        }
        return message;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd2022);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.hcm_scd_2022_root_view);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[8];

        checkBoxes[0] = findViewById(R.id.family_hx_scd);
        checkBoxes[1] = findViewById(R.id.nsvt);
        checkBoxes[2] = findViewById(R.id.unexplained_syncope);
        checkBoxes[3] = findViewById(R.id.apical_aneurysm);
        checkBoxes[4] = findViewById(R.id.low_lvef);
        checkBoxes[5] = findViewById(R.id.extensive_lge);
        checkBoxes[6] = findViewById(R.id.abnormal_bp_response);
        checkBoxes[7] = findViewById(R.id.sarcomeric_mutation);

        ageEditText = findViewById(R.id.age);
        maxLvWallThicknessEditText = findViewById(R.id.max_lv_wall_thickness);
        maxLvotGradientEditText = findViewById(R.id.max_lvot_gradient);
        laSizeEditText = findViewById(R.id.la_size);
    }

    @Override
    protected void showActivityKey() {
        String key = getString(R.string.hcm_scd_key) + getString(R.string.hcm_scd_2022_key);
        showAlertDialog(getString(R.string.hcm_scd_2022_title), key);
    }
}
