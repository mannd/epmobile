package org.epstudios.epmobile

import org.junit.Test
import kotlin.test.assertEquals

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 4/27/25.

This file is part of epmobile.

epmobile is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epmobile is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

class BmiTest {
    @Test
    fun testBmi() {
        val bmi1 = BMI()
        assertEquals(20.1, bmi1.calculate(65.0, 1.8), 0.05)
        assertEquals(20.1, bmi1.calculateCm(65.0, 180.0), 0.05)
        assertEquals(20.1, bmi1.calculateRounded(65.0, 1.8))
        assertEquals(20.1, bmi1.calculateCmRounded(65.0, 180.0))
        assertEquals(20.3, bmi1.calculateUSUnits(150.0, 72.0), 0.05)
        assertEquals(20.3, bmi1.calculateUSUnitsRounded(150.0, 72.0))
    }
}