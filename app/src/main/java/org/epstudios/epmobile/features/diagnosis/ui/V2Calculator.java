package org.epstudios.epmobile.features.diagnosis.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.EpActivity;

import java.text.DecimalFormat;

import androidx.core.content.ContextCompat;

import static java.lang.Math.abs;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 5/24/24.
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
public class V2Calculator extends EpActivity implements View.OnClickListener {
    private EditText rWaveVt;
    private EditText sWaveVt;
    private EditText rWaveSr;
    private EditText sWaveSr;
    private TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2calculator);
        initToolbar();

        rWaveVt = findViewById(R.id.rWaveVtEditText);
        sWaveVt = findViewById(R.id.sWaveVtEditText);
        rWaveSr = findViewById(R.id.rWaveSrEditText);
        sWaveSr = findViewById(R.id.sWaveSrEditText);
        resultText = findViewById(R.id.calculated_v2_transition_ratio);

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        clearEntries();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, V2TransitionRatioVt.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculate();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void calculate() {
        CharSequence rVtText = rWaveVt.getText();
        CharSequence sVtText = sWaveVt.getText();
        CharSequence rSrText = rWaveSr.getText();
        CharSequence sSrText = sWaveSr.getText();
        resultText.setText(null);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.md_theme_secondary));
        try {
            double rVt = abs(Double.parseDouble(rVtText.toString()));
            double sVt = abs(Double.parseDouble(sVtText.toString()));
            double rSr = abs(Double.parseDouble(rSrText.toString()));
            double sSr = abs(Double.parseDouble(sSrText.toString()));
            double rsVt = rVt + sVt;
            double rsSr = rSr + sSr;
            if (rsVt == 0 || rsSr == 0) {
                throw new NumberFormatException();
            }
            double ratioVt = rVt / rsVt;
            double ratioSr = rSr / rsSr;
            if (ratioSr == 0) {
                throw new NumberFormatException();
            }
            double v2Ratio = ratioVt / ratioSr;
            DecimalFormat df = new DecimalFormat("#.##");
            String message = "";
            message += String.format(getString(R.string.v2_transition_calculated_result), df.format(v2Ratio));
            message += "\n";
            if (v2Ratio < 0.6) {
                message += getString(R.string.v2_transition_ratio_vt_is_rvot);
            }
            else {
                message += getString(R.string.v2_transition_ratio_vt_is_lvot);
            }
            resultText.setText(message);
        } catch (NumberFormatException e) {
            resultText.setText(getString(R.string.invalid_warning));
            resultText.setTextColor(Color.RED);
        }

    }

    private void clearEntries() {
        rWaveVt.setText(null);
        rWaveSr.setText(null);
        sWaveVt.setText(null);
        sWaveSr.setText(null);
        resultText.setText(null);
        resultText.setTextColor(ContextCompat.getColor(this, R.color.md_theme_secondary));
        rWaveVt.requestFocus();
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.v2_transition_ratio_vt_reference_0,
                R.string.v2_transition_ratio_vt_link_0);
    }


    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.v2_calculator_title,
                R.string.v2_calculator_instructions);
    }
}
