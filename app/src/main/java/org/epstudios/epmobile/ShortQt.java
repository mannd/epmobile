/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ShortQt extends RiskScore implements OnClickListener {

    private RadioGroup qtcRadioGroup;
    private CheckBox shortJtCheckBox;
    private CheckBox suddenCardiacArrestCheckBox;
    private CheckBox polymorphicVtCheckBox;
    private CheckBox unexplainedSyncopeCheckBox;
    private CheckBox afbCheckBox;
    private CheckBox relativeWithSqtsCheckBox;
    private CheckBox relativeWithSdCheckBox;
    private CheckBox sidsCheckBox;
    private CheckBox genotypePositiveCheckBox;
    private CheckBox mutationCheckBox;

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    protected void calculateResult() {
        clearSelectedRisks();
        int score = 0;
        // ECG criteria
        // one of the short QT intervals must be selected to get other points
        int selectedQTcId = qtcRadioGroup.getCheckedRadioButtonId();
        if (selectedQTcId == -1
                && !shortJtCheckBox.isChecked()) {
            displayResult(score);
            return;
        }
        if (selectedQTcId == R.id.short_qt)
            score++;
        else if (selectedQTcId == R.id.shorter_qt)
            score += 2;
        else if (selectedQTcId == R.id.shortest_qt)
            score += 3;
        RadioButton qtcRadioButton = (RadioButton) findViewById(selectedQTcId);
        if (qtcRadioButton != null) {
            addSelectedRisk("QTc " + qtcRadioButton.getText().toString());
        }
        // Short JT is very specific for SQTS
        if (shortJtCheckBox.isChecked()) {
            score++;
            addSelectedRisk(shortJtCheckBox.getText().toString());
        }
        // Clinical history
        // points can be received for only one of the next 3 selections
        if (suddenCardiacArrestCheckBox.isChecked()) {
            score += 2;
            addSelectedRisk(suddenCardiacArrestCheckBox.getText().toString());
        }
        else if (polymorphicVtCheckBox.isChecked()) {
            score += 2;
            addSelectedRisk(polymorphicVtCheckBox.getText().toString());
        }
        else if (unexplainedSyncopeCheckBox.isChecked()) {
            score++;
            addSelectedRisk(unexplainedSyncopeCheckBox.getText().toString());
        }
        if (afbCheckBox.isChecked()) {
            score++;
            addSelectedRisk(afbCheckBox.getText().toString());
        }
        // Family history
        // points can be received only once in this section
        if (relativeWithSqtsCheckBox.isChecked()) {
            score += 2;
            addSelectedRisk(relativeWithSqtsCheckBox.getText().toString());
        }
        else if (relativeWithSdCheckBox.isChecked()) {
            score++;
            addSelectedRisk(relativeWithSdCheckBox.getText().toString());
        }
        else if (sidsCheckBox.isChecked()) {
            score++;
            addSelectedRisk(sidsCheckBox.getText().toString());
        }
        // Genotype
        if (genotypePositiveCheckBox.isChecked()) {
            score += 2;
            addSelectedRisk(genotypePositiveCheckBox.getText().toString());
        }
        if (mutationCheckBox.isChecked()) {
            score++;
            addSelectedRisk(mutationCheckBox.getText().toString());
        }

        displayResult(score);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.shortqt);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.short_qt_root_view);
    }

    @Override
    protected void init() {
        qtcRadioGroup = findViewById(R.id.qtc_radio_group);
        shortJtCheckBox = findViewById(R.id.short_jt);
        suddenCardiacArrestCheckBox = findViewById(R.id.sudden_cardiac_arrest);
        polymorphicVtCheckBox = findViewById(R.id.polymorphic_vt);
        unexplainedSyncopeCheckBox = findViewById(R.id.unexplained_syncope);
        afbCheckBox = findViewById(R.id.afb);
        relativeWithSqtsCheckBox = findViewById(R.id.relative_with_sqts);
        relativeWithSdCheckBox = findViewById(R.id.relative_with_sd);
        sidsCheckBox = findViewById(R.id.sids);
        genotypePositiveCheckBox = findViewById(R.id.genotype_positive);
        mutationCheckBox = findViewById(R.id.mutation);
    }

    private void displayResult(int score) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        String message = "Score = " + score + "\n";
        if (score >= 4)
            message += "High probability";
        else if (score == 3)
            message += "Intermediate probability";
        else
            message += "Low probability";
        message += " of Short QT Syndrome";
        resultMessage = message;
        super.displayResult(message, getString(R.string.short_qt_title));
    }

    protected void clearEntries() {
        qtcRadioGroup.clearCheck();
        shortJtCheckBox.setChecked(false);
        suddenCardiacArrestCheckBox.setChecked(false);
        polymorphicVtCheckBox.setChecked(false);
        unexplainedSyncopeCheckBox.setChecked(false);
        afbCheckBox.setChecked(false);
        relativeWithSqtsCheckBox.setChecked(false);
        relativeWithSdCheckBox.setChecked(false);
        sidsCheckBox.setChecked(false);
        genotypePositiveCheckBox.setChecked(false);
        mutationCheckBox.setChecked(false);
    }

    @Override
    protected String getFullReference() {
        return convertReferenceToText(R.string.sqts_reference,
                R.string.sqts_link);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.short_qt_title);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.sqts_reference,
                R.string.sqts_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.short_qt_title,
                R.string.sqts_instructions);
    }
}
