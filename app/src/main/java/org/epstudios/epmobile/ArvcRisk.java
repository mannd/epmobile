package org.epstudios.epmobile;

/**
 * Copyright (C) 2019 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 4/16/19.
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
public class ArvcRisk extends DiagnosticScore {
    //@Override
    protected String getFullReference() {
        return getString(R.string.arvc_risk_full_reference);
    }

    //@Override
    protected String getRiskLabel() {
        return getString(R.string.arvc_risk_title);
    }

    //@Override
    protected String getShortReference() {
        return getString(R.string.arvc_risk_short_reference);
    }

    @Override
    protected void calculateResult() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.arvcrisk);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void clearEntries() {

    }
}
