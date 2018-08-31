package org.epstudios.epmobile;

import android.widget.CheckBox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (C) 2018 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 8/24/18.
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
public final class TamponadeScore extends RiskScore {
    private final List<Integer> points = new ArrayList<>(Arrays.asList(
            20, 20, 10, 10, 10, 10, 10, -10, -10,  // Etiology scores
            10, 30, 5, 10, 10, 20, 5, 5, 20, -10,  // Clinical presentation scores
            10, 5, 10, 30, 10, -10, 10, 15, 15, 20, 10, 10));  // Imaging scores

    @Override
    protected String getFullReference() {
        return getString(R.string.tamponade_reference);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.tamponade_title);
    }

    @Override
    protected String getShortReference() {
        return null;
    }

    @Override
    protected void calculateResult() {
        clearSelectedRisks();
        for (CheckBox selection : checkBox) {
            if (selection.isChecked()) {
                addSelectedRisk(selection.getText().toString());
            }
        }

        int result = 0;
        for (int i = 0; i < points.size(); i++) {
            if (checkBox[i].isChecked()) {
               result += points.get(i);
            }
        }
        String message = getResultMessage(result);
        message += "\n\n" + getString(R.string.urgent_management_message);
        displayResult(message, getString(R.string.tamponade_title));
    }

    private String getResultMessage(int result) {
        String message = String.format(Locale.getDefault(),"Risk Score = %s\n",
                UnitConverter.trimmedTrailingZeros(result / 10.0));
        if (result >= 60) {
            message += getString(R.string.urgent_pericardiocentesis_message);
        }
        else {
            message += getString(R.string.postpone_pericardiocentesis_message);
        }
        setResultMessage(message);
        // no short reference added here
        return message;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.tamponadescore);
    }


    @Override
    protected void init() {
        checkBox = new CheckBox[31];
        checkBox[0] = findViewById(R.id.malignant_disease);
        checkBox[1] = findViewById(R.id.tuberculosis);
        checkBox[2] = findViewById(R.id.recent_radiotherapy);
        checkBox[3] = findViewById(R.id.recent_viral_infection);
        checkBox[4] = findViewById(R.id.recurrent_pe);
        checkBox[5] = findViewById(R.id.chronic_renal_failure);
        checkBox[6] = findViewById(R.id.immunodeficiency);
        checkBox[7] = findViewById(R.id.hypo_hyperthyroidism);
        checkBox[8] = findViewById(R.id.autoimmune_disease);
        checkBox[9] = findViewById(R.id.dyspnea);
        checkBox[10] = findViewById(R.id.orthopnea);
        checkBox[11] = findViewById(R.id.hypotension);
        checkBox[12] = findViewById(R.id.sinus_tachycardia);
        checkBox[13] = findViewById(R.id.oliguria);
        checkBox[14] = findViewById(R.id.pulsus_paradoxus);
        checkBox[15] = findViewById(R.id.chest_pain);
        checkBox[16] = findViewById(R.id.friction_rub);
        checkBox[17] = findViewById(R.id.symptom_worsening);
        checkBox[18] = findViewById(R.id.slow_evolution);
        checkBox[19] = findViewById(R.id.cardiomegaly);
        checkBox[20] = findViewById(R.id.electrical_alternans);
        checkBox[21] = findViewById(R.id.microvoltage);
        checkBox[22] = findViewById(R.id.circumferential_pe);
        checkBox[23] = findViewById(R.id.moderate_pe);
        checkBox[24] = findViewById(R.id.small_pe);
        checkBox[25] = findViewById(R.id.ra_collapse);
        checkBox[26] = findViewById(R.id.large_ivc);
        checkBox[27] = findViewById(R.id.rv_collapse);
        checkBox[28] = findViewById(R.id.la_collapse);
        checkBox[29] = findViewById(R.id.respiratory_variations);
        checkBox[30] = findViewById(R.id.swinging_heart);

    }
}
