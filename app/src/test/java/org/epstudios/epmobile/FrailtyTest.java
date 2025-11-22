package org.epstudios.epmobile;

import org.epstudios.epmobile.features.riskscores.ui.Frailty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright (C) 2023 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 10/25/23.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

public class FrailtyTest {
    @Test
    public void testRules() {
        Frailty.FrailtyRule noYesRule = Frailty.getNoYesRule();
        Frailty.FrailtyRule fitnessRule = Frailty.getFitnessRule();
        Frailty.FrailtyRule independentRule = Frailty.getIndependentDependentRule();
        Frailty.FrailtyRule sometimesNoYesRule = Frailty.getSometimesNoYesRule();
        Frailty.FrailtyRule sometimesYesNoRule = Frailty.getSometimesYesNoRule();
        assertEquals(0, noYesRule.operation(0));
        assertEquals(1, noYesRule.operation(1));
        assertEquals(1, fitnessRule.operation(0));
        assertEquals(1, fitnessRule.operation(6));
        assertEquals(0, fitnessRule.operation(7));
        assertEquals(0, fitnessRule.operation(10));
        assertEquals(0, independentRule.operation(0));
        assertEquals(1, independentRule.operation(1));
        assertEquals(0, sometimesNoYesRule.operation(0));
        assertEquals(0, sometimesNoYesRule.operation(1));
        assertEquals(1, sometimesNoYesRule.operation(2));
        assertEquals(0, sometimesYesNoRule.operation(0));
        assertEquals(1, sometimesYesNoRule.operation(1));
        assertEquals(1, sometimesYesNoRule.operation(2));
    }

}
