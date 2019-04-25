package org.epstudios.epmobile;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Copyright (C) 2019 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 4/16/19.
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
public class ArvcRisk extends DiagnosticScore {
    EditText ageText;
    RadioGroup sexRadioGroup;
    Switch syncopeSwitch;
    SeekBar twiSeekBar;
    TextView twiTextView;
    EditText pvcText;
    Switch nsvtSwitch;
    SeekBar rvefSeekBar;
    TextView rvefTextView;
    TextView arvcRiskReference;

    @Override
    protected void init() {
        ageText = findViewById(R.id.ageEditText);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        syncopeSwitch = findViewById(R.id.hxSyncopeSwitch);
        twiSeekBar = findViewById(R.id.twiCountSeekBar);
        twiTextView = findViewById(R.id.twiCountSeekBarValue);
        pvcText = findViewById(R.id.pvcCountText);
        nsvtSwitch = findViewById(R.id.hxNSVTSwitch);
        rvefSeekBar = findViewById(R.id.rvefSeekBar);
        rvefTextView = findViewById(R.id.rvefSeekBarValue);
        twiSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                twiTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rvefSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rvefTextView.setText(String.valueOf(adjustRVEF(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        arvcRiskReference = findViewById(R.id.arvc_risk_reference);
        arvcRiskReference.setText("\nReference: " + getFullReference());
        clearEntries();
    }

    private int adjustRVEF(int rvef) {
        int minEF = 5;
        int maxEF = 70;
        if (rvef < minEF) {
            return minEF;
        }
        if (rvef > maxEF) {
            return maxEF;
        }
        return rvef;
    }

    //@Override
    protected String getFullReference() {
        return getString(R.string.arvc_risk_full_reference);
    }

    //@Override
    protected String getRiskLabel() {
        return getString(R.string.arvc_risk_title);
    }

    //@Override
    protected String getShortReference() {
        return getString(R.string.arvc_risk_short_reference);
    }

    @Override
    protected void calculateResult() {
        String message;
        if (dataIncomplete()) {
            message = getString(R.string.data_incomplete_message);
            displayResult(message, getString(R.string.error_dialog_title));
            return;
        }
        try {
            int age = Integer.parseInt(ageText.getText().toString());
            if (age < 14 || age > 90) {
                message = getString(R.string.arvc_age_out_of_range_message);
                displayResult(message, getString(R.string.arvc_age_out_of_range_title));
                return;
            }
            int sex = sexRadioGroup.getCheckedRadioButtonId() == R.id.male ? 1 : 0;
            int recentSyncope = syncopeSwitch.isChecked() ? 1 : 0;
            int numTWI = twiSeekBar.getProgress();
            int pvcCount = Integer.parseInt(pvcText.getText().toString());
            if (pvcCount > 100000) {
                message = getString(R.string.arvc_pvc_count_range_error_message);
                displayResult(message, getString(R.string.arvc_pvc_count_range_error_title));
                return;
            }
            int hxNSVT = nsvtSwitch.isChecked() ? 1 : 0;
            int rvef = adjustRVEF(rvefSeekBar.getProgress());
            ArvcRiskModel model = new ArvcRiskModel(sex, age, recentSyncope, hxNSVT, pvcCount, numTWI, rvef);
            double yr5Risk = model.calculateRisk(ArvcRiskModel.year5);
            double yr2Risk = model.calculateRisk(ArvcRiskModel.year2);
            double yr1Risk = model.calculateRisk(ArvcRiskModel.year1);
            message = String.format( "%s%% within 5 years\n", NumberFormat.getInstance().format(yr5Risk) );
            message += String.format( "%s%% within 2 years\n", NumberFormat.getInstance().format(yr2Risk) );
            message += String.format( "%s%% within 1 year", NumberFormat.getInstance().format(yr1Risk) );
            displayResult(message, getString(R.string.risk_sus_va_title));
        }
        catch (Exception ex) {
            message = getString(R.string.values_range_error_message);
            displayResult(message, getString(R.string.error_dialog_title));
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.arvcrisk);
    }

    @Override
    protected void clearEntries() {
        ageText.setText("");
        sexRadioGroup.clearCheck();
        syncopeSwitch.setChecked(false);
        twiSeekBar.setProgress(0);
        pvcText.setText("");
        nsvtSwitch.setChecked(false);
        rvefSeekBar.setProgress(50);

    }

    private boolean dataIncomplete() {
        return sexRadioGroup.getCheckedRadioButtonId() == -1
                || TextUtils.isEmpty(ageText.getText())
                || TextUtils.isEmpty(pvcText.getText());
    }

}
