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

import org.epstudios.epmobile.features.calculators.ui.Warfarin;

public class WarfarinTest extends TestCase {
	public void testgetDoseFromPercentage() {
		double newDose = Warfarin.getNewDoseFromPercentage(-.2, 14.0, true);
		assertEquals(11.0, newDose);
		newDose = Warfarin.getNewDoseFromPercentage(-.15, 14.0, true);
		assertEquals(12.0, newDose);
		newDose = Warfarin.getNewDoseFromPercentage(-.05, 14.0, true);
		assertEquals(13.0, newDose);
		newDose = Warfarin.getNewDoseFromPercentage(+.05, 14.0, true);
		assertEquals(15.0, newDose);
		newDose = Warfarin.getNewDoseFromPercentage(+.15, 14.0, true);
		assertEquals(16.0, newDose);
		newDose = Warfarin.getNewDoseFromPercentage(+.20, 14.0, true);
		assertEquals(17.0, newDose);

	}

	public void testWeeklyDoseIsSane() {
		assertTrue(Warfarin.weeklyDoseIsSane(15.0, 2.0));
		assertTrue(Warfarin.weeklyDoseIsSane(15.0, 2.5));
		assertTrue(Warfarin.weeklyDoseIsSane(15.0, 5.0));
		assertFalse(Warfarin.weeklyDoseIsSane(0.0, 2.0));
		assertTrue(Warfarin.weeklyDoseIsSane(10.1, 2.0));
		assertTrue(Warfarin.weeklyDoseIsSane(6.0, 2.0));
		assertTrue(Warfarin.weeklyDoseIsSane(80.0, 7.5));
	}

}
