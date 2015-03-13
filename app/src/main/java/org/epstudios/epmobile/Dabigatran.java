/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2011 EP Studios, Inc.
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

public class Dabigatran extends DrugCalculator {
	// / TODO need to fix these colors for both themes
	@Override
	protected String getMessage(int crCl, double age) {
		String msg = super.getMessage(crCl, age);
		if ((crCl >= 15) && (crCl <= 30)) {
			msg += "\n" + getString(R.string.dabigatran_warning_severe);
			ccTextView.setTextColor(getResources().getColor(R.color.sienna_2));
		} else if ((crCl > 30) && (crCl <= 50)) {
			msg += "\n" + getString(R.string.dabigatran_warning_mild);
			ccTextView.setTextColor(getResources().getColor(
					R.color.dark_goldenrod_1));
		} else
			ccTextView.setTextAppearance(this,
					android.R.style.TextAppearance_Medium);
		if (age >= 75.0 && crCl >= 15) // don't bother with age warning if drug
										// shouldn't be used
			msg += "\n" + getString(R.string.dabigatran_warning_75_years_old);
		return msg;
	}

	@Override
	protected int getDose(int crClr) {
		if (crClr > 30)
			return 150;
		if (crClr >= 15)
			return 75;
		return 0;
	}

    @Override
    protected String getDisclaimer() {
        return getString(R.string.af_drug_dose_disclaimer) + super.getDisclaimer();
    }

}
