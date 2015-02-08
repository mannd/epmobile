/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2015 EP Studios, Inc.
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

public class DavilaAlgorithm extends WpwArruda {

    @Override
    protected void step1() {
        stepTextView.setText(getString(R.string.davila_step_1));
        backButton.setEnabled(false);
    }

    @Override
    protected void getYesResult() {
        adjustStepsForward();
        switch (step) {
            case 1:
                step = 2;  // III +
                break;
            case 2:
                step = 8; // LL
                break;
            case 3:
                step = 9; // LP
                break;
            case 10:
                step = 7; //  AS
                break;
            case 5:
                step = 6;  // aVL +?
                break;
            case 6:
                step = 7;  // AS
                break;
            case 7:
                step = 14;
                break;
            case 11:
                step = 12; // MS
                break;
            case 13:
                step = 14; // V2 + ?
                break;
            case 14:
                step = 15; // RPS
                break;
            case 16:
                step = 17; // RL
                break;
            case 17:
                step = 19; // PS
                break;
        }
        gotoStep();
    }

    @Override
    protected void getNoResult() {
        adjustStepsForward();
        switch (step) {
            case 1:
                step = 5;  // III + ?
                break;
            case 2:
                step = 3;   // III +/- ?
                break;
            case 3:
                step = 4;
                break;
            case 4:
                step = 11;
                break;
            case 5:
                step = 10;
                break;
            case 6:
                step = 8;  // LL
                break;
            case 10:
                step = 11;
                break;
            case 11:
                step = 13; // II + ?
                break;
            case 13:
                step = 17; // II -
                break;
            case 14:
                step = 16; //RL
                break;
            case 17:
                step = 18; // RL
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
                stepTextView.setText(getString(R.string.davila_positive_iii));
                break;

            case 3:
            case 10:
                stepTextView.setText(getString(R.string.davila_plus_minus_iii));
                break;
            case 6:
                stepTextView.setText(getString(R.string.davila_positive_avl));
                break;
            case 11:
                stepTextView.setText(getString(R.string.davila_qrs_pattern));
                break;
            case 13:
                stepTextView.setText(getString(R.string.davila_positive_ii));
                break;
            case 14:
            case 17:
                stepTextView.setText(getString(R.string.davila_positive_v2));
                break;
            case 4:
            case 7:
            case 8:
            case 9:
            case 12:
            case 15:
            case 16:
            case 18:
            case 19:
                showResult();
                break;
        }
        if (step != 1)
            backButton.setEnabled(true);
    }

    protected void setMessageAndLocation() {
        switch (step) {
            case 4:
                message += getString(R.string.psma_location);
                location1 = PSMA;
                break;
            case 7:
                message += getString(R.string.as_location);
                location1 = AS;
                break;
            case 8:
                message += getString(R.string.ll_location);
                location1 = LL;
                break;
            case 9:
                message += getString(R.string.lp_location);
                location1 = LP;
                break;
            case 12:
                message += getString(R.string.davila_ms_location);
                location1 = MSTA;
                break;
            case 16:
            case 18:
                message += getString(R.string.rl_location);
                location1 = RL;
                break;
            case 15:
                message += getString(R.string.davila_rps_location);
                location1 = PSTA;
                break;
            case 19:
                message += getString(R.string.davila_ps_location);
                location1 = SUBEPI;
                break;
        }
    }

}

