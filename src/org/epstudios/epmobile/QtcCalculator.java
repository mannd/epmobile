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
	public enum QtcFormula { BAZETT, FRIDERICIA, SAGIE, HODGES } 
	// note Sagie also referred to as Framingham test in literature.
	
	public static int calculate(int rr, int qt, QtcFormula formula) {
		int result = 0;
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
		return result;
	}
	
	private static double msecToSec(int interval) {
		return  interval / 1000.0;
	}
	
	private static int secToMsec(double interval) {
		return (int) Math.round((interval * 1000));
	}
	
	private static int calculateQtcBazett(double rrSec, double qtSec) {
		double qtcSec = qtSec / Math.sqrt(rrSec);
		return secToMsec(qtcSec);
	}
	
	private static int calculateQtcFridericia(double rrSec, double qtSec) {
		double qtcSec = qtSec / Math.cbrt(rrSec);
		return secToMsec(qtcSec);
	}
	
	private static int calculateQtcSagie(double rrSec, double qtSec) {
		double qtcSec = qtSec + 0.154 * (1.0 - rrSec);
		return secToMsec(qtcSec);
	}
	
	private static int calculateQtcHodges(double rrSec, double qtSec) {
		if (rrSec == 0)
			return 0;	// avoid divide by zero
		double hr =  (60000 / (rrSec * 1000)); // double: avoid rounding error
		double qtcSec = qtSec + ((1.75 * (hr - 60) / 1000));
		return secToMsec(qtcSec);
	}
	
}
