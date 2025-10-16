package org.epstudios.epmobile;

import org.jetbrains.annotations.Nullable;

/**
 * Copyright (C) 2025 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 10/16/25.
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
 * along with epmobile.  If not, see <<a href="http://www.gnu.org/licenses/">...</a>>.
 */
public class Painesd extends RiskScore {
    @Override
    @Nullable
    protected String getFullReference() {
        return convertReferenceToText(R.string.painesd_reference,
                R.string.painesd_link);
    }

    @Override
    @Nullable
    protected String getRiskLabel() {
        return getString(R.string.painesd_label);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.painesd_risk_title,
                R.string.painesd_instructions);
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return true;
    }

    @Override
    protected void calculateResult() {

    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.painesd_reference,
                R.string.painesd_link);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.painesd);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.painesd_root_view);
    }

    @Override
    protected void init() {

    }
}
