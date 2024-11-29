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
public class HcmScd2024 extends RiskScore {
    private enum Recommendation {
        class1,
        class2a,
        class2b,
        class3
    }

    @Override
    protected String getFullReference() {
        String reference5 = convertReferenceToText(R.string.hcm_scd_reference_5,
                R.string.hcm_scd_link_5);
        String reference6 = convertReferenceToText(R.string.hcm_scd_reference_6,
                R.string.hcm_scd_link_6);
        String reference7 = convertReferenceToText(R.string.hcm_scd_reference_7,
                R.string.hcm_scd_link_7);
        String reference4 = convertReferenceToText(R.string.hcm_scd_reference_4,
                R.string.hcm_scd_link_4);
        return reference5 + "\n" + reference6 + "\n" + reference7
        + "\n" + reference4;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[4];
        references[0] = new Reference(this, R.string.hcm_scd_reference_5, R.string.hcm_scd_link_5);
        references[1] = new Reference(this, R.string.hcm_scd_reference_6, R.string.hcm_scd_link_6);
        references[2] = new Reference(this, R.string.hcm_scd_reference_7, R.string.hcm_scd_link_7);
        references[3] = new Reference(this, R.string.hcm_scd_reference_4, R.string.hcm_scd_link_4);
        showReferenceAlertDialog(references);
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.hcm_scd_2024_title, R.string.hcm_scd_2024_instructions);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hcm_scd_2024_title);
    }

    @Override
    protected void calculateResult() {
        Recommendation recommendation = calculateRecommendation();
        String message;
        switch (recommendation) {
            case class2a:
                message = "ICD is reasonable (Class 2a)";
                break;
            case class2b:
                message = "ICD may be considered (Class 2b)";
                break;
            case class3:
                message = "ICD is not recommended (Class 3)";
                break;
            case class1:
            default:
                message = "ERROR";
                break;
        }
        addRisks();
        displayResult(message, getString(R.string.hcm_scd_2024_title));
    }

    private void addRisks() {
        boolean familyHxScd = checkBoxes[0].isChecked();
        boolean massiveLvh = checkBoxes[1].isChecked();
        boolean hasSyncope = checkBoxes[2].isChecked();
        boolean apicalAneurysm = checkBoxes[3].isChecked();
        boolean lowLvef = checkBoxes[4].isChecked();
        boolean hxNSVT = checkBoxes[5].isChecked();
        boolean extensiveLge = checkBoxes[6].isChecked();
        clearSelectedRisks();
        addSelectedRisks();
//        if (familyHxScd) {
//            addSelectedRisk("Family history of SCD");
//        }
//        if (massiveLvh) {
//            addSelectedRisk("Massive LVH");
//        }
//        if (hasSyncope) {
//            addSelectedRisk("Unexplained syncope");
//        }
//        if (apicalAneurysm) {
//            addSelectedRisk("Apical aneurysm");
//        }
//        if (lowLvef) {
//            addSelectedRisk("Low LVEF");
//        }
//        if (hxNSVT) {
//            addSelectedRisk("NSVT");
//        }
//        if (extensiveLge) {
//            addSelectedRisk("Extensive LGE");
//        }
    }

    private Recommendation calculateRecommendation() {
        Recommendation recommendation = Recommendation.class3;
        for (int i = 0; i < 5; ++i) {
            if (checkBoxes[i].isChecked()) {
               recommendation = Recommendation.class2a;
               return recommendation;
            }
        }
        if (checkBoxes[5].isChecked() || checkBoxes[6].isChecked()) {
            recommendation = Recommendation.class2b;
            return recommendation;
        }
        return recommendation;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd2024);
    }

    @Override
    protected void init() {
        checkBoxes = new CheckBox[7];
        checkBoxes[0] = findViewById(R.id.family_hx_scd);
        checkBoxes[1] = findViewById(R.id.massive_lvh);
        checkBoxes[2] = findViewById(R.id.unexplained_syncope);
        checkBoxes[3] = findViewById(R.id.apical_aneurysm);
        checkBoxes[4] = findViewById(R.id.low_lvef);
        checkBoxes[5] = findViewById(R.id.nsvt);
        checkBoxes[6] = findViewById(R.id.extensive_lge);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return false;
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityKey() {
        showKeyAlertDialog(R.string.hcm_scd_2024_key);
    }
}
