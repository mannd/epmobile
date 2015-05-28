/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 5/28/15.
 * <p/>
 * This file is part of epmobile.
 * <p/>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class HcmScd extends RiskScore
{  // TODO implement
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.hcmscd);
        // super sets up toolbar, so need layout first.  Call
        // after setContentView().
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void calculateResult() {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void init() {
        checkBox = new CheckBox[3];

        checkBox[0] = (CheckBox) findViewById(R.id.family_hx_scd);
        checkBox[1] = (CheckBox) findViewById(R.id.nsvt);
        checkBox[2] = (CheckBox) findViewById(R.id.unexplained_syncope);
    }

    @Override
    protected String getFullReference() {
        return null;
    }

    @Override
    protected String getRiskLabel() {
        return null;
    }

    protected String getShortReference() {
        return "";
    }
}
