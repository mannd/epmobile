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
            double dummy = model.calculateResult();
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
    public void testAgeException() throws AgeOutOfRangeException {
        HcmRiskScdModel model = new HcmRiskScdModel("2", "30", "30", "39", false, false, false);
        try {
            double dummy = model.calculateResult();
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
}
