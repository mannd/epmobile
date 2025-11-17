package org.epstudios.epmobile;/*  EP Mobile -- Mobile tools for electrophysiologists
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

import junit.framework.TestCase;

import org.epstudios.epmobile.features.calculators.data.CreatinineClearance;
import org.epstudios.epmobile.features.calculators.data.DoseCalculator;
import org.epstudios.epmobile.features.calculators.ui.Warfarin;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DoseCalculatorTest extends TestCase {

	public void testWeeklyDosesEqual() {
		DoseCalculator d = new DoseCalculator(5, 35);
		double[] result = d.weeklyDoses();
		for (int i = 0; i < result.length; ++i)
			assertEquals(1.0, result[i]);
	}

	public void testActualWeeklyDose() {
		DoseCalculator d = new DoseCalculator(2.5, 30);
		double doses[] = { 1, 2, 2, 3.5, 7, 0, 0 };
		assertEquals(38.75, d.actualWeeklyDose(doses));
		double doses2[] = { 1, 1, 1, 0, 0, 0, 0 };
		assertEquals(7.5, d.actualWeeklyDose(doses2));
	}

	public void testRealDoses() {
		DoseCalculator d = new DoseCalculator(5.0, 40.0);
		double result[] = d.weeklyDoses();
		assertEquals(40.0, d.actualWeeklyDose(result));
		// some tests from predefined tables in references
		d.setTabletDose(2.0);
		d.setWeeklyDose(11.0);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(0.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(0.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(16.0);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(17.0);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(2.5);
		d.setWeeklyDose(18.75);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(5.0);
		d.setWeeklyDose(42.5);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(32.5);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(7.5);
		d.setWeeklyDose(41.25);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(0.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(0.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(56.25);
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
	}

	public void testCalculatedRealDoses() {
		DoseCalculator d = new DoseCalculator(5.0, 40.0);
		double result[] = d.weeklyDoses();
		assertEquals(40.0, d.actualWeeklyDose(result));
		d.setTabletDose(2.0);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(-.2, 14.0, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(0.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(0.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(.05, 14.0, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(.2, 14.0, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(2.5);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(.05, 17.5, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(5.0);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(.2, 35.0, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(-.05, 35.0, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setTabletDose(7.5);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(-.2, 52.5, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(0.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(0.5, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(0.5, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);
		d.setWeeklyDose(Warfarin.getNewDoseFromPercentage(.05, 52.5, true));
		result = d.weeklyDoses();
		assertEquals(1.0, result[DoseCalculator.SUN]);
		assertEquals(1.5, result[DoseCalculator.MON]);
		assertEquals(1.0, result[DoseCalculator.TUE]);
		assertEquals(1.0, result[DoseCalculator.WED]);
		assertEquals(1.0, result[DoseCalculator.THU]);
		assertEquals(1.0, result[DoseCalculator.FRI]);
		assertEquals(1.0, result[DoseCalculator.SAT]);

	}

	private Boolean resultIsZero(double[] result) {
		double num = 0.0;
		for (int i = 0; i < result.length; ++i)
			num = num + result[i];
		return num == 0.0;
	}

	public static class CreatinineClearanceTest {
		@Test
		public void testCalculate() {
			Assert.assertEquals(0, CreatinineClearance.calculate(true, 0, 0, 0, false));
			Assert.assertEquals(0, CreatinineClearance.calculate(true, 140, 0, 0, false));
			Assert.assertEquals(58,
					CreatinineClearance.calculate(true, 77, 65, 0.98, false));
			Assert.assertEquals((int) (0.85 * 58),
					CreatinineClearance.calculate(false, 77, 65, 0.98, false));
			// next one tests for round off, precise answer is 47.79, should round
			// to 48
			Assert.assertEquals(48,
					CreatinineClearance.calculate(true, 77, 65, 1.19, false));
			// test microMol/L units
			Assert.assertEquals(56, CreatinineClearance.calculate(true, 77, 65, 90, true));
			Assert.assertEquals(48, CreatinineClearance.calculate(false, 77, 65, 90, true));
			Assert.assertEquals(47.5,
					CreatinineClearance.calculateDouble(false, 77, 65, 90, true),
					0.1);

		}
	}
}
