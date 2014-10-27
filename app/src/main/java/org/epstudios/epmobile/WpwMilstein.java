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

public class WpwMilstein extends WpwArruda {
	@Override
	protected void step1() {
		stepTextView.setText(getString(R.string.milstein_step_1));
		backButton.setEnabled(false);
	}

	@Override
	protected void getYesResult() {
		adjustStepsForward();
		switch (step) {
		case 1:
			step = 2;
			break;
		case 2:
			step = 8;
			break;
		case 3:
			step = 4;
			break;
		case 4:
			step = 10;
			break;
		case 5:
			step = 6;
			break;
		case 6:
			step = 12;
			break;
		case 7:
			step = 14;
			break;
		}
		gotoStep();
	}

	@Override
	protected void getNoResult() {
		adjustStepsForward();
		switch (step) {
		case 1:
			step = 3;
			break;
		case 2:
			step = 9;
			break;
		case 3:
			step = 5;
			break;
		case 4:
			step = 11;
			break;
		case 5:
			step = 7;
			break;
		case 6:
			step = 13;
			break;
		case 7:
			step = 15;
			break;
		}
		gotoStep();
	}

	protected void gotoStep() {
		switch (step) {
		case 1:
			step1();
			break;
		case 2:
		case 5:
			stepTextView.setText(getString(R.string.milstein_step_2_5));
			break;
		case 3:
			stepTextView.setText(getString(R.string.milstein_step_3));
			break;
		case 4:
			stepTextView.setText(getString(R.string.milstein_step_4));
			break;
		case 6:
			stepTextView.setText(getString(R.string.milstein_step_6));
			break;
		case 7:
			stepTextView.setText(getString(R.string.milstein_step_7));
			break;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			showResult();
			break;
		}
		if (step != 1)
			backButton.setEnabled(true);
	}

	protected void setMessageAndLocation() {
		switch (step) {
		case 8:
		case 12:
			message += getString(R.string.as_location);
			location1 = AS;
			break;
		case 9:
		case 14:
			message += getString(R.string.ll_location);
			location1 = LL;
			break;
		case 10:
			message += getString(R.string.psta_psma_location);
			location1 = PSTA;
			location2 = PSMA;
			break;
		case 11:
		case 13:
			message += getString(R.string.rl_location);
			location1 = RL;
			break;
		case 15:
			message += getString(R.string.undetermined_location);
			break;
		}
	}

}
