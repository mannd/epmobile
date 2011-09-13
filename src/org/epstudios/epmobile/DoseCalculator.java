package org.epstudios.epmobile;

public class DoseCalculator {
	static final int NUM_DAYS = 7;
	// probably a more Java-esque way to do below exists...
	public static final int SUN = 0;
	public static final int MON = 1;
	public static final int TUE = 2;
	public static final int WED = 3;
	public static final int THU = 4;
	public static final int FRI = 5;
	public static final int SAT = 6;
	
	public enum Order { INCREASE, DECREASE }
	
	// order in which to add or subtract doses
	private static final int orderedDays[] = { MON, FRI, WED, SAT, TUE, THU, SUN };
	
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
		if (tabletDose * 2 * NUM_DAYS < weeklyDose) // should use bigger tablets
			return result;
		if (weeklyDose == tabletDose * NUM_DAYS)  // just a tablet a day
			for (int i = 0; i < NUM_DAYS; i++) {
				result[i] = 1.0;
			}
		else {
			for (int i = 0; i < NUM_DAYS; ++i) {
				result[i] = 1.0;	// start with a tab a day, no skipping days
			}
			if (tabletDose * NUM_DAYS > weeklyDose)
				tryDoses(result, Order.DECREASE, 0);
			else if (tabletDose * NUM_DAYS < weeklyDose)
				tryDoses(result, Order.INCREASE, 0);
			else	// shouldn't happen that they are equal
				return result;
		}
		// return bad zeros if you get here
		return result;
	}
	
	public double[] tryDoses(double[] doses, Order order, int nextDay) {
		// recursive algorithm, finds closest dose (1st >= target)
		if (order == Order.DECREASE) {
			while (actualWeeklyDose(doses) > weeklyDose) {
				// check for all half tablets, we're done
				if (allHalfTablets(doses)) {
					zeroDoses(doses);
					return doses;
				}
				if (doses[orderedDays[nextDay]] > 0.5)
					doses[orderedDays[nextDay]] = doses[orderedDays[nextDay]] - 0.5;
				++nextDay;
				if (nextDay >= NUM_DAYS - 1)
					nextDay = 0;
				tryDoses(doses, order, nextDay);
			}
			
		}	
		if (order == Order.INCREASE) {
			while (actualWeeklyDose(doses) < weeklyDose) {
				// check for all double tablets, we're done
				if (allDoubleTablets(doses)) {
					zeroDoses(doses);
					return doses;
				}
				if (doses[orderedDays[nextDay]] < 2.0)
					doses[orderedDays[nextDay]] = doses[orderedDays[nextDay]] + 0.5;
				++nextDay;
				if (nextDay >= NUM_DAYS - 1)
					nextDay = 0;
				tryDoses(doses, order, nextDay);
			}			
		}
		return doses;
	}
	
	private Boolean allHalfTablets(double[] doses) {
		Boolean allHalfTabs = true;
		for (int i = 0; i < doses.length; ++i)
			if (doses[i] != 0.5)
				allHalfTabs = false;
		return allHalfTabs;
	}
	
	private Boolean allDoubleTablets(double[] doses) {
		Boolean allDoubleTabs = true;
		for (int i = 0; i < doses.length; ++i)
			if (doses[i] != 2.0)
				allDoubleTabs = false;
		return allDoubleTabs;
	}
	
	private void zeroDoses(double[] doses) {
		for (int i = 0; i < doses.length; ++i)
			doses[i] = 0.0;
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
