package org.epstudios.epmobile;

public class CreatinineClearance {
	public static int calculate(Boolean isMale, double age, double weight,
			double creatinine) {
		double crClr = 0;
		crClr = (140 - age) * weight;
		crClr = crClr / (72 * creatinine);
		if (!isMale)
			crClr = crClr * 0.85;
		return (int) Math.round(crClr); // avoid round off error
	}

}
