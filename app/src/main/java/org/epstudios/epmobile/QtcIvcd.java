package org.epstudios.epmobile;

/**
 * Copyright (C) 2016 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/30/16.
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

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class QtcIvcd extends EpActivity implements View.OnClickListener {
    private enum IntervalRate {
        INTERVAL, RATE
    };

    private RadioGroup sexRadioGroup;
    private Spinner intervalRateSpinner;
    private EditText rrEditText;
    private EditText qtEditText;
    private EditText qrsEditText;
    private AdapterView.OnItemSelectedListener itemListener;


    private final static int INTERVAL_SELECTION = 0;
    private final static int RATE_SELECTION = 1;

    private IntervalRate defaultIntervalRateSelection = IntervalRate.INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.qtcivcd);
        super.onCreate(savedInstanceState);

        View calculateQtcButton = findViewById(R.id.calculate_qtc_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        intervalRateSpinner = (Spinner) findViewById(R.id.interval_rate_spinner);
        rrEditText = (EditText) findViewById(R.id.rrEditText);
        qtEditText = (EditText) findViewById(R.id.qtEditText);
        qrsEditText = (EditText) findViewById(R.id.qrsEditText);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);


        getPrefs();
        setAdapters();
        clearEntries();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentActivityIntent = new Intent(this, CalculatorList.class);
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
        switch (v.getId()) {
            case R.id.calculate_qtc_button:
                calculateQtc();
                break;
            case R.id.clear_button:
                clearEntries();
                break;
        }
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.interval_rate_labels,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalRateSpinner.setAdapter(adapter);
        if (defaultIntervalRateSelection.equals(IntervalRate.INTERVAL))
            intervalRateSpinner.setSelection(INTERVAL_SELECTION);
        else
            intervalRateSpinner.setSelection(RATE_SELECTION);
        itemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateIntervalRateSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };

        intervalRateSpinner.setOnItemSelectedListener(itemListener);
    }

    private void updateIntervalRateSelection() {
        IntervalRate intervalRateSelection = getIntervalRateSelection();
        if (intervalRateSelection.equals(IntervalRate.INTERVAL))
            rrEditText.setHint(getString(R.string.rr_hint));
        else
            rrEditText.setHint(getString(R.string.hr_hint));
    }

    private IntervalRate getIntervalRateSelection() {
        String result = intervalRateSpinner.getSelectedItem().toString();
        if (result.startsWith("RR"))
            return IntervalRate.INTERVAL;
        else
            return IntervalRate.RATE;

    }

    private void calculateQtc() {
        CharSequence rateIntervalText = rrEditText.getText();
        CharSequence qtText = qtEditText.getText();
        CharSequence qrsText = qrsEditText.getText();
        IntervalRate intervalRateSelection = getIntervalRateSelection();
        try {
            int interval;
            int rate;
            int rateInterval = Integer.parseInt(rateIntervalText.toString());

            if (intervalRateSelection.equals(IntervalRate.RATE)) {
                interval = (int) Math.round(60000.0 / rateInterval);
                rate = rateInterval;
            }
            else {
                interval = rateInterval;
                rate = (int) Math.round(60000.0 / rateInterval);
            }
            int qt = Integer.parseInt(qtText.toString());
            int qrs = Integer.parseInt(qrsText.toString());
            if (rateInterval <= 0 || qt <= 0 || qrs <= 00 || qrs >= qt) {
                throw new NumberFormatException();
            }
            QtcCalculator.QtcFormula formula = QtcCalculator.QtcFormula.BAZETT;
            int qtc = QtcCalculator.calculate(interval, qt, formula);
            // TODO: other QtcFormula calculations here
            // TODO: display results
            startActivity(new Intent(this, QtcIvcdResults.class));
        } catch (NumberFormatException e) {
            // TODO diplay error message
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(getString(R.string.error_dialog_title));
            alert.setMessage(getString(R.string.qt_calculator_error));
            alert.show();;
        }
    }

    private void clearEntries() {
        rrEditText.setText(null);
        qtEditText.setText(null);
        qrsEditText.setText(null);
        rrEditText.requestFocus();
    }

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String intervalRatePreference = prefs.getString("interval_rate",
                "INTERVAL");
        if (intervalRatePreference.equals("INTERVAL"))
            defaultIntervalRateSelection = IntervalRate.INTERVAL;
        else
            defaultIntervalRateSelection = IntervalRate.RATE;
    }

}
