package org.epstudios.epmobile;

import android.widget.CheckBox;

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
        boolean apicalAneurysm = checkBox[3].isChecked();
        boolean lowLvef = checkBox[4].isChecked();
        boolean extensiveLge = checkBox[5].isChecked();
        boolean abnormalBP = checkBox[6].isChecked();
        boolean sarcomericMutation = checkBox[7].isChecked();
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
            clearSelectedRisks();
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
            if (apicalAneurysm) {
                addSelectedRisk(getString(R.string.apical_aneurysm_label));
            }
            if (lowLvef) {
                addSelectedRisk(getString(R.string.low_lvef_label));
            }
            if (extensiveLge) {
                addSelectedRisk(getString(R.string.extensive_lge_label));
            }
            if (abnormalBP) {
                addSelectedRisk(getString(R.string.abnormal_bp_response_label));
            }
            if (sarcomericMutation) {
                addSelectedRisk(getString(R.string.sarcomeric_mutation_label));
            }
            // TODO: Double check this: don't we have to displayResult AFTER adding selected risks?
            displayResult(getResultMessage(result,
                    apicalAneurysm,
                    lowLvef,
                    extensiveLge,
                    abnormalBP,
                    sarcomericMutation,
                    NO_ERROR), getString(R.string.hcm_scd_2022_title));
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
        // TODO: setResultMessage(message); is repeated everywhere
        // We need a better mechanism for this embedded in RisScore!
        return message;
    }

    private String getResultMessage(
            double result,
            boolean apicalAneurysm,
            boolean lowLvef,
            boolean extensiveLge,
            boolean abnormalBP,
            boolean sarcomericMutation,
            int errorCode ) {
        if (errorCode != NO_ERROR) {
            return getResultMessage(result, errorCode);
        } else {
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
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd2022);
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[8];

        checkBox[0] = findViewById(R.id.family_hx_scd);
        checkBox[1] = findViewById(R.id.nsvt);
        checkBox[2] = findViewById(R.id.unexplained_syncope);
        checkBox[3] = findViewById(R.id.apical_aneurysm);
        checkBox[4] = findViewById(R.id.low_lvef);
        checkBox[5] = findViewById(R.id.extensive_lge);
        checkBox[6] = findViewById(R.id.abnormal_bp_response);
        checkBox[7] = findViewById(R.id.sarcomeric_mutation);

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
