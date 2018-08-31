package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2016 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 9/18/16.
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
public class IcdMortalityRisk extends RiskScore {
    final int VHR_SCORE = 99;

    private class RiskResult {
        int conv;
        int icd;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.icdmortalityrisk);
    }

    @Override
    protected String getFullReference() {
        return getString(R.string.icd_mortality_risk_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.icd_mortality_risk_title);
    }

    @Override
    protected String getShortReference() {
        return getString(R.string.icd_mortality_risk_short_reference);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBox.length; i++) {
            if (checkBox[i].isChecked()) {
                addSelectedRisk(checkBox[i].getText().toString());
                if (i == 0) // Very High Risk group
                    result = result + VHR_SCORE;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result), getString(R.string.icd_mortality_risk_title));
    }

    private String getResultMessage(int score) {
        RiskResult risk = getRisk(score);
        String message;
        if (score >= VHR_SCORE) {
            message = getString(R.string.very_high_risk_label) + "\n" + getString(R.string.vhr_message, risk.conv, risk.icd);
        }
        else {
            message = getRiskLabel() + " score = " + score + "\n" + getString(R.string.not_vhr_message,
                    risk.conv, risk.icd);
        }
        message += "\n" + getString(R.string.icd_mortality_warning_message);
        setResultMessage(message);
        return resultWithShortReference();

    }

    private RiskResult getRisk(int score) {
        RiskResult riskResult = new RiskResult();
        if (score >= VHR_SCORE) {
            riskResult.conv = 43;
            riskResult.icd = 51;
            return riskResult;
        }
        switch (score) {
            case 0:
                riskResult.conv = 8;
                riskResult.icd = 7;
                break;
            case 1:
                riskResult.conv = 22;
                riskResult.icd = 9;
                break;
            case 2:
                riskResult.conv = 32;
                riskResult.icd = 15;
                break;
            case 3:
            case 4:
            case 5:
                riskResult.conv = 32;
                riskResult.icd = 29;
                break;
        }
        return riskResult;
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[6];

        checkBox[0] = findViewById(R.id.very_high_risk);
        checkBox[1] = findViewById(R.id.nyha_greater_than_2);
        checkBox[2] = findViewById(R.id.age70);
        checkBox[3] = findViewById(R.id.bun_greater_than_26);
        checkBox[4] = findViewById(R.id.qrs_greater_than_12);
        checkBox[5] = findViewById(R.id.afb_baseline);
    }
}
