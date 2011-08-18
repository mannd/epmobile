package org.epstudios.epmobile;

public class QtcCalculator {
	public enum QtcFormula { BAZETT, FRIDERICIA, SAGIE } 
	
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
		}
		return result;
	}
	
	private static double msecToSec(int interval) {
		return  interval / 1000.0;
	}
	
	private static int secToMsec(double interval) {
		return (int) (interval * 1000);
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
	
}
