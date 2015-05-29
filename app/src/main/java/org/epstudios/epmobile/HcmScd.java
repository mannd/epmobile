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
        setContentView(R.layout.hcmscd);
        // super sets up toolbar, so need layout first.  Call
        // after setContentView().
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void calculateResult() {
        Log.d("EPS", "Calculate HCM-SCD");
        String ageString = ageEditText.getText().toString();
        String maxLvWallThicknessString = maxLvWallThicknessEditText.getText().toString();
        String maxLvotGradientString = maxLvotGradientEditText.getText().toString();
        boolean hasFamilyHxScd = checkBox[0].isChecked();
        boolean hasNsvt = checkBox[1].isChecked();
        boolean hasSyncope = checkBox[2].isChecked();
        try {
            int age = Integer.parseInt(ageString);
            int maxLvWallThickness = Integer.parseInt(maxLvWallThicknessString);
            int maxLvotGradient = Integer.parseInt(maxLvotGradientString);
            if (age > 116 || age < 16) {
                displayResult(getResultMessage(0.0, AGE_OUT_OF_RANGE),
                        getString(R.string.error_dialog_title));
                return;
            }


        } catch (NumberFormatException e) {
            Log.d("EPS", "Invalid number");
            displayResult(getResultMessage(0.0, NUMBER_EXCEPTION),
                    getString(R.string.error_dialog_title));
            return;
        }


    }

    @Override
    protected void setContentView() {

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
        return null;
    }

    @Override
    protected String getRiskLabel() {
        return null;
    }

    protected String getShortReference() {
        return "";
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
            // etc.
            default:
                break;
        }
        if (errorCode == NO_ERROR) {
            // do the message here
        }
        setResultMessage(message);
        // no short reference added here
        return message;
    }

}
