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

import junit.framework.TestCase;

import org.epstudios.epmobile.QtcCalculator;
import org.epstudios.epmobile.QtcCalculator.QtcFormula;

public class QtcCalculatorTest extends TestCase {

	public QtcCalculatorTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCalculate() {
		assertEquals(0, QtcCalculator.calculate(0, 0, QtcFormula.BAZETT));
		assertEquals(400, QtcCalculator.calculate(1000, 400, QtcFormula.BAZETT));
		assertEquals(460, QtcCalculator.calculate(863, 427, QtcFormula.BAZETT));
	}
	
	public void testCalculateFridericia() {
		assertEquals(0, QtcCalculator.calculate(0, 0, QtcFormula.FRIDERICIA));
		assertEquals(400, QtcCalculator.calculate(1000, 400, QtcFormula.FRIDERICIA));
		assertEquals(448, QtcCalculator.calculate(863, 427, QtcFormula.FRIDERICIA));
	}
	
	public void testCalculateSagie() {
		assertEquals(154, QtcCalculator.calculate(0, 0, QtcFormula.SAGIE));
		assertEquals(400, QtcCalculator.calculate(1000, 400, QtcFormula.SAGIE));
		assertEquals(448, QtcCalculator.calculate(863, 427, QtcFormula.SAGIE));
	}
	
	public void testCalculateHodges() {
		assertEquals(0, QtcCalculator.calculate(0, 0, QtcFormula.HODGES));
		assertEquals(400, QtcCalculator.calculate(1000, 400, QtcFormula.HODGES));
		assertEquals(444, QtcCalculator.calculate(863, 427, QtcFormula.HODGES));
	}
	
}
