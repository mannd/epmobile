package org.epstudios.epmobile;

import org.epstudios.epmobile.features.riskscores.data.ArvcRiskModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright (C) 2019 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 4/15/19.
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
 * along with epmobile.  If not, see <<a href="http://www.gnu.org/licenses/">...</a>>.
 */
public class ArvcRiskModelTest {
    @Test
    public void testCalculateRisk() {
        ArvcRiskModel pt1 = new ArvcRiskModel(0, 48, 0, 0,
                1, 4, 55 );
        assertEquals(2.4, pt1.calculateRisk(ArvcRiskModel.year5), 0.0001);
        assertEquals(1.4, pt1.calculateRisk(ArvcRiskModel.year2), 0.0001);
        assertEquals(0.9, pt1.calculateRisk(ArvcRiskModel.year1), 0.0001);

        ArvcRiskModel pt2 = new ArvcRiskModel(1, 50, 0, 0,
                312, 3, 48 );
        assertEquals(10.1, pt2.calculateRisk(ArvcRiskModel.year5), 0.0001);
        assertEquals(6.2, pt2.calculateRisk(ArvcRiskModel.year2), 0.0001);
        assertEquals(3.8, pt2.calculateRisk(ArvcRiskModel.year1), 0.0001);

        ArvcRiskModel pt3 = new ArvcRiskModel(0, 22, 0, 1,
                20527, 4, 28 );
        assertEquals(64.1, pt3.calculateRisk(ArvcRiskModel.year5), 0.0001);
        assertEquals(45.8, pt3.calculateRisk(ArvcRiskModel.year2), 0.0001);
        assertEquals(31.4, pt3.calculateRisk(ArvcRiskModel.year1), 0.0001);

        ArvcRiskModel pt4 = new ArvcRiskModel(1, 15, 1, 0,
                1800, 5, 66 );
        assertEquals(37.7, pt4.calculateRisk(ArvcRiskModel.year5), 0.0001);
        assertEquals(24.6, pt4.calculateRisk(ArvcRiskModel.year2), 0.0001);
        assertEquals(16.0, pt4.calculateRisk(ArvcRiskModel.year1), 0.0001);
        ArvcRiskModel pt5 = new ArvcRiskModel(1, 15, 1, 0,
                0, 5, 66 );
        assertEquals(12.4, pt5.calculateRisk(ArvcRiskModel.year5), 0.0001);
        assertEquals(7.6, pt5.calculateRisk(ArvcRiskModel.year2), 0.0001);
        assertEquals(4.8, pt5.calculateRisk(ArvcRiskModel.year1), 0.0001);
    }


}
