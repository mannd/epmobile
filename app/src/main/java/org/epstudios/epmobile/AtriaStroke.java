package org.epstudios.epmobile;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 11/29/15.
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
public class AtriaStroke extends RiskScore {

    private boolean hasStrokeHx;
    
    @Override
    protected String getFullReference() {
        return getString(R.string.atria_stroke_full_reference);
    }

    @Override
    protected String getRiskLabel() {
        return null;
    }

    @Override
    protected String getShortReference() {
        return getString(R.string.atria_stroke_short_reference);
    }

    @Override
    protected void calculateResult() {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void init() {

    }
}
