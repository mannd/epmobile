/*
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

package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.epstudios.epmobile.QtcCalculator.QtcFormula;

@SuppressWarnings("SpellCheckingInspection")
public class QtcIvcd extends EpActivity implements View.OnClickListener {
    private enum IntervalRate {
        INTERVAL, RATE
    }

    private RadioGroup sexRadioGroup;
    private Spinner intervalRateSpinner;
    private EditText rrEditText;
    private EditText qtEditText;
    private EditText qrsEditText;
    private CheckBox lbbbCheckBox;
    private Spinner qtcFormulaSpinner;

    private final static int INTERVAL_SELECTION = 0;
    private final static int RATE_SELECTION = 1;

    private final static int BAZETT_FORMULA = 0;
    private final static int FRIDERICIA_FORMULA = 1;
    private final static int SAGIE_FORMULA = 2;
    private final static int HODGES_FORMULA = 3;

    private String qtcFormula;

    private IntervalRate defaultIntervalRateSelection = IntervalRate.INTERVAL;

    private AdapterView.OnItemSelectedListener itemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qtcivcd);
        initToolbar();

        View calculateQtcButton = findViewById(R.id.calculate_button);
        calculateQtcButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        intervalRateSpinner = findViewById(R.id.interval_rate_spinner);
        rrEditText = findViewById(R.id.rrEditText);
        qtEditText = findViewById(R.id.qtEditText);
        qrsEditText = findViewById(R.id.qrsEditText);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        lbbbCheckBox = findViewById(R.id.lbbbCheckBox);
        qtcFormulaSpinner = findViewById(R.id.qtc_formula_spinner);

        getPrefs();
        setAdapters();
        setFormulaAdapters();
        clearEntries();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateQtc();
        }
        else if (id == R.id.clear_button) {
            clearEntries();
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
        AdapterView.OnItemSelectedListener itemListener = new AdapterView.OnItemSelectedListener() {
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

    private void setFormulaAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.formula_names,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtcFormulaSpinner.setAdapter(adapter);
        int formula = BAZETT_FORMULA;
        QtcCalculator.QtcFormula f = getQtcFormula(qtcFormula);
        switch (f) {
            case BAZETT:
                // already initialized to BAZETT_FORMULA
                break;
            case FRIDERICIA:
                formula = FRIDERICIA_FORMULA;
                break;
            case SAGIE:
                formula = SAGIE_FORMULA;
                break;
            case HODGES:
                formula = HODGES_FORMULA;
                break;
        }
        qtcFormulaSpinner.setSelection(formula);
        itemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateQtcFormula();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };

        qtcFormulaSpinner.setOnItemSelectedListener(itemListener);
    }

    private QtcCalculator.QtcFormula getQtcFormula(String name) {
        switch (name) {
            case "FRIDERICIA":
                return QtcCalculator.QtcFormula.FRIDERICIA;
            case "SAGIE":
                return QtcCalculator.QtcFormula.SAGIE;
            case "HODGES":
                return QtcCalculator.QtcFormula.HODGES;
            case "BAZETT":
            default:
                return QtcCalculator.QtcFormula.BAZETT;
        }
    }

    private void updateIntervalRateSelection() {
        IntervalRate intervalRateSelection = getIntervalRateSelection();
        if (intervalRateSelection.equals(IntervalRate.INTERVAL))
            rrEditText.setHint(getString(R.string.rr_hint));
        else
            rrEditText.setHint(getString(R.string.hr_hint));
    }

    private void updateQtcFormula() {
        int result = qtcFormulaSpinner.getSelectedItemPosition();
        switch (result) {
            case BAZETT_FORMULA:
                qtcFormula = "BAZETT";
                break;
            case FRIDERICIA_FORMULA:
                qtcFormula = "FRIDERICIA";
                break;
            case SAGIE_FORMULA:
                qtcFormula = "SAGIE";
                break;
            case HODGES_FORMULA:
                qtcFormula = "HODGES";
                break;
        }
    }

    private IntervalRate getIntervalRateSelection() {
        String result = intervalRateSpinner.getSelectedItem().toString();
        if (result.startsWith("RR"))
            return IntervalRate.INTERVAL;
        else
            return IntervalRate.RATE;
    }

    private static class ShortQrsException extends Exception {}

    private void calculateQtc() {
        CharSequence rateIntervalText = rrEditText.getText();
        CharSequence qtText = qtEditText.getText();
        CharSequence qrsText = qrsEditText.getText();
        IntervalRate intervalRateSelection = getIntervalRateSelection();
        boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
        boolean isLBBB = lbbbCheckBox.isChecked();
        try {
            int interval;
            double rate;
            int rateInterval = Integer.parseInt(rateIntervalText.toString());

            if (intervalRateSelection.equals(IntervalRate.RATE)) {
                interval = (int) Math.round(60000.0 / rateInterval);
                rate = rateInterval;
            }
            else {
                interval = rateInterval;
                rate = Math.round(60000.0 / rateInterval);
            }
            int qt = Integer.parseInt(qtText.toString());
            int qrs = Integer.parseInt(qrsText.toString());
            //noinspection ConditionCoveredByFurtherCondition
            if (rateInterval <= 0 || qt <= 0 || qrs <= 0 || qrs >= qt) {
                throw new NumberFormatException();
            }
            else if (qrs < 120) {
                throw new ShortQrsException();
            }
            QtcCalculator.QtcFormula formula = getQtcFormula(qtcFormula);
            int qtc = QtcCalculator.calculate(interval, qt, formula);
            int jt = (int) QtcCalculator.jtInterval(qt, qrs);
            int jtc = qtc - qrs;
            // We no longer use BAZETT by default in calculating JTc.
            //int jtc = QtcCalculator.jtCorrected(qt, interval, qrs);
            int qtm = (int) QtcCalculator.qtCorrectedForLBBB(qt, qrs);
            int qtmc = QtcCalculator.calculate(interval, qtm, formula);
            int qtrrqrs = (int) QtcCalculator.qtRrIvcd(qt, rate, qrs, isMale);
            int preLbbbQtc = (int) QtcCalculator.preLbbbQtc(qt, interval, qrs, isMale, formula);

            Intent intent = new Intent(this, QtcIvcdResults.class);
            intent.putExtra("isLBBB", isLBBB);
            intent.putExtra("isMale", isMale);
            intent.putExtra("QT", qt);
            intent.putExtra("QTc", qtc);
            intent.putExtra("JT", jt);
            intent.putExtra("JTc", jtc);
            intent.putExtra("QTm", qtm);
            intent.putExtra("QTmc", qtmc);
            intent.putExtra("QTrrqrs", qtrrqrs);
            intent.putExtra("preLbbbQtc", preLbbbQtc);
            intent.putExtra("QTcFormula", formula.name());
            startActivity(intent);
        } catch (NumberFormatException e) {
            showError(getString(R.string.qt_calculator_error));
        } catch (ShortQrsException e) {
            showError(getString(R.string.short_qrs_error));
        }

    }

    private void showError(String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.error_dialog_title));
        alert.setMessage(message);
        alert.show();
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
        qtcFormula = prefs.getString("qtc_formula", "BAZETT");
        String intervalRatePreference = prefs.getString("interval_rate",
                "INTERVAL");
        if (intervalRatePreference != null){
            if (intervalRatePreference.equals("INTERVAL"))
                defaultIntervalRateSelection = IntervalRate.INTERVAL;
            else
                defaultIntervalRateSelection = IntervalRate.RATE;
        }
    }

}
