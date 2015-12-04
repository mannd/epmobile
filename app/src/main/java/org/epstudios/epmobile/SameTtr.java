package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 12/2/15.
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
public class SameTtr  extends RiskScore {

    @Override
    protected String getFullReference() {
        return getString(R.string.same_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.same_risk_label);
    }

    @Override
    protected String getShortReference() {
        return getString(R.string.same_short_reference);
    }

    @Override
    protected void calculateResult() {
        int result = 0;
        clearSelectedRisks();
        for (int i = 0; i < checkBox.length; i++) {
            if (checkBox[i].isChecked()) {
                addSelectedRisk(checkBox[i].getText().toString());
                if (i == 4 || i == 5)
                    result = result + 2;
                else
                    result++;
            }
        }
        displayResult(getResultMessage(result),
                getString(R.string.same_tt2r2_title));
    }

    private String getResultMessage(int result) {
        String message;
        if (result <= 2)
            message = getString(R.string.low_same_risk_message);
        else
            message = getString(R.string.high_same_risk_message);
        message = getRiskLabel() + " score = " + result + "\n" + message;
        setResultMessage(message);
        return resultWithShortReference();
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.samett2r2);
    }

    @Override
    protected void init() {
        checkBox = new CheckBox[6];

        checkBox[0] = (CheckBox) findViewById(R.id.sex);
        checkBox[1] = (CheckBox) findViewById(R.id.age);
        checkBox[2] = (CheckBox) findViewById(R.id.medhx);
        checkBox[3] = (CheckBox) findViewById(R.id.treatment);
        checkBox[4] = (CheckBox) findViewById(R.id.smoking);
        checkBox[5] = (CheckBox) findViewById(R.id.race);

    }
}
