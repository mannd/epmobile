package org.epstudios.epmobile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

/**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 8/24/22.
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
public class GfrCalculator extends EpActivity implements View.OnClickListener {
    private TextView calculatedResultTextView;
    private EditText creatinineEditText;
    private RadioGroup sexRadioGroup;
    private RadioGroup raceRadioGroup;
    private EditText ageEditText;
    protected TextView ccTextView; // cc == Creatinine Clearance
    private Spinner creatinineSpinner;

    private final static int MG_SELECTION = 0;
    private final static int MMOL_SELECTION = 1;
    private CreatinineUnit defaultCreatinineUnitSelection = CreatinineUnit.MG;

    private final static int MAX_AGE = 120;

    private enum CreatinineUnit {
        MG, MMOL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gfr);
        initToolbar();
        View calculateQtcButton = findViewById(R.id.calculate_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);
        calculatedResultTextView = findViewById(R.id.calculated_gfr);
        creatinineEditText = findViewById(R.id.creatinineEditText);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        raceRadioGroup = findViewById(R.id.raceRadioGroup);
        ageEditText = findViewById(R.id.ageEditText);
        ccTextView = findViewById(R.id.ccTextView);
        creatinineSpinner = findViewById(R.id.creatinine_spinner);

        getPrefs();
        setAdapters();
        clearEntries();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void calculateResult() {
        // Reset to normal appearing text color
        calculatedResultTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Medium);
        CharSequence ageText = ageEditText.getText();
        CharSequence crText = creatinineEditText.getText();
        boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
        boolean isBlack = raceRadioGroup.getCheckedRadioButtonId() == R.id.black;
        try {
            double age = Double.parseDouble(ageText.toString());
            if (age > MAX_AGE) {
                calculatedResultTextView.setText(getString(R.string.invalid_warning));
                calculatedResultTextView.setTextColor(Color.RED);
                return;
            }
            // TODO: screen out min and max age
            double cr = Double.parseDouble(crText.toString());
            if (getCreatinineUnitSelection() == CreatinineUnit.MMOL) {
                cr = Gfr.convertMicroMolPerLiterToMgPerDL(cr);
            }
            double result = Gfr.ckdEpiGfr(cr, (int)age, isMale, isBlack);
            String resultString = getString(R.string.gfr_result_string, Math.round(result));
            calculatedResultTextView.setText(resultString);
        } catch (NumberFormatException e) {
            calculatedResultTextView.setText(getString(R.string.invalid_warning));
            calculatedResultTextView.setTextColor(Color.RED);
        }
    }

    private void clearEntries() {
        ageEditText.setText(null);
        creatinineEditText.setText(null);
        calculatedResultTextView.setText(getString(R.string.gfr_result_label));
        calculatedResultTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Large); // this also resets the color to default
        ageEditText.requestFocus();
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> creatinineAdapter = ArrayAdapter
                .createFromResource(this, R.array.creatinine_unit_labels,
                        android.R.layout.simple_spinner_item);
        creatinineAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creatinineSpinner.setAdapter(creatinineAdapter);
        if (defaultCreatinineUnitSelection.equals(CreatinineUnit.MG))
            creatinineSpinner.setSelection(MG_SELECTION);
        else
            creatinineSpinner.setSelection(MMOL_SELECTION);
        AdapterView.OnItemSelectedListener creatinineItemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateCreatinineUnitSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };
        creatinineSpinner.setOnItemSelectedListener(creatinineItemListener);
    }

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String creatinineUnitPreference = prefs.getString(
                getString(R.string.creatinine_clearance_unit_key), "MG");
        if (creatinineUnitPreference.equals("MG"))
            defaultCreatinineUnitSelection = CreatinineUnit.MG;
        else
            defaultCreatinineUnitSelection = CreatinineUnit.MMOL;
    }

    private void updateCreatinineUnitSelection() {
        CreatinineUnit creatinineUnitSelection = getCreatinineUnitSelection();
        if (creatinineUnitSelection.equals(CreatinineUnit.MG))
            creatinineEditText.setHint(getString(R.string.creatinine_mg_hint));
        else
            creatinineEditText
                    .setHint(getString(R.string.creatinine_mmol_hint));
    }

    private CreatinineUnit getCreatinineUnitSelection() {
        int result = creatinineSpinner.getSelectedItemPosition();
        if (result == MG_SELECTION)
            return CreatinineUnit.MG;
        else
            return CreatinineUnit.MMOL;
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.gfr_reference,
                R.string.gfr_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.gfr_calculator_title,
                R.string.gfr_instructions);
    }

}
