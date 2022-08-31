package org.epstudios.epmobile;

/**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 8/31/22.
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

import android.text.Spanned;
import org.junit.Test;
import androidx.annotation.StringRes;

import static android.provider.Settings.System.getString;
import static org.junit.Assert.assertEquals;

public class HtmlTest {
    @Test
    public void testHtml() {
        String result = EpActivity.convertReferenceToHtmlString("Test Reference", "https://www.google.com");
        assertEquals(result, "<p>Test Reference<br/><a href =\"https://www.google.com\">Link to reference</a></p>");
    }
}
