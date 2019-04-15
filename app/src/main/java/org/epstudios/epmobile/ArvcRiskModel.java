package org.epstudios.epmobile;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Copyright (C) 2019 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 4/15/19.
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
public class ArvcRiskModel {
    public static final double year5 = 0.800813822845434;
    public static final double year4 = 0.837312364505388;
    public static final double year3 = 0.849912331481654;
    public static final double year2 = 0.875734032965286;
    public static final double year1 = 0.921429983419349;

    private int sex;
    private int age;
    private int hxSyncope;
    private int hxNSVT;
    private int pvcCount;
    private int twiCount;
    private int rvef;

    ArvcRiskModel(int sex, int age, int hxSyncope, int hxNSVT, int pvcCount,
                  int twiCount, int rvef) {
        this.sex = sex;
        this.age = age;
        this.hxSyncope = hxSyncope;
        this.hxNSVT = hxNSVT;
        this.pvcCount = pvcCount;
        this.twiCount = twiCount;
        this.rvef = rvef;
    }

    // linear predictor
    private double linearPredictor() {
        return 0.4879 * sex
                - 0.0218 * age
                + 0.6573 * hxSyncope
                + 0.8112 * hxNSVT
                + 0.1701 * (pvcCount > 0 ? log(pvcCount) : 0)
                + 0.1131 * twiCount
                - 0.0252 * rvef;
    }

    public double calculateRisk(double baselineSurvival) {
        return calculateRisk(baselineSurvival, linearPredictor());
    }

    public double calculateRisk(double baselineSurvival, double linearPredictor) {
        return roundToOnePlace( 100 * (1.0 - pow(baselineSurvival, exp(linearPredictor))));
    }

    private double roundToOnePlace(double value) {
        return round(value * 10.0) / 10.0;
    }

}

