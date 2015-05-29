/**
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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DecimalFormat;

public class HcmScd extends RiskScore
{
    private static final int NO_ERROR = 8999;
    private static final int NUMBER_EXCEPTION = 9000;
    private static final int AGE_OUT_OF_RANGE = 9001;
    private static final int THICKNESS_OUT_OF_RANGE = 9002;
    private static final int GRADIENT_OUT_OF_RANGE = 9003;
    private static final int SIZE_OUT_OF_RANGE = 9004;

    private EditText ageEditText;
    private EditText maxLvWallThicknessEditText;
    private EditText maxLvotGradientEditText;
    private EditText laSizeEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void calculateResult() {
        String ageString = ageEditText.getText().toString();
        String maxLvWallThicknessString = maxLvWallThicknessEditText.getText().toString();
        String maxLvotGradientString = maxLvotGradientEditText.getText().toString();
        String laDiameterString = laSizeEditText.getText().toString();
        boolean hasFamilyHxScd = checkBox[0].isChecked();
        boolean hasNsvt = checkBox[1].isChecked();
        boolean hasSyncope = checkBox[2].isChecked();
        try {
            int age = Integer.parseInt(ageString);
            int maxLvWallThickness = Integer.parseInt(maxLvWallThicknessString);
            int maxLvotGradient = Integer.parseInt(maxLvotGradientString);
            int laDiameter = Integer.parseInt(laDiameterString);
            if (age > 115 || age < 16) {
                displayResult(getResultMessage(0.0, AGE_OUT_OF_RANGE),
                        getString(R.string.error_dialog_title));
                return;
            }
            if (maxLvWallThickness < 10 || maxLvWallThickness > 35) {
                displayResult(getResultMessage(0.0, THICKNESS_OUT_OF_RANGE),
                        getString(R.string.error_dialog_title));
                return;
            }
            if (maxLvotGradient < 2 || maxLvotGradient > 154) {
                displayResult(getResultMessage(0.0, GRADIENT_OUT_OF_RANGE),
                        getString(R.string.error_dialog_title));
                return;
            }
            if (laDiameter < 28 || laDiameter > 67) {
                displayResult(getResultMessage(0.0, SIZE_OUT_OF_RANGE),
                        getString(R.string.error_dialog_title));
                return;
            }
            final double coefficient = 0.998;
            double prognosticIndex = 0.15939858 * maxLvWallThickness
                    - 0.00294271 * maxLvWallThickness * maxLvWallThickness
                    + 0.0259082 * laDiameter
                    + 0.00446131 * maxLvotGradient
                    + (hasFamilyHxScd ? 0.4583082 : 0.0)
                    + (hasNsvt ? 0.82639195 : 0.0)
                    + (hasSyncope ? 0.71650361 : 0.0)
                    - 0.01799934 * age;
            double scdProb = 1 - Math.pow(coefficient, Math.exp(prognosticIndex));
            displayResult(getResultMessage(scdProb, NO_ERROR),
                    getString(R.string.hcm_scd_esc_score_title));
            addSelectedRisk("Age = " + ageString + " yrs");
            addSelectedRisk("LV wall thickness = " + maxLvWallThicknessString + " mm");
            addSelectedRisk("LA diameter = " + laDiameterString + " mm");
            addSelectedRisk(("LVOT gradient = " + maxLvotGradientString + " mmHg"));
            if (hasFamilyHxScd) {
                addSelectedRisk(getString(R.string.scd_in_family_label));
            }
            if (hasNsvt) {
                addSelectedRisk(getString(R.string.nonsustained_vt_label));
            }
            if (hasSyncope) {
                addSelectedRisk(getString(R.string.unexplained_syncope_label));
            }
        } catch (NumberFormatException e) {
            displayResult(getResultMessage(0.0, NUMBER_EXCEPTION),
                    getString(R.string.error_dialog_title));
            return;
        }


    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd);
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[3];

        checkBox[0] = (CheckBox) findViewById(R.id.family_hx_scd);
        checkBox[1] = (CheckBox) findViewById(R.id.nsvt);
        checkBox[2] = (CheckBox) findViewById(R.id.unexplained_syncope);

        ageEditText = (EditText) findViewById(R.id.age);
        maxLvWallThicknessEditText = (EditText) findViewById(R.id.max_lv_wall_thickness);
        maxLvotGradientEditText = (EditText) findViewById(R.id.max_lvot_gradient);
        laSizeEditText = (EditText) findViewById(R.id.la_size);
    }

    @Override
    protected String getFullReference() {
        return getString(R.string.hcm_scd_2014_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hcm_scd_esc_score_title);
    }

    protected String getShortReference() {
        // no short reference given, since it is in layout
        return null;
    }

    @Override
    protected void clearEntries() {
        super.clearEntries();
        ageEditText.getText().clear();
        maxLvWallThicknessEditText.getText().clear();
        maxLvotGradientEditText.getText().clear();
        laSizeEditText.getText().clear();
    }

    private String getResultMessage(double result,int errorCode) {
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
            }
            else if (result < 6) {
                recommendations = getString(R.string.icd_may_be_considered_message);
            }
            else {
                recommendations = getString(R.string.icd_should_be_considered_message);
            }
            message = message + "\n" + recommendations;
        }
        // no short reference added here
        // this is needed for clipboard copying of result
        setResultMessage(message);
        return message;
    }

}
