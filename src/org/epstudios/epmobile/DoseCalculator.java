package org.epstudios.epmobile;

public class DoseCalculator {
	static final int NUM_DAYS = 7;
	private enum Day { SUN, MON, TUE, WED, THU, FRI, SAT }
	
	// order in which to add or subtract doses
	private static final Day orderedDays[] = {
		Day.MON, Day.FRI, Day.WED, Day.SAT, Day.TUE, Day.THU, Day.SUN};
	
	public DoseCalculator(double tabletDose, double weeklyDose) {
		this.tabletDose = tabletDose;
		this.weeklyDose = weeklyDose;
	}
	
	public void setTabletDose(double tabletDose) {
		this.tabletDose = tabletDose;
	}
	
	public void setWeeklyDose(double weeklyDose) {
		this.weeklyDose = weeklyDose;
	}
	
	public double[] weeklyDoses() {
		double result[] = new double [NUM_DAYS];
		// zeros are bad
		if (tabletDose == 0 || weeklyDose == 0)
			return result;
		// half a tab daily more than weekly dose - bad!
		if (tabletDose * 0.5 * NUM_DAYS > weeklyDose)
			return result;
		if (weeklyDose % tabletDose == 0)
			for (int i = 0; i < NUM_DAYS; i++) {
				double numTablets = weeklyDose/ tabletDose / NUM_DAYS;
				result[i] = numTablets;
			}
		else {
			for (int i = 0; i < NUM_DAYS; ++i) {
				result[i] = 0.5;	// start with half tab a day, no skipping days
			}
			result = tryDoses(result);
		}
		// return bad zeros if you get here
		return result;
	}
	
	public double[] tryDoses(double[] doses) {
		// recursive algorithm, finds closest dose (1st >= target)
		//while (actualWeeklyDose(doses) <= weeklyDose) {
		// just return same for now
		return doses;
			
	}
	
	public double actualWeeklyDose(double[] doses) {
		double dose = 0;
		for (int i = 0; i < doses.length; ++i)
			dose = dose + doses[i] * tabletDose;
		return dose;
	}
	
	
	private double tabletDose;
	private double weeklyDose;

}
