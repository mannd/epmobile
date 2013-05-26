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

import android.graphics.Color;

public class Dabigatran extends DrugCalculator {

	@Override
	protected String getMessage(int crCl, double age) {
		String msg = super.getMessage(crCl, age);
		if ((crCl >= 15) && (crCl <= 30)) {
			msg += "\n" + getString(R.string.dabigatran_warning_severe);
			ccTextView.setTextColor(Color.parseColor("#ffa500"));
		} else if ((crCl > 30) && (crCl <= 50)) {
			msg += "\n" + getString(R.string.dabigatran_warning_mild);
			ccTextView.setTextColor(Color.YELLOW);
		} else
			ccTextView.setTextColor(Color.WHITE);
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

}
