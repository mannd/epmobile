/*
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 5/28/15.
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

package org.epstudios.epmobile;

import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DecimalFormat;

public class HcmRiskScd extends RiskScore {
    protected static final int NO_ERROR = 8999;
    protected static final int NUMBER_EXCEPTION = 9000;
    protected static final int AGE_OUT_OF_RANGE = 9001;
    protected static final int THICKNESS_OUT_OF_RANGE = 9002;
    protected static final int GRADIENT_OUT_OF_RANGE = 9003;
    protected static final int SIZE_OUT_OF_RANGE = 9004;

    protected EditText ageEditText;
    protected EditText maxLvWallThicknessEditText;
    protected EditText maxLvotGradientEditText;
    protected EditText laSizeEditText;

    @Override
    protected void calculateResult() {
        String ageString = ageEditText.getText().toString();
        String maxLvWallThicknessString = maxLvWallThicknessEditText.getText().toString();
        String maxLvotGradientString = maxLvotGradientEditText.getText().toString();
        String laDiameterString = laSizeEditText.getText().toString();
        boolean hasFamilyHxScd = checkBoxes[0].isChecked();
        boolean hasNsvt = checkBoxes[1].isChecked();
        boolean hasSyncope = checkBoxes[2].isChecked();
        try {
            HcmRiskScdModel model = new HcmRiskScdModel(
                    ageString,
                    maxLvWallThicknessString,
                    maxLvotGradientString,
                    laDiameterString,
                    hasFamilyHxScd,
                    hasNsvt,
                    hasSyncope
            );
            double result = model.calculateResult();
            displayResult(getResultMessage(result, NO_ERROR),
                    getString(R.string.hcm_scd_esc_score_title));
            clearSelectedRisks();
            addSelectedRisk("Age = " + ageString + " yrs");
            addSelectedRisk("LV wall thickness = " + maxLvWallThicknessString + " mm");
            addSelectedRisk("LA diameter = " + laDiameterString + " mm");
            addSelectedRisk(("LVOT gradient = " + maxLvotGradientString + " mmHg"));
            addSelectedRisks();
        } catch (AgeOutOfRangeException e) {
            displayResult(getResultMessage(0.0, AGE_OUT_OF_RANGE), getString(R.string.error_dialog_title));
        } catch (LvWallThicknessOutOfRangeException e) {
            displayResult(getResultMessage(0.0, THICKNESS_OUT_OF_RANGE), getString(R.string.error_dialog_title));
        } catch (LvotGradientOutOfRangeException e) {
            displayResult(getResultMessage(0.0, GRADIENT_OUT_OF_RANGE), getString(R.string.error_dialog_title));
        } catch (LaSizeOutOfRangeException e) {
            displayResult(getResultMessage(0.0, SIZE_OUT_OF_RANGE), getString(R.string.error_dialog_title));
        } catch (ParsingException e) {
            displayResult(getResultMessage(0.0, NUMBER_EXCEPTION), getString(R.string.error_dialog_title));
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[3];

        checkBoxes[0] = findViewById(R.id.family_hx_scd);
        checkBoxes[1] = findViewById(R.id.nsvt);
        checkBoxes[2] = findViewById(R.id.unexplained_syncope);

        ageEditText = findViewById(R.id.age);
        maxLvWallThicknessEditText = findViewById(R.id.max_lv_wall_thickness);
        maxLvotGradientEditText = findViewById(R.id.max_lvot_gradient);
        laSizeEditText = findViewById(R.id.la_size);
    }

    @Override
    protected String getFullReference() {

        String reference1 = convertReferenceToText(R.string.hcm_scd_reference_1,
                R.string.hcm_scd_link_1);
        String reference2 = convertReferenceToText(R.string.hcm_scd_reference_2,
                R.string.hcm_scd_link_2);
        return reference1 + "\n" + reference2;
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hcm_scd_esc_score_title);
    }

    @Override
    protected void clearEntries() {
        super.clearEntries();
        ageEditText.getText().clear();
        maxLvWallThicknessEditText.getText().clear();
        maxLvotGradientEditText.getText().clear();
        laSizeEditText.getText().clear();
    }

    private String getResultMessage(double result, int errorCode) {
        String message = "";
        switch (errorCode) {
            case NUMBER_EXCEPTION:
                message = getString(R.string.invalid_entries_message);
                break;
            case AGE_OUT_OF_RANGE:
                message = getString(R.string.invalid_age_message);
                break;
            case THICKNESS_OUT_OF_RANGE:
                message = getString(R.string.invalid_thickness_message);
                break;
            case GRADIENT_OUT_OF_RANGE:
                message = getString(R.string.invalid_gradient_message);
                break;
            case SIZE_OUT_OF_RANGE:
                message = getString(R.string.invalid_diameter_message);
                break;
            case NO_ERROR:      // drop through
            default:
                break;
        }
        if (errorCode == NO_ERROR) {
            // convert to percentage
            result = result * 100.0;
            DecimalFormat formatter = new DecimalFormat("##.##");
            String formattedResult = formatter.format(result);
            message = "5 year SCD risk = " + formattedResult + "%";
            String recommendations;
            if (result < 4) {
                recommendations = getString(R.string.icd_not_indicated_message);
            } else if (result < 6) {
                recommendations = getString(R.string.icd_may_be_considered_message);
            } else {
                recommendations = getString(R.string.icd_should_be_considered_message);
            }
            message = message + "\n" + recommendations;
        }
        // no short reference added here
        // this is needed for clipboard copying of result
        setResultMessage(message);
        return message;
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[2];
        references[0] = new Reference(this, R.string.hcm_scd_reference_1, R.string.hcm_scd_link_1);
        references[1] = new Reference(this, R.string.hcm_scd_reference_2, R.string.hcm_scd_link_2);
        showReferenceAlertDialog(references);
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return false;
    }

    @Override
    protected void showActivityKey() {
        showKeyAlertDialog(R.string.hcm_scd_key);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    protected void showActivityInstructions() {
        showAlertDialog(R.string.hcm_scd_esc_score_title, R.string.hcm_scd_esc_score_instructions);
    }
}
