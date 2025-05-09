package org.epstudios.epmobile;

import android.content.Context;

/**
 * Copyright (C) 2022 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 9/1/22.
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

public class Reference {
    public Reference(Context context, int textId, int linkId) {
        this.text= context.getString(textId);
        this.link= context.getString(linkId);
    }

    public Reference(String text, String link) {
        this.text = text;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    private String text;
    private String link;
}
