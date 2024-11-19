package org.epstudios.epmobile;

import android.widget.CheckBox;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/11/24.
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
public class HcmScd2022 extends HcmRiskScd {
    @Override
    protected String getFullReference() {
        String reference2 = convertReferenceToText(R.string.hcm_scd_reference_2,
                R.string.hcm_scd_link_2);
        String reference3 = convertReferenceToText(R.string.hcm_scd_reference_3,
                R.string.hcm_scd_link_3);
        String reference4 = convertReferenceToText(R.string.hcm_scd_reference_4,
                R.string.hcm_scd_link_4);
        String fullReference = reference2 + "\n" + reference3 + "\n" + reference4;
        return fullReference;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[3];
        references[0] = new Reference(this, R.string.hcm_scd_reference_2, R.string.hcm_scd_link_2);
        references[1] = new Reference(this, R.string.hcm_scd_reference_3, R.string.hcm_scd_link_3);
        references[2] = new Reference(this, R.string.hcm_scd_reference_4, R.string.hcm_scd_link_4);
        showReferenceAlertDialog(references);
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.hcm_scd_2022_title, R.string.hcm_scd_2022_instructions);
    }

    @Override
    protected String getRiskLabel() {
        return getString(R.string.hcm_scd_2022_title);
    }

    @Override
    protected void calculateResult() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.hcmscd2022);
    }

    @Override
    protected void init() {
        super.init();
    }
}
