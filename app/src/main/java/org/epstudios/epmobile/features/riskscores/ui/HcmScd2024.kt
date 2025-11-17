package org.epstudios.epmobile.features.riskscores.ui

import org.epstudios.epmobile.R
import org.epstudios.epmobile.Reference
import org.epstudios.epmobile.RiskScore

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 *
 *
 * Created by mannd on 11/11/24.
 *
 *
 * This file is part of epmobile.
 *
 *
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
class HcmScd2024 : RiskScore() {
    private enum class Recommendation {
        class1,
        class2a,
        class2b,
        class3
    }

    override val fullReference: String?
        get() {
            val reference5 = convertReferenceToText(
                R.string.hcm_scd_reference_5,
                R.string.hcm_scd_link_5
            )
            val reference6 = convertReferenceToText(
                R.string.hcm_scd_reference_6,
                R.string.hcm_scd_link_6
            )
            val reference7 = convertReferenceToText(
                R.string.hcm_scd_reference_7,
                R.string.hcm_scd_link_7
            )
            val reference4 = convertReferenceToText(
                R.string.hcm_scd_reference_4,
                R.string.hcm_scd_link_4
            )
            return (reference5 + "\n" + reference6 + "\n" + reference7
                    + "\n" + reference4)
        }

    override fun showActivityReference() {
        val references = arrayOf(
            Reference(this, R.string.hcm_scd_reference_5, R.string.hcm_scd_link_5),
            Reference(this, R.string.hcm_scd_reference_6, R.string.hcm_scd_link_6),
            Reference(this, R.string.hcm_scd_reference_7, R.string.hcm_scd_link_7),
            Reference(this, R.string.hcm_scd_reference_4, R.string.hcm_scd_link_4)
        )
        showReferenceAlertDialog(references)
    }

    override fun showActivityInstructions() {
        showAlertDialog(R.string.hcm_scd_2024_title, R.string.hcm_scd_2024_instructions)
    }

    override val riskLabel: String?
        get() = getString(R.string.hcm_scd_2024_title)

    override fun calculateResult() {
        val recommendation = calculateRecommendation()
        val message: String?
        when (recommendation) {
            Recommendation.class2a -> message = "ICD is reasonable (Class 2a)"
            Recommendation.class2b -> message = "ICD may be considered (Class 2b)"
            Recommendation.class3 -> message = "ICD is not recommended (Class 3)"
            Recommendation.class1 -> message = "ERROR"
        }
        addRisks()
        displayResult(message, getString(R.string.hcm_scd_2024_title))
    }

    private fun addRisks() {
        val familyHxScd = checkBoxes!![0].isChecked()
        val massiveLvh = checkBoxes!![1].isChecked()
        val hasSyncope = checkBoxes!![2].isChecked()
        val apicalAneurysm = checkBoxes!![3].isChecked()
        val lowLvef = checkBoxes!![4].isChecked()
        val hxNSVT = checkBoxes!![5].isChecked()
        val extensiveLge = checkBoxes!![6].isChecked()
        clearSelectedRisks()
        addSelectedRisks()
    }

    private fun calculateRecommendation(): Recommendation {
        var recommendation = Recommendation.class3
        for (i in 0..4) {
            if (checkBoxes!![i].isChecked()) {
                recommendation = Recommendation.class2a
                return recommendation
            }
        }
        if (checkBoxes!![5].isChecked() || checkBoxes!![6].isChecked()) {
            recommendation = Recommendation.class2b
            return recommendation
        }
        return recommendation
    }

    override fun setContentView() {
        setContentView(R.layout.hcmscd2024)
    }

    override fun setupInsets() {
        setupInsets(R.id.hcmscd2024_root_view)
    }

    override fun init() {
        checkBoxes = arrayOf(
            findViewById(R.id.family_hx_scd),
            findViewById(R.id.massive_lvh),
            findViewById(R.id.unexplained_syncope),
            findViewById(R.id.apical_aneurysm),
            findViewById(R.id.low_lvef),
            findViewById(R.id.nsvt),
            findViewById(R.id.extensive_lge)
        )
    }

    override fun hideReferenceMenuItem(): Boolean {
        return false
    }

    override fun hideKeyMenuItem(): Boolean {
        return false
    }

    override fun hideInstructionsMenuItem(): Boolean {
        return false
    }

    override fun showActivityKey() {
        showKeyAlertDialog(R.string.hcm_scd_2024_key)
    }
}
