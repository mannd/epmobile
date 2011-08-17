package org.epstudios.epmobile;

import java.lang.Math;

public class QtcCalculator {
	public int getQtc(int cycleLength, int qt) {
		double realCycleLength = cycleLength / 1000;
		double realQt = qt / 1000;
		double realResult = realQt / Math.sqrt(realCycleLength);
		return (int) realResult * 1000;
	}

}
