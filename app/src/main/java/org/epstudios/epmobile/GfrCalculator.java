package org.epstudios.epmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
public class GfrCalculator extends  EpActivity implements View.OnClickListener {
    private TextView calculatedResultTextView;
    private EditText creatinineEditText;
    private RadioGroup sexRadioGroup;
    private RadioGroup raceRadioGroup;
    private EditText ageEditText;
    protected TextView ccTextView; // cc == Creatinine Clearance
    private Spinner creatinineSpinner;

    private final static int MG_SELECTION = 0;
    private final static int MMOL_SELECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gfr);
        initToolbar();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        }
        else if (id == R.id.clear_button) {
            clearEntries();
        }

    }

    private void calculateResult() {

    }

    private void clearEntries() {

    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> creatAdapter = ArrayAdapter
                .createFromResource(this, R.array.creatinine_unit_labels,
                        android.R.layout.simple_spinner_item);
        creatAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creatinineSpinner.setAdapter(creatAdapter);
//        if (defaultCreatinineUnitSelection.equals(DrugCalculator.CreatinineUnit.MG))
//            creatinineSpinner.setSelection(MG_SELECTION);
//        else
//            creatinineSpinner.setSelection(MMOL_SELECTION);
        AdapterView.OnItemSelectedListener creatItemListener = new AdapterView.OnItemSelectedListener() {
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
        creatinineSpinner.setOnItemSelectedListener(creatItemListener);
    }

    private void updateCreatinineUnitSelection() {
//        DrugCalculator.CreatinineUnit creatinineUnitSelection = getCreatinineUnitSelection();
//        if (creatinineUnitSelection.equals(DrugCalculator.CreatinineUnit.MG))
//            creatinineEditText.setHint(getString(R.string.creatinine_mg_hint));
//        else
//            creatinineEditText
//                    .setHint(getString(R.string.creatinine_mmol_hint));
    }

}
