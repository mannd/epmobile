package org.epstudios.epmobile;

public class QtcCalculator {
	public enum QtcFormula { BAZETT, FRIDERICIA, REGRESSION } 
	
	public static int calculate(int rr, int qt, QtcFormula formula) {
		int result = 0;
		switch (formula) {
		case BAZETT:
			result = calculateQtcBazett(rr, qt);
			break;
		}
		return result;
	}
	
	private static int calculateQtcBazett(int rr, int qt) {
		double decimalRr = rr / 1000.0;
		double decimalQt = qt / 1000.0;
		double decimalQtc = decimalQt / Math.sqrt(decimalRr);
		int qtc =  (int) (decimalQtc * 1000);
		return qtc;
	}
	
}
