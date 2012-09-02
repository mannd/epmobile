package org.epstudios.epmobile;

public class CreatinineClearance {
	public static int calculate(boolean isMale, double age, double weight,
			double creatinine, boolean usingMicroMolUnits) {

		return (int) Math.round(calculateDouble(isMale, age, weight,
				creatinine, usingMicroMolUnits));
	}

	public static double calculateDouble(boolean isMale, double age,
			double weight, double creatinine, boolean usingMicroMolUnits) {
		double crClr = (140.0 - age) * weight;
		if (!usingMicroMolUnits) {

			crClr = crClr / (72 * creatinine);
			if (!isMale)
				crClr = crClr * 0.85;
		} else {
			if (isMale)
				crClr = crClr * 1.2291;
			else
				crClr = crClr * 1.0447;
			crClr = crClr / creatinine;
		}
		// don't allow negative creatinine clearance
		return (crClr < 0) ? 0 : crClr;
	}

}
