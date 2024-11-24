package org.epstudios.epmobile;

import android.widget.CheckBox;

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
    @Override
    protected String getFullReference() {
        String reference2 = convertReferenceToText(R.string.hcm_scd_reference_2,
                R.string.hcm_scd_link_2);
        String reference3 = convertReferenceToText(R.string.hcm_scd_reference_3,
                R.string.hcm_scd_link_3);
        String reference4 = convertReferenceToText(R.string.hcm_scd_reference_4,
                R.string.hcm_scd_link_4);
        String fullReference = reference2 + "\n" + reference3 + "\n" + reference4;
        return fullReference;
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
        boolean hasFamilyHxScd = checkBox[0].isChecked();
        boolean hasNsvt = checkBox[1].isChecked();
        boolean hasSyncope = checkBox[2].isChecked();
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
                    getString(R.string.hcm_scd_2022_title));
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

    private String getResultMessage(double result, int errorCode )  {
        return "TEST" + result;
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd2022);
    }

    @Override
    protected void init() {
        super.init();
    }
}
