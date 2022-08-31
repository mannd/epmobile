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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EpMobile extends EpActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainscreen);
        Button calculatorButton = findViewById(R.id.calculators_button);
        Button diagnosisButton = findViewById(R.id.diagnosis_button);
        Button referenceButton = findViewById(R.id.reference_button);
        Button riskScoresButton = findViewById(R.id.risk_scores_button);

        calculatorButton.setOnClickListener(this);
        diagnosisButton.setOnClickListener(this);
        referenceButton.setOnClickListener(this);
        riskScoresButton.setOnClickListener(this);

        initToolbar();
        // only EP Mobile main screen has no back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void calculatorList() {
        Intent i = new Intent(this, CalculatorList.class);
        startActivity(i);
    }

    private void diagnosisList() {
        Intent i = new Intent(this, DiagnosisList.class);
        startActivity(i);
    }

    private void riskScores() {
        Intent i = new Intent(this, RiskScoreList.class);
        startActivity(i);
    }

    private void referenceList() {
        Intent i = new Intent(this, ReferenceList.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculators_button) {
            calculatorList();
        } else if (id == R.id.diagnosis_button) {
            diagnosisList();
        } else if (id == R.id.reference_button) {
            referenceList();
        } else if (id == R.id.risk_scores_button) {
            riskScores();
        }
    }
}