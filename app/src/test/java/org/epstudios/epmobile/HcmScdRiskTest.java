package org.epstudios.epmobile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/19/24.
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
public class HcmScdRiskTest {
    @Test(expected = ParsingException.class)
    public void testHcmScdRisk() throws ParsingException {
        HcmRiskScdModel model = new HcmRiskScdModel();
        try {
            model.calculateResult();
        } catch (AgeOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (LvWallThicknessOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (LvotGradientOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (LaSizeOutOfRangeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = AgeOutOfRangeException.class)
    public void testThrowsAgeOutOfRangeException() throws AgeOutOfRangeException {
        HcmRiskScdModel model = new HcmRiskScdModel("2", "30", "30", "39", false, false, false);
        try {
            model.calculateResult();
        } catch (LvWallThicknessOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (LvotGradientOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (LaSizeOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (ParsingException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testCalculateResult() {
        HcmRiskScdModel model = new HcmRiskScdModel("30", "30", "30", "30", false, false, false);
        HcmRiskScdModel model1 = new HcmRiskScdModel("30", "30", "30", "30", true, false, false);
        HcmRiskScdModel model2 = new HcmRiskScdModel("30", "30", "30", "30", true, true, false);
        HcmRiskScdModel model3 = new HcmRiskScdModel("30", "30", "30", "30", true, true, true);
        HcmRiskScdModel model4 = new HcmRiskScdModel("30", "30", "60", "30", true, true, true);
        try {
            double result = model.calculateResult();
            assertEquals(0.024, result, 0.001);
            double result1 = model1.calculateResult();
            assertEquals(0.039, result1, 0.001);
            double result2 = model2.calculateResult();
            assertEquals(0.085, result2, 0.001);
            double result3 = model3.calculateResult();
            assertEquals(0.166, result3, 0.001);
            double result4 = model4.calculateResult();
            assertEquals(0.187, result4, 0.001);
        } catch (Exception e) { }
    }
}
