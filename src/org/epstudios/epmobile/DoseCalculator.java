package org.epstudios.epmobile;

public class DoseCalculator {
	static final int NUM_DAYS = 7;

	public static final int SUN = 0;
	public static final int MON = 1;
	public static final int TUE = 2;
	public static final int WED = 3;
	public static final int THU = 4;
	public static final int FRI = 5;
	public static final int SAT = 6;

	public enum Order {
		INCREASE, DECREASE
	}

	// order in which to add or subtract doses
	private static final int orderedDays[] = { MON, FRI, WED, SAT, TUE, THU,
			SUN };

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

	public static double getNewDoseFromPercentage(double percent,
			double oldDose, boolean isIncrease) {
		return Math.round(oldDose
				+ (isIncrease ? oldDose * percent : -oldDose * percent));
	}

	public double[] weeklyDoses() {
		double result[] = new double[NUM_DAYS];
		for (int i = 0; i < NUM_DAYS; ++i) {
			result[i] = 1.0;
		}
		if (weeklyDose == tabletDose * NUM_DAYS) // just a tablet a day
			return result;
		if (tabletDose * NUM_DAYS > weeklyDose)
			tryDoses(result, Order.DECREASE, 0);
		else
			tryDoses(result, Order.INCREASE, 0);
		return result;

	}

	public void tryDoses(double[] doses, Order order, int nextDay) {
		// recursive algorithm, finds closest dose (1st >= target)
		boolean allowZeroDoses = false;
		if (order == Order.DECREASE) {
			while (actualWeeklyDose(doses) > weeklyDose) {
				// check for all half tablets, we're done
				if (allHalfTablets(doses)) {
					allowZeroDoses = true;
				}
				double value = doses[orderedDays[nextDay]];
				if (allowZeroDoses && value > 0.0 || value > 0.5)
					doses[orderedDays[nextDay]] = value - 0.5;
				++nextDay;
				if (nextDay > NUM_DAYS - 1)
					nextDay = 0;
				tryDoses(doses, order, nextDay);
			}

		}
		if (order == Order.INCREASE) {
			while (actualWeeklyDose(doses) < weeklyDose) {
				// check for all double tablets, we're done
				if (allDoubleTablets(doses)) {
					return;
				}
				double value = doses[orderedDays[nextDay]];
				if (value < 2.0)
					doses[orderedDays[nextDay]] = value + 0.5;
				++nextDay;
				if (nextDay > NUM_DAYS - 1)
					nextDay = 0;
				tryDoses(doses, order, nextDay);
			}
		}
	}

	private Boolean allHalfTablets(double[] doses) {
		Boolean allHalfTabs = true;
		for (int i = 0; i < doses.length; ++i)
			if (doses[i] > 0.5)
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

	public double actualWeeklyDose(double[] doses) {
		double dose = 0;
		for (int i = 0; i < NUM_DAYS; ++i)
			dose = dose + doses[i] * tabletDose;
		return dose;
	}

	private double tabletDose;
	private double weeklyDose;

}
