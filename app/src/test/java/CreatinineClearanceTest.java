/*  EP Mobile -- Mobile tools for electrophysiologists
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


import org.epstudios.epmobile.CreatinineClearance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CreatinineClearanceTest {
	@Test
	public void testCalculate() {
		assertEquals(0, CreatinineClearance.calculate(true, 0, 0, 0, false));
		assertEquals(0, CreatinineClearance.calculate(true, 140, 0, 0, false));
		assertEquals(58,
				CreatinineClearance.calculate(true, 77, 65, 0.98, false));
		assertEquals((int) (0.85 * 58),
				CreatinineClearance.calculate(false, 77, 65, 0.98, false));
		// next one tests for round off, precise answer is 47.79, should round
		// to 48
		assertEquals(48,
				CreatinineClearance.calculate(true, 77, 65, 1.19, false));
		// test microMol/L units
		assertEquals(56, CreatinineClearance.calculate(true, 77, 65, 90, true));
		assertEquals(48, CreatinineClearance.calculate(false, 77, 65, 90, true));
		assertEquals(47.5,
				CreatinineClearance.calculateDouble(false, 77, 65, 90, true),
				0.1);

	}
}
