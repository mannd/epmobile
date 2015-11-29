package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 11/29/15.
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
public class AtriaBleed extends RiskScore {

    @Override
    protected void setContentView() {
        setContentView((R.layout.atriableed));
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[5];

        checkBox[0] = (CheckBox) findViewById(R.id.anemia);
        checkBox[1] = (CheckBox) findViewById(R.id.renal_disease);
        checkBox[2] = (CheckBox) findViewById(R.id.age75);
        checkBox[3] = (CheckBox) findViewById(R.id.prior_hemorrhage);
        checkBox[4] = (CheckBox) findViewById(R.id.htn);
    }


    @Override
    protected String getFullReference() {
        return getString(R.string.atria_bleed_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.atria_bleed_risk_label);
    }

    @Override
    protected String getShortReference() {
        return getString(R.string.atria_bleed_short_reference);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
         for (int i = 0; i < checkBox.length; i++) {
            if (checkBox[i].isChecked()) {
                addSelectedRisk(checkBox[i].getText().toString());
                if (i == 0 || i == 1)
                    result = result + 3;
                else if (i == 2)
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.atria_bleeding_score_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result <= 3)
            message = getString(R.string.low_atria_bleed_message);
        else if (result == 4)
            message = getString(R.string.medium_atria_bleed_message);
        else
            message = getString(R.string.high_atria_bleed_message);
        message = getRiskLabel() + " score = " + result + "\n" + message;
        setResultMessage(message);
        return resultWithShortReference();
    }





}
