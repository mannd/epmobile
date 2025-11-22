package org.epstudios.epmobile; /**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 8/25/22.
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

import org.epstudios.epmobile.core.data.Gfr;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GfrTest {
    @Test
    public void testGrf() {
        double gfr1 = Gfr.ckdEpiGfr(1.0, 55, true, false);
        assertEquals(Math.round(gfr1), 84);
        double gfr2 = Gfr.ckdEpiGfr(0.66, 67, false, true);
        assertEquals(Math.round(gfr2), 106);
        double gfr3 = Gfr.ckdEpiGfr(4.5, 20, false, false);
        assertEquals(Math.round(gfr3), 13);
    }
}
