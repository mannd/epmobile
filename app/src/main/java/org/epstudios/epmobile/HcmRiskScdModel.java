package org.epstudios.epmobile;

import android.content.Context;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/19/24.
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
public class HcmRiskScdModel {
    HcmRiskScdModel(Context context, String ageString, String maxLvWallThicknessString, String maxLvotGradientString,
                    String laSizeString, boolean hasFamilyHxScd, boolean hasNsvt, boolean hasSyncope) {
        this.context = context;
        this.ageString = ageString;
        this.maxLvWallThicknessString = maxLvWallThicknessString;
        this.maxLvotGradientString = maxLvotGradientString;
        this.laSizeString = laSizeString;
        this.hasFamilyHxScd = hasFamilyHxScd;
        this.hasNsvt = hasNsvt;
        this.hasSyncope = hasSyncope;
    }

    Context context;
    private String ageString;
    private String maxLvWallThicknessString;
    private String maxLvotGradientString;
    private String laSizeString;
    private boolean hasFamilyHxScd;
    private boolean hasNsvt;
    private boolean hasSyncope;

    double calculateResult() throws IllegalArgumentException {
            int age = Integer.parseInt(ageString);
            int maxLvWallThickness = Integer.parseInt(maxLvWallThicknessString);
            int maxLvotGradient = Integer.parseInt(maxLvotGradientString);
            int laDiameter = Integer.parseInt(laSizeString);
            if (age > 115 || age < 16) {
                throw new IllegalArgumentException(context.getString((R.string.invalid_age_message)));
            }
            if (maxLvWallThickness < 10 || maxLvWallThickness > 35) {
                throw new IllegalArgumentException(context.getString(R.string.invalid_thickness_message));
            }
            if (maxLvotGradient < 2 || maxLvotGradient > 154) {
                throw new IllegalArgumentException(context.getString(R.string.invalid_gradient_message));
            }
            if (laDiameter < 28 || laDiameter > 67) {
                throw new IllegalArgumentException(context.getString(R.string.invalid_diameter_message));
            }
            final double coefficient = 0.998;
            double prognosticIndex = 0.15939858 * maxLvWallThickness
                    - 0.00294271 * maxLvWallThickness * maxLvWallThickness
                    + 0.0259082 * laDiameter
                    + 0.00446131 * maxLvotGradient
                    + (hasFamilyHxScd ? 0.4583082 : 0.0)
                    + (hasNsvt ? 0.82639195 : 0.0)
                    + (hasSyncope ? 0.71650361 : 0.0)
                    - 0.01799934 * age;
            return 1 - Math.pow(coefficient, Math.exp(prognosticIndex));
    }
}