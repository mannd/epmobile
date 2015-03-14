package org.epstudios.epmobile;

/**
 * Copyright (C) 2013, 2014 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 1/16/15.
 * <p/>
 * This file is part of EP Mobile.
 * <p/>
 * EP Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Edoxaban extends DrugCalculator {
    @Override
    protected int getDose(int crClr) {
        if (crClr > 95 || crClr < 15)
            return 0;
        if (crClr > 50)
            return 60;
        if (crClr >= 15)
            return 30;
        return 0;
    }

    @Override
    protected String doseFrequency(int crCl) {
        return " mg daily";
    }

    @Override
    protected String getMessage(int crCl, double age) {
        String msg = super.getMessage(crCl, age);
        if (crCl > 95) {
            msg += "\nEdoxaban should not be used in patients with CrCl > 95 mL/min";
        }
        return msg;
    }

    @Override
    protected String getDisclaimer() {
        return getString(R.string.af_drug_dose_disclaimer) + super.getDisclaimer();
    }
}


