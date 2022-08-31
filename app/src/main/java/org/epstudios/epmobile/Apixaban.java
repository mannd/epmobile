/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2012 EP Studios, Inc.
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

package org.epstudios.epmobile;

public class Apixaban extends DrugCalculator {

    @Override
    protected int getDose(int crCl) {
        // note only warning now if CrCl < 15, apixaban not prohibited
        return USE_APIXABAN_DOSING;
    }

    @Override
    protected String getDisclaimer() {
        return getString(R.string.af_drug_dose_disclaimer) + super.getDisclaimer();
    }
}
