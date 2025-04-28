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
package org.epstudios.epmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class EpMobile : EpActivity(), View.OnClickListener {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mainscreen)
        setupInsets(R.id.main_root_view)

        val calculatorButton = findViewById<Button>(R.id.calculators_button)
        val diagnosisButton = findViewById<Button>(R.id.diagnosis_button)
        val referenceButton = findViewById<Button>(R.id.reference_button)
        val riskScoresButton = findViewById<Button>(R.id.risk_scores_button)

        calculatorButton.setOnClickListener(this)
        diagnosisButton.setOnClickListener(this)
        referenceButton.setOnClickListener(this)
        riskScoresButton.setOnClickListener(this)

        initToolbar()
        // only EP Mobile main screen has no back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun calculatorList() {
        val i = Intent(this, CalculatorList::class.java)
        startActivity(i)
    }

    private fun diagnosisList() {
        val i = Intent(this, DiagnosisList::class.java)
        startActivity(i)
    }

    private fun riskScores() {
        val i = Intent(this, RiskScoreList::class.java)
        startActivity(i)
    }

    private fun referenceList() {
        val i = Intent(this, ReferenceList::class.java)
        startActivity(i)
    }

    override fun onClick(v: View) {
        val id = v.getId()
        if (id == R.id.calculators_button) {
            calculatorList()
        } else if (id == R.id.diagnosis_button) {
            diagnosisList()
        } else if (id == R.id.reference_button) {
            referenceList()
        } else if (id == R.id.risk_scores_button) {
            riskScores()
        }
    }
}