package org.epstudios.epmobile;

/**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 8/25/22.
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
public class Gfr {
    public static double ckdEpiGfr(double cr, int age, boolean isMale, boolean isBlack) {
        //  - GFR = 141 * min(Scr/kappa,1)^alpha  * max(Scr/kappa,1)^-1.209
        //  * 0.993^Age * 1.018 [if female] * 1.159 [if black],
        //  where Scr is serum creatinine, kappa is 0.7 for females and 0.9 for males,
        //  alpha is -0.329 for females and -0.411 for males, min indicates the minimum of Scr/kappa or 1,
        //  and max indicates the maximum of Scr/kappa or 1.
        double kappa;
        kappa = isMale ? 0.9 : 0.7;
        double alpha;
        alpha = isMale ? -0.411 : -0.329;
        double minCr = Math.min(cr / kappa, 1.0);
        double maxCr = Math.max(cr / kappa, 1.0);
        double gfr = 141.0 * Math.pow(minCr, alpha) * Math.pow(maxCr, -1.209)
                * Math.pow(0.993, age);
        if (!isMale) {
           gfr *= 1.018;
        }
        if (isBlack) {
            gfr *= 1.159;
        }
        return gfr;
    }
}
