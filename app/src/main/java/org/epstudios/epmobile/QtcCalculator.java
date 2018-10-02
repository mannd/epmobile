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

public class QtcCalculator {
	public enum QtcFormula {
		BAZETT, FRIDERICIA, SAGIE, HODGES
	}

	// note Sagie also referred to as Framingham test in literature.

	public static int calculate(int rr, int qt, QtcFormula formula) {
		double result = 0.0;
		double rrSec = msecToSec(rr);
		double qtSec = msecToSec(qt);
		switch (formula) {
		case BAZETT:
			result = calculateQtcBazett(rrSec, qtSec);
			break;
		case FRIDERICIA:
			result = calculateQtcFridericia(rrSec, qtSec);
			break;
		case SAGIE:
			result = calculateQtcSagie(rrSec, qtSec);
			break;
		case HODGES:
			result = calculateQtcHodges(rrSec, qtSec);
			break;
		}
		return secToMsec(result);
	}

	private static double msecToSec(int interval) {
		return interval / 1000.0;
	}

	private static int secToMsec(double interval) {
		return (int) Math.round((interval * 1000));
	}

	private static double calculateQtcBazett(double rrSec, double qtSec) {
		return qtSec / Math.sqrt(rrSec);
	}

	private static double calculateQtcFridericia(double rrSec, double qtSec) {
		return qtSec / Math.cbrt(rrSec);
	}

	private static double calculateQtcSagie(double rrSec, double qtSec) {
		return qtSec + 0.154 * (1.0 - rrSec);
	}

	private static double calculateQtcHodges(double rrSec, double qtSec) {
		if (rrSec == 0)
			return 0; // avoid divide by zero
		double hr = (60000 / (rrSec * 1000)); // double: avoid rounding error
		return qtSec + ((1.75 * (hr - 60) / 1000));
	}

	public static long qtCorrectedForLBBB(int qt, int qrs) {
        return Math.round(qt - (qrs * 0.485));
    }

    public static long jtInterval(int qt, int qrs) {
        return Math.round(qt - qrs);
    }

    public static int jtCorrected(int qt, int rr, int qrs) {
        int result = calculate(rr, qt, QtcFormula.BAZETT);
        result -= qrs;
        return result;
    }

    public static long qtRrIvcd(int qt, double hr, int qrs, boolean isMale) {
        double k = isMale ? -22 : -34;
        return Math.round(qt - 155 * (60/hr - 1) - 0.93 * (qrs - 139) + k);
    }

    // From https://www.jecgonline.com/article/S0022-0736(17)30455-7/fulltext?platform=hootsuite#s0050
    // note: assumes LBBB, values in msec
    public static long preLbbbQtc(int qt, int rr, int qrs, boolean isMale) {
		int k = isMale ? 95 : 88;
		int qtc = calculate(rr, qt, QtcFormula.BAZETT);
		return qtc - qrs + k;
	}

}
